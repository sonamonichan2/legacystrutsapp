# Security Patterns — OpenMRS Core 2.0.3

## Authentication

### Authentication Flow
```
User Login → Context.authenticate(username, password)
    → UserContext.authenticate(username, password)
        → ContextDAO.authenticate(username, password)
            → HibernateContextDAO.authenticate()
                ├── Lookup user by username
                ├── Get LoginCredential from database
                ├── Security.hashSecret(password + salt)
                ├── Compare hashed password
                ├── If match: set UserContext.authenticatedUser
                └── If no match: throw ContextAuthenticationException
```

### Password Security (`Security.java` — 370 lines)
- Password hashing using SHA-512 (configurable via global property)
- Salt generation using `SecureRandom`
- Hash format: `algorithm:salt:hash`
- Supported algorithms: SHA-1, SHA-256, SHA-512
- Legacy support for unsalted MD5 hashes (from older OpenMRS versions)
- Password stored in separate `LoginCredential` table (not on User entity)

### Password Policy (UserServiceImpl)
- Minimum length: configurable via `security.passwordMinimumLength` (default: 8)
- Requires uppercase letter (configurable)
- Requires lowercase letter (configurable)
- Requires digit (configurable)
- Cannot match username
- Recent password reuse prevention
- Custom exceptions: `ShortPasswordException`, `WeakPasswordException`, `InvalidCharactersPasswordException`

## Authorization

### AOP-Based Authorization (`AuthorizationAdvice` — 147 lines)
- Implements `MethodBeforeAdvice` — runs before every service method
- Reads `@Authorized` annotation from method or declaring interface
- Logic:
  1. If no `@Authorized` annotation → allow (no restriction)
  2. If `@Authorized(requireAll=false)` → user must have ANY listed privilege
  3. If `@Authorized(requireAll=true)` → user must have ALL listed privileges
  4. Superuser role bypasses all checks
  5. Anonymous user with required privileges → `APIAuthenticationException`

### Privilege System
- **189 privilege constants** defined in `PrivilegeConstants.java`
- Privileges are fine-grained: `ADD_PATIENTS`, `EDIT_ENCOUNTERS`, `DELETE_OBS`, etc.
- Privileges assigned to Roles, Roles assigned to Users
- Hierarchical roles (roles can inherit from parent roles)
- Special roles: `Superuser` (all privileges), `Anonymous` (pre-login)

### Key Privilege Categories
- **Patient**: ADD_PATIENTS, EDIT_PATIENTS, DELETE_PATIENTS, VIEW_PATIENTS
- **Encounters**: ADD_ENCOUNTERS, EDIT_ENCOUNTERS, DELETE_ENCOUNTERS, VIEW_ENCOUNTERS
- **Observations**: ADD_OBS, EDIT_OBS, DELETE_OBS, VIEW_OBS
- **Orders**: ADD_ORDERS, EDIT_ORDERS, DELETE_ORDERS, VIEW_ORDERS
- **Users**: MANAGE_USERS, VIEW_USERS, EDIT_USER_PASSWORDS
- **Admin**: MANAGE_GLOBAL_PROPERTIES, MANAGE_MODULES, SQL_LEVEL_ACCESS

## Web Layer Security

### Filter Chain Security
- **OpenmrsFilter**: Main request filter, ensures context is available
- **StartupFilter**: Blocks all requests if application not initialized
- **InitializationFilter**: Handles first-time setup (database credentials, admin user creation)
- **UpdateFilter**: Handles schema migrations before allowing access

### OWASP Encoder Usage (web module)
- `org.owasp.encoder:encoder:1.2` used in web module
- HTML encoding for XSS prevention in JSP output
- `OpenmrsCharacterEscapes` — custom Jackson character escapes

### GZIP Filter
- `GZIPFilter` compresses responses
- `GZIPResponseWrapper` / `GZIPResponseStream` — custom wrapper implementations
- `GZIPRequestWrapper` / `GZIPRequestStream` — compressed request handling

## Data Security Patterns

### Soft Delete (Void/Retire)
- Entities are never hard-deleted through normal operations
- `voidPatient()`, `retireLocation()` set flags instead of deleting
- Void/retire requires reason string for audit trail
- Only `purge*()` methods perform actual DELETE (requires elevated privileges)

### Audit Trail
- All entities track: `creator`, `dateCreated`, `changedBy`, `dateChanged`
- Voidable entities track: `voidedBy`, `dateVoided`, `voidReason`
- Retireable entities track: `retiredBy`, `dateRetired`, `retireReason`
- `RequiredDataAdvice` (AOP) auto-populates these fields
- `PersonMergeLog` tracks patient merge operations for potential undo

### Hibernate Interceptors
- `AuditableInterceptor` — additional audit logging
- `ImmutableEntityInterceptor` — prevents modification of immutable entities
- `ImmutableObsInterceptor` — prevents modification of saved observations
- `ImmutableOrderInterceptor` — prevents modification of saved orders
- `DropMillisecondsHibernateInterceptor` — normalizes timestamp precision

## Daemon Thread Security

### DaemonToken Pattern
- `DaemonToken` — security token for background threads
- `DaemonTokenAware` — interface for modules needing daemon access
- `Daemon.java` — manages daemon thread execution with elevated privileges
- Prevents modules from executing privileged operations without proper tokens

## Liquibase Schema Security

### Migration Strategy
- `liquibase-schema-only.xml` — Schema DDL only (no data)
- `liquibase-core-data.xml` — Required reference data (roles, privileges, etc.)
- `liquibase-update-to-latest.xml` — Incremental schema updates
- Changesets are idempotent and checksummed
- Custom Liquibase extensions for type conversion and identity insert

## Security Concerns

| Concern | Severity | Description |
|---------|----------|-------------|
| Log4j 1.2.15 | High | EOL logging library with known issues |
| XStream 1.4.3 | High | Known deserialization vulnerabilities |
| Commons-FileUpload 1.2.1 | Medium | Known CVEs in older versions |
| SHA-1 password hashing support | Medium | Legacy hash algorithm still supported |
| Unencrypted database passwords | Medium | Runtime properties file contains DB credentials in plaintext |
| No CSRF protection visible | Low | Web filter chain lacks explicit CSRF filter |

## Related Documentation

- [Code Metrics](code-metrics.md)
- [Complexity Analysis](complexity-analysis.md)
- [Error Handling](../behavior/error-handling.md)
- [Technical Debt Report](../technical-debt-report.md)
