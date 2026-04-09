# Architecture Diagrams — OpenMRS Core 2.0.3

## System Context Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    External Systems                          │
│                                                              │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────────┐  │
│  │ HL7 Sources │  │ Community    │  │ Browser/REST      │  │
│  │ (Lab, ADT)  │  │ Modules     │  │ Clients           │  │
│  └──────┬──────┘  └──────┬───────┘  └──────────┬────────┘  │
│         │                │                      │            │
└─────────┼────────────────┼──────────────────────┼────────────┘
          │                │                      │
          ▼                ▼                      ▼
┌─────────────────────────────────────────────────────────────┐
│               OpenMRS Core (WAR Application)                 │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              Servlet Container (Tomcat 7+)             │  │
│  │                                                        │  │
│  │  ┌──────────┐  ┌───────────┐  ┌───────────────────┐  │  │
│  │  │ Web      │  │ Service   │  │ Module System     │  │  │
│  │  │ Filters  │  │ Layer     │  │ (Dynamic Plugins) │  │  │
│  │  └──────────┘  └───────────┘  └───────────────────┘  │  │
│  │  ┌──────────┐  ┌───────────┐  ┌───────────────────┐  │  │
│  │  │ DAO      │  │ Hibernate │  │ Scheduler         │  │  │
│  │  │ Layer    │  │ ORM       │  │ (Timer-based)     │  │  │
│  │  └──────────┘  └───────────┘  └───────────────────┘  │  │
│  └───────────────────────────────────────────────────────┘  │
│                          │                                   │
└──────────────────────────┼───────────────────────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │   Database             │
              │   MySQL 5.x (primary)  │
              │   PostgreSQL 9.x       │
              │   H2 (testing)         │
              └────────────────────────┘
```

## Current Layered Architecture

```
┌─────────────────────────────────────────────────────────────┐
│ PRESENTATION LAYER (web module)                              │
│ ┌──────────┐ ┌───────────────┐ ┌──────────────────────────┐│
│ │ Filters  │ │ Dispatcher    │ │ Module Web Integration   ││
│ │ (6 types)│ │ Servlet       │ │ (Servlets, Filters)      ││
│ └──────────┘ └───────────────┘ └──────────────────────────┘│
├─────────────────────────────────────────────────────────────┤
│ SERVICE LAYER (api module — api/impl packages)               │
│ ┌──────────────────────────────────────────────────────────┐│
│ │ AOP Proxy: Auth → Logging → RequiredData → Service Impl  ││
│ ├──────────────────────────────────────────────────────────┤│
│ │ PatientSvc │ EncounterSvc │ OrderSvc │ ConceptSvc │ ... ││
│ └──────────────────────────────────────────────────────────┘│
├─────────────────────────────────────────────────────────────┤
│ DATA ACCESS LAYER (api module — db/hibernate packages)       │
│ ┌──────────────────────────────────────────────────────────┐│
│ │ HibernatePatientDAO │ HibernateEncounterDAO │ ...        ││
│ │ 84 HBM XML Mappings │ Criteria/HQL Queries               ││
│ └──────────────────────────────────────────────────────────┘│
├─────────────────────────────────────────────────────────────┤
│ DOMAIN MODEL (api module — root org.openmrs package)         │
│ ┌───────┐┌───────┐┌─────┐┌─────┐┌───────┐┌────────┐      │
│ │Person ││Patient││Enctr││Obs  ││Order  ││Concept │ ...  │
│ └───────┘└───────┘└─────┘└─────┘└───────┘└────────┘      │
├─────────────────────────────────────────────────────────────┤
│ CROSS-CUTTING (api module)                                   │
│ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────────────┐ │
│ │ Context  │ │ Module   │ │ HL7      │ │ Scheduler      │ │
│ │ (Locator)│ │ System   │ │ Process  │ │ (Timer tasks)  │ │
│ └──────────┘ └──────────┘ └──────────┘ └────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Proposed Modular-Monolith Target Architecture

