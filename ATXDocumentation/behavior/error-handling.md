> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Error Handling — OpenMRS Core 2.0.3

## Exception Hierarchy

```
RuntimeException
└── APIException (base for all OpenMRS API exceptions)
    ├── APIAuthenticationException — unauthorized access
    ├── ValidationException — validation failures
    ├── PatientIdentifierException — identifier issues
    │   ├── BlankIdentifierException
    │   ├── DuplicateIdentifierException
    │   ├── IdentifierNotUniqueException
    │   ├── InsufficientIdentifiersException
    │   ├── InvalidCheckDigitException
    │   ├── InvalidIdentifierFormatException
    │   └── MissingRequiredIdentifierException
    ├── PasswordException — password policy violations
    │   ├── ShortPasswordException
    │   ├── WeakPasswordException
    │   └── InvalidCharactersPasswordException
    ├── ConceptsLockedException — concept dictionary locked
    ├── ConceptInUseException — concept has references
    ├── ConceptNameInUseException — concept name conflict
    ├── ConceptStopWordException — stop word issues
    ├── DuplicateConceptNameException — duplicate names
    ├── AmbiguousOrderException — conflicting active orders
    ├── EncounterTypeLockedException — encounter types locked
    ├── FormsLockedException — forms locked
    ├── PatientIdentifierTypeLockedException
    ├── PersonAttributeTypeLockedException
    ├── ProgramNameDuplicatedException
    ├── CannotDeleteRoleWithChildrenException
    └── InvalidFileTypeException

ContextAuthenticationException — authentication failure (login)

Exception
└── DAOException — data access layer errors
└── ModuleException — module system errors
    ├── MandatoryModuleException
    ├── ModuleMustStartException
    └── OpenmrsCoreModuleException
└── SchedulerException — task scheduling errors
└── SerializationException — serialization/deserialization errors
└── MessageException — notification errors
└── CustomDatatypeException — custom datatype errors
    ├── InvalidCustomValueException
    └── NotYetPersistedException
```

## Error Handling Patterns

### Service Layer Error Handling

1. **Validation Before Persistence**: Services validate inputs before DAO calls
   ```
   savePatient(Patient) {
       validate(patient);  // throws ValidationException
       checkPrivileges();  // throws APIAuthenticationException
       dao.savePatient(patient);  // throws DAOException
   }
   ```

2. **Exception Wrapping**: DAO exceptions are wrapped in API-level exceptions
   - `DAOException` → caught by service → re-thrown as `APIException`
   - Hibernate exceptions → caught by DAO → wrapped in `DAOException`

3. **Soft Delete Pattern**: Most entities use void/retire instead of hard delete
   - `voidPatient(patient, "reason")` — sets voided=true, voidReason
   - `retireLocation(location, "reason")` — sets retired=true, retireReason
   - `purge*()` methods perform actual database delete (requires higher privilege)

### AOP-Level Error Handling

- **AuthorizationAdvice**: Throws `APIAuthenticationException` with privilege details
- **LoggingAdvice**: Catches and logs exceptions before re-throwing
- **RequiredDataAdvice**: May throw `APIException` if required audit fields cannot be set

### Web Layer Error Handling

1. **StartupFilter**: Catches all startup errors
   - If application fails to start → redirects to `StartupErrorFilter`
   - `StartupErrorFilter` displays error page with stack trace
   - No application routes are accessible until startup completes

2. **InitializationFilter**: Handles setup wizard errors
   - Database connection failures → shows retry page
   - Migration errors → shows error with rollback option

3. **UpdateFilter**: Handles schema update errors
   - Liquibase migration failures → shows error page
   - Option to skip failed changesets (risky)

### Module System Error Handling

- `ModuleException` — base for module errors
- `MandatoryModuleException` — required module cannot start
- `ModuleMustStartException` — module marked as must-start failed
- `OpenmrsCoreModuleException` — core module dependency issue
- Module errors are logged but don't crash the application (except mandatory modules)

### HL7 Error Handling

- Messages that fail processing are moved to `HL7InError` table
- Error details include: error message, HL7 message content, stack trace
- Failed messages can be requeued for reprocessing
- `ProposingConceptException` — when HL7 references unmapped concept

## Error Recovery Patterns

| Scenario | Recovery Strategy |
|----------|------------------|
| Authentication failure | Return to login, increment attempt counter |
| Validation failure | Return validation errors to client, no state change |
| Database connection loss | Retry with C3P0 connection pool recovery |
| Liquibase migration failure | Display error page, manual intervention required |
| Module start failure | Log error, continue startup (unless mandatory) |
| HL7 processing failure | Move to error queue, allow manual reprocessing |
| Patient merge failure | Transaction rollback, no partial merge state |
| Order save failure | Transaction rollback, return error to caller |
| Scheduler task failure | Log error, task continues on next interval |

## Global Error Configuration

- `ExceptionUtil` — utility for extracting root causes and formatting errors
- `VelocityExceptionHandler` — custom handler for Velocity template errors
- `DatabaseUpdateException` — wraps Liquibase migration errors with context
- `InputRequiredException` — used during initialization when user input is needed
- `CycleException` — detected when cyclic dependencies exist

## Related Documentation

- [Business Logic](business-logic.md)
- [Workflows](workflows.md)
- [Decision Logic](decision-logic.md)
- [Security Patterns](../analysis/security-patterns.md)
