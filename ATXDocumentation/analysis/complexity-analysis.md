# Complexity Analysis — OpenMRS Core 2.0.3

## Complexity Hotspots

### Very High Complexity

#### 1. ConceptServiceImpl (1,891 lines)
- **~140 public methods** — largest service interface in the system
- Manages concepts, names, descriptions, answers, sets, mappings, sources, reference terms, map types, drugs, drug ingredients, drug reference maps, concept proposals, stop words
- Heavy validation logic for concept name uniqueness per locale
- Complex concept search with Lucene integration
- Multiple deprecated methods maintained for backward compatibility

#### 2. HibernateConceptDAO (1,984 lines)
- Largest DAO — mirror of ConceptService complexity
- Complex Hibernate Criteria queries for concept search
- Lucene/Hibernate Search integration for full-text search
- Multiple database-specific optimizations

#### 3. PatientServiceImpl (1,590 lines)
- **Patient Merging** (~780 lines for merge logic): Most complex single operation
  - Must move encounters, observations, orders, programs, relationships, identifiers
  - Must handle duplicate detection and resolution
  - Creates audit log (PersonMergeLog) for tracking
  - Serializes merge data for potential undo
- Identifier validation with check-digit algorithms
- Search with multiple strategies (name, identifier, attribute)

#### 4. ModuleFactory (1,687 lines)
- Custom module lifecycle management
- Dependency resolution between modules
- Version compatibility checking
- Dynamic Spring context registration/unregistration
- Error handling for missing/incompatible modules

#### 5. ModuleClassLoader (1,103 lines)
- Custom classloader per module
- Parent-first delegation with module-specific overrides
- Resource loading from `.omod` files
- Class visibility rules between modules

### High Complexity

#### 6. OrderServiceImpl (1,013 lines)
- Complex order state machine (NEW → ACTIVE → REVISE/DISCONTINUE/EXPIRE)
- Drug order auto-expiry calculation
- Order number generation
- Previous order linking for revisions/discontinuations
- Care setting and order type validation

#### 7. EncounterServiceImpl (912 lines)
- Cascading save of observations and orders
- Provider-role assignment
- Encounter transfer between patients
- Visit association management
- Heavy use of Context.getService() (48 calls)

#### 8. PersonServiceImpl (939 lines)
- Similar-person search (fuzzy matching)
- Person merge support for PatientServiceImpl
- Attribute type locking mechanism
- Relationship management with bidirectional types

### Complexity Contributors

| Factor | Impact | Description |
|--------|--------|-------------|
| Service Locator Pattern | Very High | 308 Context.get*() calls hide dependencies |
| AOP Proxy Chain | High | 3 advice layers on every service method call |
| Hibernate XML Mappings | High | 84 mapping files to maintain alongside entities |
| Spring XML Config | High | 689-line XML configuration |
| Module System | High | Custom classloading and lifecycle management |
| Deprecated API Maintenance | Medium | Many deprecated methods kept for module compatibility |
| Deep Inheritance Hierarchies | Medium | 4-5 levels deep in entity hierarchy |
| Cross-Cutting Validation | Medium | 56 validators with overlapping responsibilities |

## Cognitive Complexity Assessment

| Area | Rating | Justification |
|------|--------|---------------|
| Patient Merge | Very High | Multi-entity cascade, audit trail, rollback |
| Order Lifecycle | Very High | State machine, drug/test specialization, revision chain |
| Concept Dictionary | High | 140+ methods, locale-aware naming, mapping complexity |
| Module System | High | Custom classloading, dependency resolution |
| HL7 Processing | Medium | Message parsing, concept mapping, encounter creation |
| Startup Flow | Medium | Multi-filter chain, database detection, migration |
| Authentication | Low | Straightforward privilege-based checks |

## Related Documentation

- [Code Metrics](code-metrics.md)
- [Security Patterns](security-patterns.md)
- [Components](../architecture/components.md)
