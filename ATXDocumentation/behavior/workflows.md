> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Application Workflows — OpenMRS Core 2.0.3

> **Note**: Only application-level workflows are documented. Build/test/CI/CD workflows are excluded.

## 1. Patient Registration Workflow

```
User Request → PatientService.savePatient(Patient)
    │
    ├── [AOP] AuthorizationAdvice: Check ADD_PATIENTS privilege
    ├── [AOP] LoggingAdvice: Log method entry
    ├── [AOP] RequiredDataAdvice: Set creator, dateCreated
    │
    ├── checkPatientIdentifiers(patient)
    │   ├── Validate required identifier types present
    │   ├── Validate check digits
    │   ├── Validate format (regex)
    │   └── Check uniqueness
    │
    ├── PersonService.savePerson(patient)  [via Context]
    │   ├── Save person names
    │   ├── Save person addresses
    │   └── Save person attributes
    │
    ├── PatientDAO.savePatient(patient)
    │   └── Hibernate: session.saveOrUpdate(patient)
    │
    └── Return saved Patient
```

## 2. Clinical Encounter Workflow

```
User Request → EncounterService.saveEncounter(Encounter)
    │
    ├── [AOP] Check EDIT_ENCOUNTERS or ADD_ENCOUNTERS privilege
    ├── Set encounterDatetime = now() if not set
    │
    ├── Validate encounter:
    │   ├── Patient must be set
    │   ├── EncounterType must be set
    │   └── Location should be set
    │
    ├── For each Obs in encounter.getObs():
    │   ├── Set obs.encounter = this encounter
    │   ├── Set obs.person = encounter.patient
    │   └── ObsService.saveObs(obs)
    │
    ├── For each Order in encounter.getOrders():
    │   ├── Set order.encounter = this encounter
    │   ├── Set order.patient = encounter.patient
    │   └── OrderService.saveOrder(order)
    │
    ├── For each EncounterProvider:
    │   └── Save provider-role assignment
    │
    ├── EncounterDAO.saveEncounter(encounter)
    │   └── Hibernate: session.saveOrUpdate(encounter)
    │
    └── Return saved Encounter
```

## 3. Order Placement and Lifecycle Workflow

```
Order Placement:
    OrderService.saveOrder(order, orderContext)
    │
    ├── [AOP] Check ADD_ORDERS privilege
    ├── Validate via OrderValidator / DrugOrderValidator
    │
    ├── If order.action == NEW:
    │   ├── Generate orderNumber via OrderNumberGenerator
    │   ├── Set dateActivated = now() if not set
    │   ├── If DrugOrder: calculate autoExpireDate from duration/frequency
    │   └── OrderDAO.saveOrder(order)
    │
    ├── If order.action == REVISE:
    │   ├── Validate previousOrder exists and is active
    │   ├── Copy relevant fields from previousOrder
    │   ├── Set dateStopped on previousOrder
    │   └── Save both orders
    │
    ├── If order.action == DISCONTINUE:
    │   ├── Validate order to discontinue is active
    │   ├── Create discontinuation order
    │   ├── Set dateStopped on original order
    │   └── Save discontinuation order
    │
    └── Return saved Order

Order State Machine:
    [NEW] → [ACTIVE] → [REVISED] → [ACTIVE (new version)]
                     → [DISCONTINUED]
                     → [EXPIRED] (autoExpireDate reached)
                     → [COMPLETED]
```

## 4. HL7 Message Processing Pipeline

```
External HL7 Source → HL7InQueue (database table)
    │
    ├── ProcessHL7InQueueTask (scheduled task)
    │   └── HL7InQueueProcessor.processHL7InQueue()
    │       │
    │       ├── Dequeue HL7InQueue message
    │       ├── Parse HL7 message using HAPI
    │       ├── Determine message type (MSH segment)
    │       │
    │       ├── If ADT^A28 (Patient Registration):
    │       │   └── ADTA28Handler.processMessage()
    │       │       ├── Extract patient demographics from PID
    │       │       ├── Create/update Person and Patient
    │       │       └── PatientService.savePatient()
    │       │
    │       ├── If ORU^R01 (Observation Result):
    │       │   └── ORUR01Handler.processMessage()
    │       │       ├── Extract patient from PID
    │       │       ├── Create Encounter from PV1
    │       │       ├── For each OBX segment:
    │       │       │   ├── Map concept via ConceptService
    │       │       │   ├── Create Obs with value
    │       │       │   └── Add to encounter
    │       │       └── EncounterService.saveEncounter()
    │       │
    │       ├── On success: Move to HL7InArchive
    │       └── On error: Move to HL7InError with error message
```

## 5. Application Startup/Initialization Flow

```
Servlet Container starts webapp
    │
    ├── Listener.contextInitialized() [ServletContextListener]
    │   ├── Load runtime properties (OPENMRS_RUNTIME_PROPERTIES_FILE)
    │   ├── Initialize Log4j
    │   ├── Create Spring ApplicationContext
    │   │   └── Load applicationContext-service.xml
    │   │       ├── Configure Hibernate SessionFactory
    │   │       ├── Register all service beans
    │   │       └── Apply AOP proxies
    │   │
    │   ├── Context.startup() / Context.openSession()
    │   ├── Start OpenMRS modules (ModuleFactory.loadModules())
    │   └── Start Scheduler (SchedulerService.start())
    │
    ├── If NOT initialized (first-time):
    │   └── StartupFilter → InitializationFilter
    │       ├── Display setup wizard
    │       ├── Collect: database type, credentials, admin user
    │       ├── Run Liquibase schema creation
    │       ├── Create admin user
    │       └── Write runtime properties file
    │
    └── If initialized but needs update:
        └── StartupFilter → UpdateFilter
            ├── Run Liquibase migrations (liquibase-update-to-latest.xml)
            ├── Apply module updates
            └── Redirect to application
```

## 6. Program Enrollment and State Transition Workflow

```
ProgramWorkflowService.savePatientProgram(PatientProgram)
    │
    ├── Set enrollment date, location
    ├── For each ProgramWorkflow in program:
    │   └── Initialize patient state if applicable
    │
    State Transition:
    ├── PatientProgram.transitionToState(ProgramWorkflowState, Date)
    │   ├── Validate state belongs to workflow
    │   ├── Validate state transition is allowed
    │   ├── End current state (set endDate)
    │   └── Create new PatientState with startDate
    │
    └── ProgramWorkflowDAO.savePatientProgram(patientProgram)
```

## 7. Module Loading Lifecycle

```
ModuleFactory.loadModules(Collection<File>)
    │
    ├── For each .omod file:
    │   ├── ModuleFileParser.parse() → Module object
    │   │   ├── Read config.xml (module metadata)
    │   │   ├── Extract required modules and versions
    │   │   └── Extract extension points
    │   │
    │   ├── Create ModuleClassLoader (isolated classpath)
    │   ├── Load module Spring context (moduleApplicationContext.xml)
    │   ├── Register module services in ServiceContext
    │   │
    │   ├── ModuleActivator.willStart()
    │   ├── ModuleActivator.started()
    │   └── Register module filters, servlets, extensions
    │
    └── Module stop lifecycle:
        ├── ModuleActivator.willStop()
        ├── Unregister services, filters, extensions
        ├── Destroy ModuleClassLoader
        └── ModuleActivator.stopped()
```

## Related Documentation

- [Business Logic](business-logic.md)
- [Decision Logic](decision-logic.md)
- [Error Handling](error-handling.md)
- [Components](../architecture/components.md)
