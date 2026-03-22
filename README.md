# Students Microservice - Scotiabank Code Challenge

Reactive REST microservice built with Spring WebFlux for managing students.

## Tech Stack

- Java 17
- Spring Boot 3.4.1
- Spring WebFlux (reactive REST)
- Spring Data R2DBC (reactive database access)
- H2 (in-memory database)
- Lombok
- Swagger / OpenAPI
- JUnit 5 + Mockito + StepVerifier
- Jacoco (100% code coverage)
- Gradle Groovy

## Architecture

Layered architecture with the following packages:
```
com.scotiabank.students
├── controller    # REST endpoints
├── service       # Business logic
├── repository    # Database access
├── model         # Entity
├── dto           # Request objects with validations
├── exception     # Custom exceptions and global handler
└── config        # Swagger configuration
```

## Endpoints

### POST /api/students
Creates a new student.

**Request body:**
```json
{
    "id": "A001",
    "firstName": "Juan",
    "lastName": "Perez",
    "status": "ACTIVE",
    "age": 20
}
```

**Responses:**
- `201 Created` — student created successfully
- `400 Bad Request` — validation error
- `409 Conflict` — student with given id already exists

### GET /api/students/active
Returns all active students.

**Responses:**
- `200 OK` — list of active students (can be empty)

## Validations

| Field | Rules |
|---|---|
| id | Not blank, max 20 chars, alphanumeric |
| firstName | Not blank, 2-50 chars |
| lastName | Not blank, 2-50 chars |
| status | Not blank, must be ACTIVE or INACTIVE |
| age | Between 1 and 120 |

## Running the app
```bash
./gradlew bootRun
```

App runs on `http://localhost:8080`

## Swagger UI
```
http://localhost:8080/swagger-ui.html
```

## Running tests
```bash
./gradlew test
```

## Test coverage report
```bash
./gradlew test jacocoTestReport
```

Report available at: `build/reports/jacoco/test/html/index.html`