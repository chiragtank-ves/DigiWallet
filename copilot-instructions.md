# GitHub Copilot Instructions for DigiWallet Project

## Project Overview

**DigiWallet** is a Digital Wallet Management System built with **Spring Boot 3.5.9** and **Java 17**. It follows a layered architecture pattern with clear separation of concerns across Web/Controller, Service, Repository, and Model layers.

---

## Technology Stack

- **Framework**: Spring Boot 3.5.9
- **Language**: Java 17
- **Database**: MySQL 8.0 (Production), H2 (Testing)
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Testing**: JUnit 5, Mockito, MockMvc, AssertJ
- **Build Tool**: Maven
- **Logging**: SLF4J with Logback

---

## Project Architecture

### Layered Architecture Pattern

```
Web Layer (Frontend/API Consumers)
    ↓
Controller Layer (@RestController)
    ↓
Service Layer (@Service)
    ↓
Repository Layer (JpaRepository)
    ↓
Model Layer (@Entity)
    ↓
Database (MySQL)
```

### Package Structure

```
com.orion.DigiWallet
├── controller/          # REST API endpoints
├── service/            # Business logic
├── repository/         # Data access layer
├── model/              # JPA entities
└── DigiWalletApplication.java
```

---

## Domain Models (Entities)

### Core Entities

1. **User** - Application users with roles (USER/ADMIN)
2. **Wallet** - User's digital wallet (one-to-one with User)
3. **Card** - Payment cards linked to wallets
4. **Transaction** - Financial transactions (CREDIT/DEBIT)
5. **Category** - Transaction categorization
6. **RewardsPoint** - User reward points

### Key Relationships

- **User ↔ Wallet**: One-to-One (One user has exactly one wallet)
- **Wallet ↔ Card**: One-to-Many (One wallet can have multiple cards)
- **Wallet ↔ Transaction**: One-to-Many (One wallet has many transactions)
- **Transaction ↔ Category**: Many-to-One (Transactions belong to categories)

---

## Coding Standards & Patterns

### 1. Controller Layer

**Naming Convention**: `{Entity}Controller.java`

**Patterns to Follow**:
```java
@RestController
@RequestMapping("/api/{entities}")
public class EntityController {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityController.class);
    private final EntityService entityService;
    
    // Constructor injection (preferred over @Autowired)
    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }
    
    // Use @ResponseStatus for explicit HTTP status codes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Entity getById(@PathVariable Long id) {
        logger.info("GET /api/entities/{} called", id);
        return entityService.getById(id);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Entity create(@RequestBody Entity entity) {
        logger.info("POST /api/entities called");
        return entityService.create(entity);
    }
}
```

**Key Points**:
- Always use constructor injection
- Add `@ResponseStatus` annotations for clarity
- Log all incoming requests with `logger.info()`
- Use meaningful path variables and request mappings
- Keep controllers thin - delegate to services

---

### 2. Service Layer

**Naming Convention**: `{Entity}Service.java`

**Patterns to Follow**:
```java
@Service
public class EntityService {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
    private final EntityRepository entityRepository;
    
    @Autowired
    public EntityService(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }
    
    @Transactional
    public Entity create(Entity entity) {
        logger.info("Creating new entity");
        
        // Step 1: Validation
        if (entityRepository.existsByUniqueField(entity.getUniqueField())) {
            throw new RuntimeException("Entity already exists");
        }
        
        // Step 2: Business logic
        // ... apply business rules ...
        
        // Step 3: Save and return
        Entity saved = entityRepository.save(entity);
        logger.info("Entity created with id: {}", saved.getId());
        return saved;
    }
    
    public Entity getById(Long id) {
        logger.info("Fetching entity with id {}", id);
        return entityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }
}
```

**Key Points**:
- Use `@Transactional` for write operations
- Always add detailed logging
- Use meaningful exception messages with context (include IDs, names, etc.)
- Follow the pattern: Validate → Process → Save → Return
- Comment each step for complex operations

---

### 3. Repository Layer

**Naming Convention**: `{Entity}Repository.java`