```
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot Application (single deployable)      │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                   web-adapter                         │   │
│  │  Controllers │ Filters │ REST API │ Module Web       │   │
│  └────────────┬──────────────────────────┬──────────────┘   │
│               │                          │                    │
│  ┌────────────┼──────────────────────────┼──────────────┐   │
│  │     Domain Modules (internal Maven modules)           │   │
│  │                                                       │   │
│  │  ┌─────────┐ ┌─────────┐ ┌───────────┐ ┌─────────┐ │   │
│  │  │ patient │ │ person  │ │ encounter │ │ observ. │ │   │
│  │  │ domain/ │ │ domain/ │ │ domain/   │ │ domain/ │ │   │
│  │  │ app/    │ │ app/    │ │ app/      │ │ app/    │ │   │
│  │  │ infra/  │ │ infra/  │ │ infra/    │ │ infra/  │ │   │
│  │  └─────────┘ └─────────┘ └───────────┘ └─────────┘ │   │
│  │  ┌─────────┐ ┌──────────┐ ┌───────────────────────┐ │   │
│  │  │ orders  │ │ concepts │ │ users-admin            │ │   │
│  │  │ domain/ │ │ domain/  │ │ domain/ │ app/ │ infra/│ │   │
│  │  │ app/    │ │ app/     │ │                        │ │   │
│  │  │ infra/  │ │ infra/   │ │                        │ │   │
│  │  └─────────┘ └──────────┘ └───────────────────────┘ │   │
│  │  ┌──────────┐ ┌──────────────┐ ┌────────────────┐   │   │
│  │  │ location │ │ integration  │ │ search-report  │   │   │
│  │  │          │ │ (HL7,module) │ │ (Cohort,Search)│   │   │
│  │  └──────────┘ └──────────────┘ └────────────────┘   │   │
│  │                                                       │   │
│  ├───────────────────────────────────────────────────────┤   │
│  │                  shared-kernel                         │   │
│  │  Base classes │ Interfaces │ Common utils │ Events    │   │
│  └───────────────────────────────────────────────────────┘   │
│                                                               │
│  ┌───────────────────────────────────────────────────────┐   │
│  │              Infrastructure                            │   │
│  │  Hibernate 6 │ Liquibase 4 │ Spring Boot Auto-config  │   │
│  └───────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │   Database (MySQL 8)    │
              │   (single database)     │
              └────────────────────────┘
```

## Service Dependency Flow

```
                    ┌──────────────┐
                    │ web-adapter  │
                    └──────┬───────┘
                           │ depends on all domain modules
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
    ┌──────────┐   ┌──────────┐   ┌──────────────┐
    │ patient  │   │ encounter│   │   orders     │
    └────┬─────┘   └────┬─────┘   └──────┬───────┘
         │              │                 │
         ▼              ▼                 ▼
    ┌──────────┐   ┌──────────┐   ┌──────────────┐
    │ person   │   │ location │   │   concepts   │
    └────┬─────┘   └────┬─────┘   └──────┬───────┘
         │              │                 │
         └──────────────┼─────────────────┘
                        ▼
                 ┌──────────────┐
                 │ shared-kernel│
                 └──────────────┘
```

## Security Boundaries

```
┌─────────────────────────────────────────────────────────────┐
│ TRUST BOUNDARY: Servlet Container                            │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ AUTHENTICATION BOUNDARY                                 │ │
│  │                                                         │ │
│  │  StartupFilter → InitializationFilter → UpdateFilter    │ │
│  │       ↓ (if initialized)                                │ │
│  │  OpenmrsFilter → Context.authenticate()                 │ │
│  │       ↓                                                 │ │
│  │  UserContext (thread-local authenticated user)           │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ AUTHORIZATION BOUNDARY (AOP)                            │ │
│  │                                                         │ │
│  │  AuthorizationAdvice checks @Authorized on every        │ │
│  │  service method call. 189 distinct privileges.          │ │
│  │                                                         │ │
│  │  Superuser → bypass all checks                          │ │
│  │  Anonymous → limited access                             │ │
│  │  Regular   → role/privilege-based                       │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ DATA BOUNDARY                                           │ │
│  │                                                         │ │
│  │  Soft delete (void/retire) — data never hard-deleted    │ │
│  │  Audit trail — all changes tracked                      │ │
│  │  Immutable interceptors — Obs/Order cannot be modified  │ │
│  │  Daemon tokens — controlled background access           │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Related Documentation

- [Structural Diagrams](../structural/structural-diagrams.md)
- [Behavioral Diagrams](../behavioral/behavioral-diagrams.md)
- [System Overview](../../architecture/system-overview.md)
- [Migration Plan](../../migration/component-order.md)
