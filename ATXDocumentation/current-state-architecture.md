# OpenMRS Core - Current-State Architecture Analysis

## 1. Maven Module Structure

The project is organized as a multi-module Maven build (version 2.0.3):

```
openmrs (parent POM, packaging: pom)
├── tools        → Build tools and resources (1 Java source file)
├── test         → Test support POM
├── api          → Core API module (649 Java source files)
├── web          → Web layer module (39 Java source files)
└── webapp       → WAR packaging module
```

**Build Order**: tools → test → api → web → webapp (WAR packaging)

**Key Properties**:
- Java Compiler Version: 21
- Spring Framework: 6.1.14
- Hibernate: 5.6.15.Final
- Jakarta Servlet API: 6.0.0
- Jakarta Persistence API: 3.1.0
- Jakarta Validation API: 3.0.2

## 2. Domain Entities (105 classes in org.openmrs)

### Core Entity Hierarchy
- **BaseOpenmrsObject** (abstract) → implements OpenmrsObject
  - **BaseOpenmrsData** (abstract) → implements OpenmrsData
    - Patient, Person, Encounter, Obs, Order, Visit, Cohort, etc.
  - **BaseOpenmrsMetadata** (abstract) → implements OpenmrsMetadata
    - ConceptClass, ConceptDatatype, EncounterType, LocationTag, etc.

### Key Domain Entities
| Entity | Description | Key Relationships |
|--------|-------------|-------------------|
| Patient | Core patient record | extends Person |
| Person | Person demographics | PersonName, PersonAddress, PersonAttribute |
| Encounter | Clinical encounter | Patient, Provider, Visit, Location |
| Obs | Clinical observation | Concept, Encounter, Person |
| Order | Clinical order | Concept, Encounter, Patient, Provider |
| DrugOrder | Drug-specific order | extends Order, Drug |
| TestOrder | Test-specific order | extends Order |
| Concept | Clinical concept | ConceptName, ConceptAnswer, ConceptSet |
| Location | Care location | LocationTag, LocationAttribute |
| User | System user | extends Person (via relationship) |
| Visit | Patient visit | VisitType, VisitAttribute, Encounter |
| Provider | Care provider | ProviderAttribute |
| Drug | Medication | DrugIngredient, Concept |
| Form | Data entry form | FormField, FormResource |
| Cohort | Patient cohort | Patient references |
| Program | Care program | ProgramWorkflow, ProgramWorkflowState |

### Concept Domain (20+ types)
Concept, ConceptAnswer, ConceptAttribute, ConceptAttributeType, ConceptClass,
ConceptComplex, ConceptDatatype, ConceptDescription, ConceptMap, ConceptMapType,
ConceptName, ConceptNameTag, ConceptNumeric, ConceptProposal, ConceptReferenceTerm,
ConceptReferenceTermMap, ConceptSearchResult, ConceptSet, ConceptSource,
ConceptStateConversion, ConceptStopWord, BaseConceptMap

### Order Domain
Order, DrugOrder, TestOrder, OrderType, OrderFrequency, OrderGroup, OrderSet,
OrderSetMember, CareSetting, DosingInstructions, FreeTextDosingInstructions,
SimpleDosingInstructions, Duration

### Shared Interfaces/Contracts
- **OpenmrsObject** - Base interface for all persistent objects
- **OpenmrsData** - Interface for voided data entities
- **OpenmrsMetadata** - Interface for retired metadata entities
- **Auditable** - Audit trail interface (creator, dateCreated, changedBy, dateChanged)
- **Voidable** - Void support interface
- **Retireable** - Retire support interface
- **Attributable** - Custom attribute support

## 3. Service Layer (18 service interfaces in org.openmrs.api)

| Service Interface | Primary Domain | Key Dependencies |
|-------------------|---------------|------------------|
| PatientService | Patient, PatientIdentifier | Person, Concept, Location, Allergy |
| PersonService | Person, PersonName, PersonAddress | PersonAttribute, Relationship |
| ConceptService | Concept + 20 related types | Drug, ConceptSource |
| EncounterService | Encounter, EncounterType | Patient, Provider, Visit, Location, Form |
| ObsService | Obs, ComplexData | Concept, Encounter, Person |
| OrderService | Order, DrugOrder, TestOrder | Concept, Encounter, Patient, Provider |
| OrderSetService | OrderSet, OrderSetMember | Concept |
| LocationService | Location, LocationTag | LocationAttribute |
| UserService | User, Role, Privilege | Person |
| VisitService | Visit, VisitType | Patient, Location, Encounter |
| ProviderService | Provider | Person, ProviderAttribute |
| AdministrationService | GlobalProperty | Configuration management |
| FormService | Form, FormField | Field, FieldType |
| CohortService | Cohort | Patient |
| ProgramWorkflowService | Program, PatientProgram | ProgramWorkflow, Concept |
| DatatypeService | Custom datatypes | Handler registration |
| SerializationService | Object serialization | Serializer implementations |
| HL7Service (in org.openmrs.hl7) | HL7 messages | HL7InQueue, HL7InArchive |

## 4. Service Implementations (20 classes in org.openmrs.api.impl)