**Patterns to Follow**:
```java
@Repository
public interface EntityRepository extends JpaRepository<Entity, Long> {
    
    // Custom query methods using Spring Data JPA naming conventions
    Optional<Entity> findByUniqueField(String uniqueField);
    
    boolean existsByUniqueField(String uniqueField);
    
    List<Entity> findByStatus(String status);
    
    // Use @Query for complex queries
    @Query("SELECT e FROM Entity e WHERE e.field = :value AND e.active = true")
    List<Entity> findActiveByField(@Param("value") String value);
}
```

**Key Points**:
- Extend `JpaRepository<Entity, Long>`
- Use Spring Data JPA method naming conventions
- Return `Optional<>` for single results that might not exist
- Use `boolean exists...()` for existence checks
- Add `@Repository` annotation for clarity

---

### 4. Model/Entity Layer

**Naming Convention**: `{Entity}.java`

**Patterns to Follow**:
```java
@Entity
@Table(name = "entity_table")
public class Entity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "unique_field", nullable = false, unique = true)
    private String uniqueField;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Transient
    private String computedField; // Not persisted to DB
    
    // Lifecycle callbacks
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Standard getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... more getters/setters ...
}
```

**Key Points**:
- Always use `@Entity` and `@Table(name = "...")`
- Use `@Id` with `@GeneratedValue(strategy = GenerationType.IDENTITY)` for MySQL
- Specify column properties: `nullable`, `unique`, `length`
- Use `@PrePersist` and `@PreUpdate` for audit fields and defaults
- Use `@Transient` for computed fields that shouldn't be persisted
- Add meaningful relationship annotations (@ManyToOne, @OneToMany, etc.)

---

## Exception Handling

### Global Exception Handler Pattern

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
```

**Error Messages**:
- Always include entity type and ID in error messages
- Format: `"Entity not found with id: " + id`
- Format: `"Entity already exists with field: " + fieldValue`

---

## Testing Standards

### 1. Unit Tests (Service Layer)

**Pattern**: Use Mockito with `@ExtendWith(MockitoExtension.class)`

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("EntityService Unit Tests")
class EntityServiceTest {
    
    @Mock
    private EntityRepository entityRepository;
    
    @InjectMocks
    private EntityService entityService;
    
    private Entity testEntity;
    
    @BeforeEach
    void setUp() {
        testEntity = new Entity();
        testEntity.setId(1L);
        testEntity.setField("value");
    }
    
    @Nested
    @DisplayName("create()")
    class CreateTests {
        
        @Test
        @DisplayName("Should create entity when valid input provided")
        void shouldCreateEntity_whenValidInput() {
            // GIVEN
            Mockito.when(entityRepository.existsByField("value"))
                    .thenReturn(false);
            Mockito.when(entityRepository.save(testEntity))
                    .thenReturn(testEntity);
            
            // WHEN
            Entity result = entityService.create(testEntity);
            
            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            Mockito.verify(entityRepository).save(testEntity);
        }
        
        @Test
        @DisplayName("Should throw exception when entity already exists")
        void shouldThrowException_whenEntityExists() {
            // GIVEN
            Mockito.when(entityRepository.existsByField("value"))
                    .thenReturn(true);
            
            // WHEN & THEN
            assertThatThrownBy(() -> entityService.create(testEntity))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Entity already exists");
            
            Mockito.verify(entityRepository, Mockito.never()).save(Mockito.any());
        }
    }
}
```

**Key Testing Points**:
- Use `@Nested` classes to group related tests
- Use `@DisplayName` for readable test descriptions
- Follow Given-When-Then pattern with comments
- Use AssertJ (`assertThat`) for fluent assertions
- Verify mock interactions with `Mockito.verify()`
- Test both happy path and error scenarios

---

### 2. Integration Tests (Repository Layer)

**Pattern**: Use `@DataJpaTest` with actual database

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("EntityRepository Integration Tests")
class EntityRepositoryTest {
    
    @Autowired
    private EntityRepository entityRepository;
    
    @Autowired
    private ParentRepository parentRepository;
    
    private Parent parent;
    
    @BeforeEach
    void setUp() {
        // Clean up
        entityRepository.deleteAll();
        parentRepository.deleteAll();
        
        // Setup test data
        parent = new Parent();
        parent = parentRepository.save(parent);
    }
    
