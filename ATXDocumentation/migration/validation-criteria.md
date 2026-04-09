# Validation Criteria — OpenMRS Core 2.0.3 Migration

## Wave-Level Success Criteria

### Wave 1: Foundation

| Criteria | Validation Method |
|----------|------------------|
| shared-kernel, location, person are separate Maven modules | `mvn verify` succeeds |
| No circular dependencies between extracted modules | ArchUnit rules pass |
| All existing unit tests pass with updated imports | Test suite green |
| WAR deploys successfully on Tomcat 7+ | Deployment smoke test |
| Database schema unchanged | Liquibase checksum verification |
| Patient data accessible through existing workflows | Manual smoke test |
| `Context.getPersonService()` still works | Backward compat test |

### Wave 2: Independent Domains

| Criteria | Validation Method |
|----------|------------------|
| users-admin, concepts are separate Maven modules | `mvn verify` succeeds |
| Authentication/authorization still functional | Login + privilege test |
| Concept dictionary CRUD operations work | Service-level tests |
| Role/privilege inheritance preserved | Authorization tests |
| All ~288 API tests pass | Test suite green |

### Wave 3: Core Clinical

| Criteria | Validation Method |
|----------|------------------|
| patient, encounter are separate Maven modules | `mvn verify` succeeds |
| Patient merging works correctly | Merge integration test |
| Encounter save with cascaded obs/orders works | Cascade test |
| Cross-module interfaces defined in shared-kernel | Interface existence check |
| Domain events for obs/order save implemented | Event test |

### Wave 4: Clinical Data + Spring Boot

| Criteria | Validation Method |
|----------|------------------|
| observation, orders are separate Maven modules | `mvn verify` succeeds |
| Order lifecycle (NEW→REVISE→DISCONTINUE) works | Order lifecycle test |
| Complex obs handling functional | Complex obs test |
| Spring Boot application context loads | Boot startup test |
| All services injectable via `@Autowired` | DI verification |
| WAR still deploys on external Tomcat | WAR deployment test |
| Embedded server starts successfully | `java -jar` test |

### Wave 5: Supporting Domains

| Criteria | Validation Method |
|----------|------------------|
| search-reporting, integration are separate modules | `mvn verify` succeeds |
| HL7 message processing works end-to-end | HL7 integration test |
| Module system loads/starts/stops modules | Module lifecycle test |
| Scheduler executes tasks | Scheduler test |
| Lucene search functional | Search integration test |

### Wave 6: Web Adapter + Full Spring Boot

| Criteria | Validation Method |
|----------|------------------|
| web-adapter is separate Maven module | `mvn verify` succeeds |
| All web filters functional | Filter chain test |
| Startup/initialization flow works | First-time setup test |
| `applicationContext-service.xml` removed | File absence check |
| No `Context.getService()` calls remain | Static analysis |
| Executable JAR starts and serves requests | JAR deployment test |
| All ~299 test files pass | Full test suite green |

## Cross-Wave Validation

### Data Integrity
- Patient data remains intact after each wave
- Encounter-Obs-Order relationships preserved
- Concept dictionary unchanged
- User/role/privilege assignments preserved
- Liquibase migration history continuous

### API Compatibility
- Existing OpenMRS modules can still load (through compatibility layer)
- REST API responses unchanged (if webservices.rest module is used)
- Module system still supports .omod loading

### Performance
- Service method response times within acceptable range of baseline
- Hibernate query count per operation not increased significantly
- Memory usage comparable to pre-migration

### Deployment
- Deployable on AWS EC2 Linux at every wave
- Database migration from wave N to wave N+1 is non-destructive
- Rollback to previous wave possible via WAR redeployment

## Final Validation (Post-Wave 6)

| Criteria | Status Required |
|----------|----------------|
| 12 Maven modules compile and pass tests | ✅ Required |
| No circular module dependencies | ✅ Required |
| Spring Boot application starts in < 60s | ✅ Required |
| All domain modules have domain/application/infrastructure layers | ✅ Required |
| `Context.java` is deprecated or removed | ✅ Required |
| All HBM XMLs replaced with JPA annotations | ✅ Required |
| `applicationContext-service.xml` removed | ✅ Required |
| Java 21 target | ✅ Required |
| Log4j 1.x removed | ✅ Required |
| All HIGH severity technical debt addressed | ✅ Required |

## Related Documentation

- [Component Order](component-order.md)
- [Test Specifications](test-specifications.md)
- [Technical Debt Report](../technical-debt-report.md)
