# Data Models — OpenMRS Core 2.0.3

## Entity-Relationship Overview

OpenMRS uses a patient-centric data model where `Person` is the foundational entity, `Patient` extends `Person`, and all clinical data (encounters, observations, orders) relates back to a patient.

## Core Entity Relationships

```
Person (1) ←——extends—— Patient (1)
  │                        │
  ├── PersonName (*)       ├── PatientIdentifier (*)
  ├── PersonAddress (*)    ├── Allergy (*)
  ├── PersonAttribute (*)  │
  └── Relationship (*)     │
                           │
Patient (1) ——→ Encounter (*) ——→ Obs (*)
                    │                │
                    ├── EncounterProvider (*)  ├── Concept (FK)
                    ├── EncounterType (FK)     ├── valueNumeric
                    ├── Location (FK)          ├── valueDatetime
                    ├── Visit (FK)             ├── valueCoded (Concept FK)
                    └── Form (FK)              └── valueText
                           │
Patient (1) ——→ Order (*) ←── DrugOrder / TestOrder
                    │
                    ├── Concept (FK, what is ordered)
                    ├── OrderType (FK)
                    ├── CareSetting (FK)
                    ├── Provider (FK, orderer)
                    └── Encounter (FK)

Patient (1) ——→ PatientProgram (*) ——→ PatientState (*)
                    │                       │
                    ├── Program (FK)        └── ProgramWorkflowState (FK)
                    └── Location (FK)

Patient (1) ——→ Visit (*) ——→ Encounter (*)
                    │
                    ├── VisitType (FK)
                    ├── Location (FK)
                    └── VisitAttribute (*)
```

## Domain Entity Details

### Person
| Field | Type | Description |
|-------|------|-------------|
| personId | Integer | Primary key |
| uuid | String | Universal unique identifier |
| gender | String | M/F/O |
| birthdate | Date | Date of birth |
| birthdateEstimated | Boolean | Whether birthdate is estimated |
| dead | Boolean | Deceased flag |
| deathDate | Date | Date of death |
| causeOfDeath | Concept | Cause of death concept |
| names | Set<PersonName> | Collection of names |
| addresses | Set<PersonAddress> | Collection of addresses |
| attributes | Set<PersonAttribute> | Extensible attributes |

### Patient (extends Person)
| Field | Type | Description |
|-------|------|-------------|
| patientId | Integer | Primary key (same as personId) |
| identifiers | Set<PatientIdentifier> | Patient identifiers (MRN, etc.) |
| allergies | List<Allergy> | Patient allergies |

### Encounter
| Field | Type | Description |
|-------|------|-------------|
| encounterId | Integer | Primary key |
| encounterType | EncounterType | Type of encounter |
| patient | Patient | Patient reference |
| location | Location | Facility location |
| form | Form | Clinical form used |
| encounterDatetime | Date | When encounter occurred |
| obs | Set<Obs> | Associated observations |
| encounterProviders | Set<EncounterProvider> | Providers involved |
| visit | Visit | Visit reference |

### Obs (Observation)
| Field | Type | Description |
|-------|------|-------------|
| obsId | Integer | Primary key |
| person | Person | Person reference |
| concept | Concept | What is being observed |
| encounter | Encounter | Encounter context |
| obsDatetime | Date | When observed |
| location | Location | Where observed |
| valueNumeric | Double | Numeric value |
| valueDatetime | Date | Date/time value |
| valueCoded | Concept | Coded value (concept reference) |
| valueText | String | Text value |
| valueDrug | Drug | Drug value |
| valueComplex | String | Complex obs data reference |
| obsGroup | Obs | Parent obs group |
| groupMembers | Set<Obs> | Child obs in group |