All implementations follow the pattern:
- Implement corresponding service interface
- Inject DAO via setter
- Use `@Transactional` for transaction management
- Use `@Authorized` for authorization checks
- Access other services via `Context.getService()`

## 5. DAO Layer

### DAO Interfaces (27 in org.openmrs.api.db)
AdministrationDAO, CohortDAO, ConceptDAO, ContextDAO, DatatypeDAO, EncounterDAO,
FormDAO, LocationDAO, NoteDAO, ObsDAO, OpenmrsDataDAO, OpenmrsMetadataDAO,
OpenmrsObjectDAO, OrderDAO, OrderSetDAO, PatientDAO, PersonDAO,
ProgramWorkflowDAO, ProviderDAO, SerializedObjectDAO, TemplateDAO, UserDAO, VisitDAO

### Hibernate Implementations (38 in org.openmrs.api.db.hibernate)

All Hibernate DAO implementations depend on:
- **DbSession** / **DbSessionFactory** (custom Hibernate session wrapper)
- Hibernate Criteria API for queries
- HBM XML mapping files

## 6. Validators (56 classes in org.openmrs.validator)

Spring Validator implementations for domain entities. Key validators:
PatientValidator, PersonValidator, PersonNameValidator, PersonAddressValidator,
ConceptValidator, EncounterValidator, ObsValidator, OrderValidator,
DrugOrderValidator, TestOrderValidator, LocationValidator, UserValidator, etc.

## 7. HL7 Integration (16 classes in org.openmrs.hl7)

| Component | Description |
|-----------|-------------|
| HL7Service | Service interface for HL7 message processing |
| HL7ServiceImpl | Implementation with queue management |
| HL7DAO / HibernateHL7DAO | Data access for HL7 messages |
| ADTA28Handler | ADT A28 message handler (patient registration) |
| ORUR01Handler | ORU R01 message handler (observation results) |
| HL7InQueue | Incoming HL7 message queue entity |
| HL7InArchive | Processed HL7 message archive entity |
| HL7InError | Error HL7 messages entity |
| HL7InQueueProcessor | Queue processing engine |
| HL7Util | HL7 utility methods |
| HL7Constants | HL7 constants |

**Cross-domain dependencies**: HL7 handlers reference PatientService, ConceptService,
EncounterService, ObsService, PersonService via Context service locator.

## 8. Notification (20 classes in org.openmrs.notification)

AlertService, MessageService, Template, Alert, AlertRecipient, and related classes
for system notifications and messaging.

## 9. Scheduler (23 classes in org.openmrs.scheduler)

SchedulerService, SchedulerServiceImpl, TaskDefinition, and related classes for
scheduled task execution.

## 10. Module System (20 classes in org.openmrs.module)

ModuleFactory, ModuleClassLoader, Module, ModuleActivator, ModuleUtil, Extension,
and related classes for the OpenMRS module loading system.

## 11. Utilities (64 classes in org.openmrs.util)

OpenmrsUtil, OpenmrsConstants, PrivilegeConstants, OpenmrsClassLoader,
DatabaseUtil, Security, HandlerUtil, and other utility classes.

## 12. Property Editors (37 classes in org.openmrs.propertyeditor)

Spring PropertyEditor implementations for binding domain objects from form data:
PatientEditor, PersonEditor, ConceptEditor, LocationEditor, UserEditor, etc.

## 13. Spring Configuration

**applicationContext-service.xml** (693 lines):
- XML-based Spring bean definitions
- All service beans defined with proxying for authorization
- AOP-based method interceptors for @Authorized and @Logging
- Event listener wiring
- Session factory configuration

## 14. Hibernate Configuration

**hibernate.cfg.xml** (120 lines, 84 HBM mapping resources):
- Single session factory configuration
- All 84 HBM mapping files registered in one place
- References entities across all domain areas

## 15. Web Layer (39 Java files)

### Filters
- **InitializationFilter** - First-run database setup wizard
- **UpdateFilter** - Database update handling
- **StartupErrorFilter** - Startup error display
- **GZIPFilter** - Response compression
- **OpenmrsFilter** - Authentication filter
- **JspClassLoaderFilter** - Classloader management

### Servlets
- **DispatcherServlet** - Spring MVC dispatcher (extended)
- **StaticDispatcherServlet** - Static content dispatcher
- **ModuleResourcesServlet** - Module resource serving
- **ModuleServlet** - Module request routing

### Module Web Integration
- **WebModuleUtil** - Module web lifecycle management
- **ModuleFilter** / **ModuleFilterChain** - Module filter support

### Controllers
- **PseudoStaticContentController** - Static content serving

### Web Support
- **Listener** - ServletContextListener for startup/shutdown
- **WebConstants** - Web-layer constants
- **WebUtil** - Web utility methods
- **WebDaemon** - Background web tasks
- **OpenmrsBindingInitializer** - Spring MVC data binding

## 16. Test Infrastructure

- **api tests**: 288 Java test files
- **web tests**: 11 Java test files
- Test support module provides BaseContextSensitiveTest and related utilities

## 17. Build Baseline Status

**Build Command**: `JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto.x86_64 mvn -DskipTests clean install`
**Result**: SUCCESS
**Output**: WAR file produced in webapp/target/
