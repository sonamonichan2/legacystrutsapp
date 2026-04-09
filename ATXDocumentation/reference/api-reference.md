# API Reference — OpenMRS Core 2.0.3

## Service API Summary

All services are accessed via `Context.getXxxService()` and proxied by AOP for authorization, logging, and audit field management.

### PatientService (51 methods, 843 lines)
**CRUD**: `savePatient`, `getPatient`, `getPatientByUuid`, `voidPatient`, `unvoidPatient`, `purgePatient`
**Search**: `getPatients(query)`, `getPatients(query, start, length)`, `getAllPatients`, `getPatientByExample`, `getDuplicatePatientsByAttributes`
**Identifiers**: `getPatientIdentifiers`, `savePatientIdentifierType`, `getPatientIdentifierType`, `retirePatientIdentifierType`, `checkPatientIdentifiers`
**Merge**: `mergePatients(preferred, notPreferred)`, `mergePatients(preferred, list)`
**Allergies**: `getAllergies(patient)`, `setAllergies(patient, allergies)`

### PersonService (65 methods, 844 lines)
**CRUD**: `savePerson`, `getPerson`, `getPersonByUuid`, `voidPerson`, `unvoidPerson`, `purgePerson`
**Names**: `savePersonName`, `getPersonName`
**Addresses**: `savePersonAddress`, `getPersonAddress`
**Attributes**: `savePersonAttributeType`, `getPersonAttributeType`, `retirePersonAttributeType`
**Relationships**: `saveRelationship`, `getRelationship`, `getRelationships`, `saveRelationshipType`
**Search**: `getSimilarPeople`, `getPeople`

### EncounterService (51 methods, 727 lines)
**CRUD**: `saveEncounter`, `getEncounter`, `getEncounterByUuid`, `voidEncounter`, `unvoidEncounter`, `purgeEncounter`
**Search**: `getEncounters(patient, location, fromDate, toDate, ...)`, `getEncountersByVisit`
**Types**: `saveEncounterType`, `getEncounterType`, `retireEncounterType`
**Roles**: `saveEncounterRole`, `getEncounterRole`
**Transfer**: `transferEncounter(encounter, patient)`

### ObsService (25 methods, 481 lines)
**CRUD**: `saveObs(obs, changeMessage)`, `getObs`, `getObsByUuid`, `voidObs`, `unvoidObs`, `purgeObs`
**Search**: `getObservations(persons, encounters, concepts, ...)`, `getObservationsByPersonAndConcept`
**Complex**: `getComplexObs(obsId, view)`

### OrderService (55 methods, 788 lines)
**CRUD**: `saveOrder(order, context)`, `getOrder`, `getOrderByUuid`, `getOrderByOrderNumber`, `voidOrder`, `unvoidOrder`, `purgeOrder`
**Lifecycle**: `discontinueOrder(order, reason, date, provider, encounter)`, `reviseOrder(order)`
**Query**: `getActiveOrders(patient, type, careSetting, date)`, `getOrders(patient, careSetting, type, includeVoided)`
**Types**: `saveOrderType`, `getOrderType`, `retireOrderType`
**Frequency**: `saveOrderFrequency`, `getOrderFrequencies`
**CareSetting**: `getCareSetting`, `getCareSettings`

### ConceptService (140 methods, 1865 lines)
**Concept CRUD**: `saveConcept`, `getConcept`, `getConceptByUuid`, `getConceptByName`, `getConceptByMapping`, `purgeConcept`, `retireConcept`
**Search**: `getConceptsByName`, `getConcepts(query)`, `getAllConcepts`
**Drug**: `saveDrug`, `getDrug`, `getDrugs`, `getDrugByMapping`
**Source**: `saveConceptSource`, `getConceptSource`, `getConceptSources`
**Reference Terms**: `saveConceptReferenceTerm`, `getConceptReferenceTerm`
**Map Types**: `saveConceptMapType`, `getConceptMapType`, `getConceptMapTypes`
**Proposals**: `saveConceptProposal`, `getConceptProposal`
**Stop Words**: `saveConceptStopWord`, `getConceptStopWords`

### LocationService (32 methods, 502 lines)
**CRUD**: `saveLocation`, `getLocation`, `getLocationByUuid`, `getLocationByName`, `retireLocation`, `unretireLocation`, `purgeLocation`
**Query**: `getAllLocations`, `getLocations(query)`, `getRootLocations`
**Tags**: `saveLocationTag`, `getLocationTag`, `getLocationTags`
**Attributes**: `saveLocationAttributeType`, `getLocationAttributeType`

### UserService (43 methods, 555 lines)
**CRUD**: `saveUser(user, password)`, `getUser`, `getUserByUuid`, `getUserByUsername`, `retireUser`, `unretireUser`, `purgeUser`
**Password**: `changePassword(user, oldPw, newPw)`, `changePassword(newPw)`
**Search**: `getUsers(name, roles, includeRetired)`, `getUsersByName`
**Roles**: `saveRole`, `getRole`, `getAllRoles`, `purgeRole`
**Privileges**: `savePrivilege`, `getPrivilege`, `getAllPrivileges`, `purgePrivilege`

### VisitService (15 methods, 382 lines)
**CRUD**: `saveVisit`, `getVisit`, `getVisitByUuid`, `voidVisit`, `unvoidVisit`, `purgeVisit`
**Query**: `getVisits(...)`, `getActiveVisitsByPatient`
**Types**: `saveVisitType`, `getVisitType`, `getVisitTypes`

### ProviderService (28 methods, 324 lines)
**CRUD**: `saveProvider`, `getProvider`, `getProviderByUuid`, `retireProvider`, `unretireProvider`, `purgeProvider`
**Search**: `getProviders(query, start, length, attributes)`
**Attributes**: `saveProviderAttributeType`, `getProviderAttributeType`

### AdministrationService (~80 methods)
**Properties**: `getGlobalProperty(key)`, `saveGlobalProperty`, `getAllGlobalProperties`, `getGlobalPropertiesByPrefix`
**SQL**: `executeSQL(sql, selectOnly)` — direct SQL execution
**System**: `getSystemVariables`, `getImplementationId`, `setImplementationId`
**Locale**: `getAllowedLocales`, `getPresentationLocales`

### Additional Services
- **FormService**: Form/Field CRUD and management
- **CohortService**: Cohort CRUD, member management
- **ProgramWorkflowService**: Program enrollment, state transitions
- **SerializationService**: Object serialization
- **DatatypeService**: Custom datatype management
- **OrderSetService**: Order set management
- **HL7Service**: HL7 message management
- **SchedulerService**: Task scheduling
- **AlertService**: User alert management

## Common Method Patterns

All services follow these conventions:
- `save*(entity)` — Create or update (returns saved entity)
- `get*(id)` — Get by primary key
- `get*ByUuid(uuid)` — Get by UUID
- `getAll*()` / `getAll*(includeRetired)` — List all
- `void*(entity, reason)` — Soft-delete data entities
- `retire*(entity, reason)` — Soft-delete metadata entities
- `unvoid*(entity)` / `unretire*(entity)` — Restore
- `purge*(entity)` — Permanent delete (elevated privilege)

## Related Documentation

- [Interfaces](interfaces.md)
- [Data Models](data-models.md)
- [Program Structure](program-structure.md)
- [Components](../architecture/components.md)
