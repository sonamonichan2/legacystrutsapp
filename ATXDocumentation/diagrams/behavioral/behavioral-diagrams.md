# Behavioral Diagrams — OpenMRS Core 2.0.3

## Patient Registration Sequence

```mermaid
sequenceDiagram
    participant C as Client
    participant Auth as AuthorizationAdvice
    participant Log as LoggingAdvice
    participant RD as RequiredDataAdvice
    participant PS as PatientServiceImpl
    participant PD as PatientDAO
    participant H as Hibernate/DB

    C->>Auth: savePatient(patient)
    Auth->>Auth: Check @Authorized(ADD_PATIENTS)
    Auth->>Log: proceed
    Log->>Log: Log method entry
    Log->>RD: proceed
    RD->>RD: Set creator, dateCreated
    RD->>PS: savePatient(patient)
    PS->>PS: checkPatientIdentifiers(patient)
    PS->>PD: savePatient(patient)
    PD->>H: session.saveOrUpdate(patient)
    H-->>PD: saved entity
    PD-->>PS: Patient
    PS-->>RD: Patient
    RD-->>Log: Patient
    Log->>Log: Log method exit + timing
    Log-->>Auth: Patient
    Auth-->>C: Patient
```

## Order Placement and Revision Sequence

```mermaid
sequenceDiagram
    participant C as Client
    participant OS as OrderServiceImpl
    participant OV as OrderValidator
    participant OD as OrderDAO
    participant DB as Database

    C->>OS: saveOrder(newOrder, context)
    OS->>OV: validate(newOrder)
    OV->>OV: Check patient, concept, orderer
    OV-->>OS: validation passed

    alt action == NEW
        OS->>OS: generateOrderNumber()
        OS->>OS: setDateActivated(now)
        OS->>OS: calculateAutoExpireDate() [if DrugOrder]
        OS->>OD: saveOrder(newOrder)
        OD->>DB: INSERT order
    else action == REVISE
        OS->>OS: Validate previousOrder is active
        OS->>OS: Set dateStopped on previousOrder
        OS->>OD: saveOrder(previousOrder) [update]
        OS->>OD: saveOrder(newOrder) [insert]
        OD->>DB: UPDATE + INSERT
    else action == DISCONTINUE
        OS->>OS: Validate order is active
        OS->>OS: Create DC order
        OS->>OD: saveOrder(dcOrder)
        OD->>DB: INSERT dc_order, UPDATE original
    end

    DB-->>OD: saved
    OD-->>OS: Order
    OS-->>C: Order
```

## HL7 Message Processing Activity

```
┌─────────────────┐
│ HL7 Message      │
│ Received         │
└────────┬────────┘
         ▼
┌─────────────────┐
│ Store in         │
│ HL7InQueue       │
└────────┬────────┘
         ▼
┌─────────────────┐
│ ProcessHL7Task   │
│ (Scheduled)      │
└────────┬────────┘
         ▼
┌─────────────────┐
│ Parse HL7        │
│ (HAPI Library)   │
└────────┬────────┘
         ▼
┌─────────────────┐       ┌──────────────┐
│ Determine        │──────→│ ADT^A28      │
│ Message Type     │       │ Handler      │
└────────┬────────┘       │ → Create     │
         │                │   Patient     │
         │                └──────────────┘
         │
         ├───────────────→┌──────────────┐
         │                │ ORU^R01      │
         │                │ Handler      │
         │                │ → Create     │
         │                │   Encounter  │
         │                │   + Obs      │
         │                └──────────────┘
         ▼
    ┌──────────┐     ┌──────────────┐
    │ Success? │──No→│ Move to      │
    └────┬─────┘     │ HL7InError   │
         │Yes        └──────────────┘
         ▼
    ┌──────────────┐
    │ Move to      │
    │ HL7InArchive │
    └──────────────┘
```

## Program Workflow State Machine

```
┌──────────────┐
│  Enrolled    │ ← PatientProgram.save()
│  (initial)   │
└──────┬───────┘
       │ transitionToState()
       ▼
┌──────────────┐
│  State A     │ ← ProgramWorkflowState
│  (active)    │
└──────┬───────┘
       │ transitionToState()
       ▼
┌──────────────┐
│  State B     │ ← New ProgramWorkflowState
│  (active)    │
└──────┬───────┘
       │ transitionToState() or complete program
       ▼
┌──────────────┐
│  Terminal    │ ← isTerminal=true
│  State       │
└──────┬───────┘
       │ completeProgram()
       ▼
┌──────────────┐
│  Completed   │ ← dateCompleted set
└──────────────┘

Rules:
- States must belong to the program's workflow
- Only one active state per workflow at a time
- Terminal states automatically trigger program completion
- ConceptStateConversion can auto-transition based on concept
```

## Application Startup Flow

```
┌─────────────────────┐
│ Servlet Container    │
│ starts webapp        │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Listener.context     │
│ Initialized()        │
└──────────┬──────────┘
           ▼
┌─────────────────────┐     ┌──────────────────┐
│ Runtime properties   │──No→│ StartupFilter →  │
│ exist?               │     │ InitializationF. │
└──────────┬──────────┘     │ (Setup Wizard)   │
           │Yes              └──────────────────┘
           ▼
┌─────────────────────┐     ┌──────────────────┐
│ Database needs       │──Yes│ UpdateFilter     │
│ update?              │────→│ (Liquibase Run)  │
└──────────┬──────────┘     └──────────────────┘
           │No
           ▼
┌─────────────────────┐
│ Create Spring        │
│ ApplicationContext   │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Start Modules        │
│ (ModuleFactory)      │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Start Scheduler      │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Application Ready    │
└─────────────────────┘
```

## Related Documentation

- [Structural Diagrams](../structural/structural-diagrams.md)
- [Architecture Diagrams](../architecture/architecture-diagrams.md)
- [Workflows](../../behavior/workflows.md)
