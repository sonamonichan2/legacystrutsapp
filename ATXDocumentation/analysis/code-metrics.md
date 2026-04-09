# Code Metrics — OpenMRS Core 2.0.3

## Summary Metrics

| Metric | Value |
|--------|-------|
| **Total Source LOC** | ~209,984 |
| **Main Java Source Files** | ~688 (api: 649, web: 39) |
| **Test Java Source Files** | ~299 (api: 288, web: 11) |
| **Maven Modules** | 5 (tools, test, api, web, webapp) |

## Domain Model Metrics

| Metric | Count |
|--------|-------|
| Domain Entity Classes (`org.openmrs.*`) | ~105 |
| Service Interfaces | ~20 |
| Service Implementations | ~20 |
| DAO Interfaces | ~27 |
| Hibernate DAO Implementations | ~30+ |
| Validators | ~56 |
| Hibernate XML Mappings | 84 |
| Custom Exception Classes | ~45 |
| AOP Advice Classes | 3 |
| Module System Classes | ~22 |

## Service Implementation Sizes

| Service | Lines | Complexity Rating |
|---------|-------|------------------|
| ConceptServiceImpl | 1,891 | Very High |
| PatientServiceImpl | 1,590 | Very High |
| OrderServiceImpl | 1,013 | High |
| PersonServiceImpl | 939 | High |
| EncounterServiceImpl | 912 | High |
| AdministrationServiceImpl | 826 | High |
| FormServiceImpl | 792 | Medium |
| UserServiceImpl | 664 | Medium |
| ObsServiceImpl | 597 | Medium |
| ProgramWorkflowServiceImpl | 534 | Medium |
| LocationServiceImpl | 476 | Medium |
| VisitServiceImpl | 388 | Low |
| ProviderServiceImpl | 291 | Low |
| DatatypeServiceImpl | 236 | Low |
| CohortServiceImpl | 164 | Low |
| SerializationServiceImpl | 149 | Low |
| OrderSetServiceImpl | 127 | Low |
| **Total Service Impl LOC** | **11,735** | — |

## DAO Implementation Sizes

| DAO | Lines | Complexity Rating |
|-----|-------|------------------|
| HibernateConceptDAO | 1,984 | Very High |
| HibernatePatientDAO | 792 | High |
| HibernateEncounterDAO | 657 | Medium |
| HibernateOrderDAO | 584 | Medium |

## Infrastructure Complexity

| Component | Lines | Complexity Rating |
|-----------|-------|------------------|
| ModuleFactory | 1,687 | Very High |
| ModuleClassLoader | 1,103 | Very High |
| applicationContext-service.xml | 689 | High |
| PrivilegeConstants (189 constants) | 551 | Medium |
| Security.java (password hashing) | 370 | Medium |
| AuthorizationAdvice | 147 | Low |

## Configuration Metrics

| Config File | Lines | Entries |
|------------|-------|---------|
| applicationContext-service.xml | 689 | ~50+ bean definitions |
| hibernate.cfg.xml | 121 | 84 entity mappings |
| web.xml | ~150 | 8+ filters, 3+ servlets, 5+ listeners |
| Root pom.xml | ~600+ | 50+ dependencies, 15+ plugins |

## Related Documentation

- [Complexity Analysis](complexity-analysis.md)
- [Security Patterns](security-patterns.md)
- [Components](../architecture/components.md)