### Order
| Field | Type | Description |
|-------|------|-------------|
| orderId | Integer | Primary key |
| orderType | OrderType | Type of order |
| patient | Patient | Patient reference |
| concept | Concept | What is ordered |
| encounter | Encounter | Encounter context |
| orderer | Provider | Ordering provider |
| urgency | Urgency | ROUTINE, STAT, ON_SCHEDULED_DATE |
| action | Action | NEW, REVISE, DISCONTINUE, RENEW |
| careSetting | CareSetting | INPATIENT, OUTPATIENT |
| dateActivated | Date | When order becomes active |
| dateStopped | Date | When order was stopped |
| autoExpireDate | Date | When order automatically expires |
| orderNumber | String | System-generated order number |
| previousOrder | Order | Reference to previous order (for revisions) |
| orderGroup | OrderGroup | Order group reference |

### DrugOrder (extends Order)
| Field | Type | Description |
|-------|------|-------------|
| dose | Double | Dose amount |
| doseUnits | Concept | Units of dose |
| frequency | OrderFrequency | Dosing frequency |
| asNeeded | Boolean | PRN flag |
| quantity | Double | Quantity to dispense |
| quantityUnits | Concept | Units of quantity |
| numRefills | Integer | Number of refills |
| duration | Integer | Duration of treatment |
| durationUnits | Concept | Units of duration |
| route | Concept | Route of administration |
| drug | Drug | Specific drug |
| dosingType | Class<? extends DosingInstructions> | Dosing instruction strategy |

### TestOrder (extends Order)
| Field | Type | Description |
|-------|------|-------------|
| laterality | Laterality | LEFT, RIGHT, BILATERAL |
| clinicalHistory | String | Relevant clinical history |
| specimenSource | Concept | Source of specimen |
| numberOfRepeats | Integer | Number of times to repeat |

### Concept
| Field | Type | Description |
|-------|------|-------------|
| conceptId | Integer | Primary key |
| datatype | ConceptDatatype | Data type (numeric, coded, text, etc.) |
| conceptClass | ConceptClass | Classification (diagnosis, test, drug, etc.) |
| names | Collection<ConceptName> | Names in multiple locales |
| descriptions | Collection<ConceptDescription> | Descriptions |
| answers | Collection<ConceptAnswer> | Valid coded answers |
| conceptSets | Collection<ConceptSet> | Concept set members |
| conceptMappings | Collection<ConceptMap> | External terminology mappings |
| set | Boolean | Whether this is a concept set |
| retired | Boolean | Retirement status |

### Location
| Field | Type | Description |
|-------|------|-------------|
| locationId | Integer | Primary key |
| name | String | Location name |
| description | String | Description |
| address1-address6 | String | Address components |
| parentLocation | Location | Hierarchical parent |
| childLocations | Set<Location> | Child locations |
| tags | Set<LocationTag> | Location tags |
| attributes | Set<LocationAttribute> | Custom attributes |

### Visit
| Field | Type | Description |
|-------|------|-------------|
| visitId | Integer | Primary key |
| patient | Patient | Patient reference |
| visitType | VisitType | Type of visit |
| indication | Concept | Visit indication |
| location | Location | Visit location |
| startDatetime | Date | Visit start |
| stopDatetime | Date | Visit end |
| encounters | Set<Encounter> | Encounters during visit |
| attributes | Set<VisitAttribute> | Custom attributes |

### User
| Field | Type | Description |
|-------|------|-------------|
| userId | Integer | Primary key |
| person | Person | Associated person |
| username | String | Login username |
| systemId | String | System identifier |
| roles | Set<Role> | Assigned roles |
| proficientLocales | Set<Locale> | Proficient locales |
| userProperties | Map<String,String> | User preferences |

## Hibernate Mapping Statistics

- **Total HBM XML files**: 84
- **Core entity mappings**: ~60 (Person, Patient, Encounter, Obs, Order, Concept, etc.)
- **HL7 mappings**: 4 (HL7Source, HL7InQueue, HL7InArchive, HL7InError)
- **Notification mappings**: 3 (Template, Alert, AlertRecipient)
- **Scheduler mappings**: 1 (TaskDefinition)
- **All mappings use Hibernate XML (.hbm.xml) — no JPA annotations**

## Related Documentation

- [Program Structure](program-structure.md)
- [Interfaces](interfaces.md)
- [Components](../architecture/components.md)
- [Dependency Analysis](../analysis/dependency-analysis.md)
