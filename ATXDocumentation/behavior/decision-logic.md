> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Decision Logic — OpenMRS Core 2.0.3

## PatientValidator Decision Points

- **Patient must have at least one non-voided name**: Rejects if all names are voided
- **Patient must have at least one non-voided identifier**: Rejects if no active identifiers
- **Birthdate must not be in the future**: Validates birthdate <= current date
- **Death date must not be in the future**: Validates deathDate <= current date
- **If dead, causeOfDeath must be set**: When `dead=true`, requires causeOfDeath concept
- **Delegates to PersonValidator**: Also validates person-level fields

## PatientIdentifierValidator Decision Points

- **Identifier must not be blank**: Rejects empty/null identifiers
- **Identifier type must be set**: Rejects identifiers without a type
- **Format validation**: If identifier type has format regex, identifier must match
- **Check digit validation**: If identifier type requires check digit, validates using Luhn algorithm or custom validator
- **Uniqueness**: If identifier type has `uniquenessBehavior`, checks for duplicates (location-scoped or global)

## OrderValidator Decision Points

- **Patient is required**: Order must reference a valid patient
- **Concept is required**: Order must specify what is being ordered
- **Encounter is required**: Order must be associated with an encounter
- **Order type must match concept class**: Validates concept class is allowed for the order type
- **Active order check**: Cannot create NEW order if an active order for the same concept exists in same care setting (throws `AmbiguousOrderException`)
- **Previous order validation (REVISE/DISCONTINUE)**: Previous order must exist and be active
- **Date validation**: dateActivated must not be after autoExpireDate or dateStopped

## DrugOrderValidator Decision Points

- **Dose is required if SimpleDosingInstructions**: Must have dose > 0
- **Dose units required with dose**: If dose is set, doseUnits concept must be set
- **Route is required for SimpleDosingInstructions**: Route concept must be set
- **Frequency is required for SimpleDosingInstructions**: OrderFrequency must be set
- **Duration units required with duration**: If duration set, durationUnits required
- **Quantity units required with quantity**: If quantity set, quantityUnits required
- **Drug must belong to order concept**: If drug and concept both set, drug.concept must match order concept
- **Number of refills must be non-negative**: numRefills >= 0

## EncounterValidator Decision Points

- **Patient is required**: Encounter must reference a patient
- **Encounter type is required**: Must specify type of encounter
- **Encounter datetime must not be in future**: encounterDatetime <= now
- **Encounter datetime must not be before patient birthdate**: encounterDatetime >= patient.birthdate
- **At least one provider should be set**: Warning if no providers assigned

## ConceptValidator Decision Points

- **Concept must have at least one fully specified name per locale**: Each locale must have a fully specified name
- **No duplicate fully specified names within a locale**: Rejects duplicate names
- **No duplicate preferred names within a locale**: Only one preferred name per locale
- **Concept class is required**: Must be classified
- **Concept datatype is required**: Must have a datatype
- **If set concept, must have at least two members**: Concept sets need members
- **Short name cannot be same as fully specified name**: Must be distinct

## ObsValidator Decision Points

- **Person is required**: Obs must reference a person
- **Concept is required**: Must specify what was observed
- **Obs datetime is required**: Must have observation timestamp
- **Value must match concept datatype**: 
  - Numeric concept → valueNumeric must be set
  - Coded concept → valueCoded must be set
  - Text concept → valueText must be set
  - Date/datetime → valueDatetime must be set
- **Numeric range validation**: If concept is ConceptNumeric, value must be within absolute low/high bounds
- **Obs group validation**: Obs in a group cannot also be a top-level obs

## UserValidator Decision Points

- **Username requirements**: Must be alphanumeric, 2-50 characters
- **System ID must be unique**: No duplicate system IDs
- **Person is required**: User must be associated with a person
- **Email format validation**: If email set, must be valid format

## Password Policy Decision Points (UserServiceImpl)

- **Minimum length**: Configured via `security.passwordMinimumLength` global property (default: 8)
- **Must contain uppercase**: At least one uppercase letter (configurable)
- **Must contain lowercase**: At least one lowercase letter
- **Must contain digit**: At least one digit (configurable)
- **Cannot match username**: Password cannot be the same as username
- **Cannot reuse recent passwords**: Configured via global property

## Authorization Decision Logic (AuthorizationAdvice)

```
For each service method invocation:
    1. Read @Authorized annotation from method or interface
    2. If no @Authorized annotation → allow (no restriction)
    3. If @Authorized with requireAll=false → user must have ANY listed privilege
    4. If @Authorized with requireAll=true → user must have ALL listed privileges
    5. Check user's direct privileges + inherited role privileges
    6. If superuser → always allow
    7. If anonymous user and privileges required → throw APIAuthenticationException
```

## Visit Validation Decision Points

- **Patient is required**: Visit must reference a patient
- **Visit type is required**: Must specify visit type
- **Start datetime is required**: Must have start time
- **Stop datetime must be after start**: If both set, stop > start
- **No overlapping visits**: Patient cannot have overlapping active visits

## Program Enrollment Decision Points

- **Patient is required**: Must reference a patient
- **Program is required**: Must specify program
- **Date enrolled is required**: Must have enrollment date
- **Date completed must be after enrolled**: If completed, must be after enrollment
- **No duplicate active enrollments**: Patient cannot be enrolled in same program twice simultaneously
- **State transitions must be valid**: State must belong to the program's workflow

## Related Documentation

- [Business Logic](business-logic.md)
- [Workflows](workflows.md)
- [Error Handling](error-handling.md)
- [Interfaces](../reference/interfaces.md)
