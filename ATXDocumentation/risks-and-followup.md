# OpenMRS Core - Risks and Follow-Up Waves

## Risk Assessment

### High Risk

#### 1. Patient extends Person (Inheritance Coupling)
- **Impact**: Patient IS-A Person via Java class inheritance. This is the tightest possible coupling.
- **Affected Components**: Patient.java, Person.java, all patient/person service implementations, HBM mappings
- **Mitigation**: Requires careful refactoring with comprehensive test coverage. Consider delegation/composition pattern. Must preserve Patient-as-Person semantics for backward compatibility.
- **Estimated Effort**: 2-3 sprints

#### 2. Context.java God-Class Service Locator Pattern
- **Impact**: Used in 189 files (29% of codebase). Every service implementation, handler, and utility accesses other services through `Context.getXxxService()`.
- **Affected Components**: All service implementations, HL7 handlers, validators, utilities
- **Mitigation**: Introduce dependency injection pattern gradually. Replace Context.getService() calls with constructor injection. Requires Spring configuration changes.
- **Estimated Effort**: 3-4 sprints

#### 3. Single hibernate.cfg.xml with 84 HBM Mappings
- **Impact**: All 84 entity mapping files registered in a single Hibernate SessionFactory. Prevents per-module session factory partitioning.
- **Affected Components**: hibernate.cfg.xml, all DAO implementations
- **Mitigation**: Partition HBM files by module, create module-specific session factory fragments. Consider JPA annotation migration to reduce HBM dependency.
- **Estimated Effort**: 2-3 sprints

### Medium Risk

#### 4. applicationContext-service.xml (693 lines)
- **Impact**: Single monolithic Spring XML configuration wiring all services, DAOs, AOP interceptors.
- **Affected Components**: All service beans, authorization interceptors, event listeners
- **Mitigation**: Split into per-module Spring configuration files. Use component scanning where possible.
- **Estimated Effort**: 1-2 sprints

#### 5. Cross-Domain Entity References in HBM Files
- **Impact**: Encounter.hbm.xml references Patient, Obs; Obs.hbm.xml references Concept, Encounter; etc.
- **Affected Components**: All HBM mapping files with foreign key relationships
- **Mitigation**: Use interface-based references or lazy loading strategies. May require custom Hibernate user types.
- **Estimated Effort**: 2 sprints

#### 6. PropertyEditor Classes (37)
- **Impact**: Scattered across the validator layer, each binding a domain entity. Cross-cut module boundaries.
- **Affected Components**: 37 PropertyEditor classes in org.openmrs.propertyeditor
- **Mitigation**: Group PropertyEditors by module during migration waves.
- **Estimated Effort**: 1 sprint

### Low Risk

#### 7. Test Infrastructure (288 test files + 11 web tests)
- **Impact**: Test files need parallel restructuring to match new module package structure.
- **Affected Components**: All test files in api/src/test and web/src/test
- **Mitigation**: Mirror module package structure in test directories. Can be done incrementally.
- **Estimated Effort**: 1-2 sprints (parallel to production code moves)

---

## Follow-Up Wave Plan

### Wave 2: Location Module Extraction
- Move Location, LocationTag, LocationAttribute, LocationAttributeType classes
- Move LocationService / LocationServiceImpl
- Move LocationDAO / HibernateLocationDAO
- Update imports across codebase
- Split Location-related entries from hibernate.cfg.xml
- **Estimated Effort**: 1 sprint

### Wave 3: Person Module Extraction
- Move Person, PersonName, PersonAddress, PersonAttribute, RelationshipType classes
- Move PersonService / PersonServiceImpl
- Handle Patient-extends-Person relationship (critical dependency)
- **Estimated Effort**: 1-2 sprints

### Wave 4: Concept Module Extraction
- Move 20+ Concept-related classes and Drug classes
- Move ConceptService / ConceptServiceImpl (largest self-contained service)
- **Estimated Effort**: 2 sprints (due to volume)

### Wave 5: Cross-Domain Module Extraction
- Move Patient, Encounter, Observation, Orders modules
- Create cross-domain adapters for inter-module references
- **Estimated Effort**: 3-4 sprints

### Wave 6: Users-Admin and Integration
- Move User, Role, Privilege and UserService
- Move HL7 and Notification subsystems
- Address HL7 handler cross-domain dependencies
- **Estimated Effort**: 2 sprints

### Wave 7: Split Spring Configuration
- Split applicationContext-service.xml per module
- Convert to component scanning where possible
- **Estimated Effort**: 1-2 sprints

### Wave 8: Split Hibernate Configuration
- Split hibernate.cfg.xml per module
- Consider JPA annotations migration from HBM XML
- **Estimated Effort**: 2-3 sprints

### Wave 9: Web-Adapter Extraction
- Refactor web layer to use WebModuleAdapter facade
- Refactor Context service locator pattern
- Final integration testing
- **Estimated Effort**: 2 sprints