    @Test
    @DisplayName("Should find entity by unique field")
    void shouldFindByUniqueField() {
        // GIVEN
        Entity entity = new Entity();
        entity.setUniqueField("test123");
        entity.setParent(parent);
        entityRepository.save(entity);
        
        // WHEN
        Optional<Entity> found = entityRepository.findByUniqueField("test123");
        
        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getUniqueField()).isEqualTo("test123");
    }
}
```

**Key Points**:
- Use `@DataJpaTest` for repository slice testing
- Use `@ActiveProfiles("test")` to use test configuration
- Clean up data in `@BeforeEach` for test isolation
- Test custom query methods thoroughly
- Verify relationships and cascades

---

### 3. Controller Tests (API Layer)

**Pattern**: Use `@WebMvcTest` with MockMvc

```java
@WebMvcTest(EntityController.class)
@DisplayName("EntityController API Tests")
class EntityControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private EntityService entityService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Entity testEntity;
    
    @BeforeEach
    void setUp() {
        testEntity = new Entity();
        testEntity.setId(1L);
        testEntity.setField("value");
    }
    
    @Nested
    @DisplayName("POST /api/entities")
    class CreateEntityTests {
        
        @Test
        @DisplayName("Should return 201 when entity created successfully")
        void shouldReturn201_whenEntityCreated() throws Exception {
            // GIVEN
            Mockito.when(entityService.create(Mockito.any(Entity.class)))
                    .thenReturn(testEntity);
            
            // WHEN
            ResultActions result = mockMvc.perform(
                    post("/api/entities")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testEntity))
            );
            
            // THEN
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.field").value("value"));
        }
    }
    
    @Nested
    @DisplayName("GET /api/entities/{id}")
    class GetEntityByIdTests {
        
        @Test
        @DisplayName("Should return entity when valid ID provided")
        void shouldReturnEntity_whenValidId() throws Exception {
            // GIVEN
            Mockito.when(entityService.getById(1L))
                    .thenReturn(testEntity);
            
            // WHEN
            ResultActions result = mockMvc.perform(get("/api/entities/1"));
            
            // THEN
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.field").value("value"));
        }
    }
}
```

**Key Points**:
- Use `@WebMvcTest` for controller slice testing
- Use `@MockitoBean` to mock service dependencies
- Extract `ResultActions` separately for readability
- Test all HTTP methods and status codes
- Verify JSON response structure with `jsonPath()`
- Test error scenarios (404, 500, etc.)

---

## Configuration Files

### application.properties (Production)

```properties
spring.application.name=DigiWallet
spring.datasource.url=jdbc:mysql://localhost:3306/digital_wallet_db
spring.datasource.username=root
spring.datasource.password=n3u3da!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### application-test.properties (Testing)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/digital_wallet_test_db
spring.datasource.username=root
spring.datasource.password=n3u3da!
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
```

---

## Common Patterns & Best Practices

### 1. Transactional Methods

```java
@Transactional
public Entity updateEntity(Long id, Entity updatedData) {
    // Fetch existing entity
    Entity existing = entityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    
    // Update fields
    existing.setField(updatedData.getField());
    
    // Save and return (JPA will auto-flush within transaction)
    return entityRepository.save(existing);
}
```

### 2. Existence Checks Before Creation

```java
public Entity create(Entity entity) {
    // Always check existence first
    if (entityRepository.existsByUniqueField(entity.getUniqueField())) {
        throw new RuntimeException("Entity already exists with field: " + entity.getUniqueField());
    }
    return entityRepository.save(entity);
}
```

### 3. Fetch-or-Throw Pattern

```java
public Entity getById(Long id) {
    return entityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
}
```

### 4. Constructor Injection (Preferred)

```java
@Service
public class EntityService {
    private final EntityRepository repository;
    
    // Constructor injection - preferred over @Autowired on fields
    public EntityService(EntityRepository repository) {
        this.repository = repository;
    }
}
```

### 5. Logging Standards

```java
// INFO level for normal operations
logger.info("Processing request for entity id: {}", id);
logger.info("Created entity with id: {}", entity.getId());

// WARN level for recoverable issues
logger.warn("Entity not found with id: {}", id);

// ERROR level for exceptions
logger.error("Failed to process entity", exception);
```

---

## API Endpoint Patterns

### Standard REST Endpoints

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/api/entities` | Get all entities | 200 OK |
| GET | `/api/entities/{id}` | Get entity by ID | 200 OK / 404 Not Found |
| POST | `/api/entities` | Create new entity | 201 Created |
| PUT | `/api/entities/{id}` | Update entity | 200 OK / 404 Not Found |
| DELETE | `/api/entities/{id}` | Delete entity | 200 OK / 404 Not Found |

