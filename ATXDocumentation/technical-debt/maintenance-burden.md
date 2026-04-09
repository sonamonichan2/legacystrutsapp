# Maintenance Burden — OpenMRS Core 2.0.3

## Service Locator Anti-Pattern (Context.java)

**Impact**: High | **Affected Area**: Entire codebase

The `Context` class contains ~24 static `getService()` methods and is called ~308 times across service implementations alone. This:
- Hides dependencies, making it impossible to trace module boundaries
- Prevents proper unit testing without full Spring context
- Creates circular service dependencies that block modularization
- Makes constructor injection impossible for services

**Key Metrics**:
- EncounterServiceImpl: 48 Context.get* calls
- PatientServiceImpl: 48 Context.get* calls
- ConceptServiceImpl: 44 Context.get* calls
- PersonServiceImpl: 31 Context.get* calls

## XML Configuration Complexity

**Impact**: Medium | **Affected Area**: Configuration layer

### applicationContext-service.xml (689 lines)
- All service beans wired explicitly in XML
- AOP proxy configuration in XML
- Transaction management configured in XML
- Error-prone, difficult to refactor
- No IDE assistance for bean reference validation

### hibernate.cfg.xml (84 entity mappings)
- 84 `.hbm.xml` mapping files referenced
- All entity-to-table mappings, associations, and cascades in XML
- Duplicates information that could be expressed via JPA annotations
- Must maintain XML files alongside entity classes

## Flat Package Structure

**Impact**: Medium | **Affected Area**: Domain model, services, DAOs

All ~105 domain entities live in `org.openmrs` root package. Services in `org.openmrs.api`, implementations in `org.openmrs.api.impl`, DAOs in `org.openmrs.api.db.hibernate`. There are no package-level boundaries between domains (patient, encounter, order, concept, etc.).

**Consequences**:
- Cannot enforce access rules between domains
- All entities are visible to all services
- No clear module boundaries for future modularization
- Package bloat in root `org.openmrs` package

## Cyclic Service Dependencies

**Impact**: High | **Affected Area**: Service layer

Five identified cyclic dependencies between services:
1. PatientService ↔ PersonService
2. PatientService ↔ EncounterService
3. EncounterService ↔ ObsService
4. EncounterService ↔ OrderService
5. EncounterService ↔ VisitService

These cycles prevent clean module extraction and require domain events or interface segregation to break.

## Legacy API Patterns

**Impact**: Medium | **Affected Area**: API design

- **Deprecated methods**: Many service interfaces retain deprecated methods for backward compatibility with modules
- **Vector usage**: Domain entities use `java.util.Vector` instead of `ArrayList` (e.g., Patient, Person)
- **Raw types**: Some collections lack generics
- **Static utility methods**: Extensive use of static methods in `OpenmrsUtil`, `Context`, etc.

## Testing Infrastructure Debt

**Impact**: Low | **Affected Area**: Test suite

- **PowerMock dependency**: PowerMock 1.5 is incompatible with modern JDK versions, blocks Java upgrade
- **JUnit 4 only**: No JUnit 5 support; test migration needed
- **Mockito 1.9.5**: Very old API, missing modern features (BDD, strict stubs)
- **BaseContextSensitiveTest**: Heavy integration-test base class requiring full Spring context
- ~288 API test files, ~11 web test files — significant migration effort

## Build Tool Debt

**Impact**: Low | **Affected Area**: Build infrastructure

- **Maven Compiler Plugin 2.1**: Doesn't support Java 9+ module system properly
- **Maven Surefire 2.18.1**: Missing support for JUnit 5
- **Maven WAR Plugin 2.4**: Outdated
- **Assembly Plugin 2.2-beta-5**: Still using beta version
- **Eclipse Plugin 2.8**: Deprecated

## Custom Module System Complexity

**Impact**: Medium | **Affected Area**: Extension infrastructure

The custom module system (`ModuleFactory`, `ModuleClassLoader`) is a significant complexity contributor:
- Custom classloading introduces hard-to-debug class visibility issues
- Module lifecycle management is tightly coupled to `ServiceContext`
- Module Spring contexts are manually registered/unregistered
- Any changes to the module API can break the ~200+ community modules

## Related Documentation

- [Summary](summary.md)
- [Outdated Components](outdated-components.md)
- [Remediation Plan](remediation-plan.md)
- [Dependency Analysis](../analysis/dependency-analysis.md)