### Custom Endpoints

```java
@GetMapping("/users/{userId}/wallet")  // Get wallet by user ID
@PostMapping("/wallets/{id}/credit")   // Credit wallet
@GetMapping("/transactions/by-wallet/{walletId}")  // Get transactions
```

---

## TODO Comments Pattern

When adding TODOs, follow this format:

```java
//TODO: X.Y.Z - Brief description
// Step 1: First action
// Step 2: Second action
// Expected outcome: What should happen
```

Example:
```java
//TODO: 2.1.3 - Implement createCard method
// Step 1: Check if card number already exists
// Step 2: Save and return the card
// Expected: Throw exception if card exists, return saved card otherwise
public Card createCard(Card card) {
    // implementation here
}
```

---

## Database Naming Conventions

- **Tables**: Lowercase with underscores (e.g., `user_table`, `wallet_transaction`)
- **Columns**: Lowercase with underscores (e.g., `user_id`, `created_at`)
- **Foreign Keys**: `{entity}_id` (e.g., `wallet_id`, `user_id`)
- **Join Tables**: `{entity1}_{entity2}` (e.g., `user_role`)

---

## Testing Checklist

Before committing code, ensure:

- [ ] All unit tests pass (`./mvnw test`)
- [ ] Service layer has tests for happy path and error cases
- [ ] Repository layer has integration tests for custom queries
- [ ] Controller layer has tests for all endpoints and status codes
- [ ] Mock interactions are verified in unit tests
- [ ] Test names clearly describe what is being tested
- [ ] Tests follow Given-When-Then pattern
- [ ] `@DisplayName` annotations are used for readability

---

## Common Issues & Solutions

### 1. LazyInitializationException

**Problem**: Accessing lazy-loaded relationships outside transaction
**Solution**: Use `@Transactional` on service method or fetch with JOIN FETCH

### 2. ConstraintViolationException

**Problem**: Violating unique constraints or null constraints
**Solution**: Add existence checks before save, validate input

### 3. Test Context Fails to Load

**Problem**: Missing entity `@Entity` annotation or repository issues
**Solution**: Ensure all entities have `@Entity` and repositories extend `JpaRepository`

### 4. MockitoException in Tests

**Problem**: Mock not properly configured
**Solution**: Ensure `@Mock` and `@InjectMocks` are used correctly with `@ExtendWith(MockitoExtension.class)`

---

## Quick Reference

### Import Statements to Use

```java
// Spring Framework
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

// JPA/Hibernate
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

// Testing
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Java utilities
import java.time.LocalDateTime;
import java.util.*;
```

---

## Project-Specific Business Rules

1. **User-Wallet Relationship**: One user has exactly ONE wallet
2. **Card Numbers**: Must be unique across the system (16 digits)
3. **Transaction Types**: Only CREDIT or DEBIT allowed
4. **Wallet Status**: ACTIVE wallets can transact, INACTIVE cannot
5. **Balance Updates**: Always through transactions, never direct
6. **Default Values**: Use `@PrePersist` for createdAt, status, etc.
7. **Exception Messages**: Always include entity ID in error messages
8. **Logging**: Log all incoming requests and important operations

---

## When Generating Code, Remember:

1. ✅ Use constructor injection, not field injection
2. ✅ Add `@ResponseStatus` to controller methods
3. ✅ Log all operations with context
4. ✅ Use Optional and orElseThrow() pattern
5. ✅ Add @Transactional for write operations
6. ✅ Write tests following Given-When-Then
7. ✅ Use meaningful exception messages
8. ✅ Follow existing naming conventions
9. ✅ Add @DisplayName to test methods
10. ✅ Verify mock interactions in tests

---

## Swagger/OpenAPI

Access API documentation at: `http://localhost:8080/swagger-ui.html`

---

## Project Maintainers Notes

- This is a learning project focused on Spring Boot best practices
- Code includes extensive TODO comments for guided learning
- Tests are written before implementation (TDD approach)
- Each layer is independently testable
- Follow the TODO_MASTER_LIST.md for implementation order

---

**Last Updated**: February 2026
**Spring Boot Version**: 3.5.9
**Java Version**: 17
