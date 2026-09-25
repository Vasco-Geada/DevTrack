# DevTrack

DevTrack is a Java-based project and issue tracking application developed to practice and demonstrate core software engineering concepts, with a strong focus on Object-Oriented Programming and clean code.

The project currently runs as a Java application and is being developed incrementally, with each stage introducing new concepts and architectural improvements.

## Features

- Project and issue management
- Multiple issue types:
  - Bug
  - Feature
  - Task
- Issue priorities and statuses
- Issue assignment to users
- Controlled issue state transitions
- Search and filtering of issues
- Priority-based sorting
- Custom domain exceptions
- Input and entity validation
- Unit testing

## Object-Oriented Programming Concepts

DevTrack applies several OOP concepts, including:

- Encapsulation
- Abstraction
- Inheritance
- Polymorphism
- Interfaces
- Method overriding
- Composition and object relationships

For example, `Issue` is an abstract base class extended by different issue types such as `Bug`, `Feature`, and `Task`.

Each issue type can provide its own behaviour while still being handled through the common `Issue` type.

## Technologies

- Java 21
- Maven
- JUnit 5
- Java Collections Framework
- Streams API
- Lambda Expressions

## Project Structure

```text
src/main/java/
└── devtrack/
    ├── exception/
    │   ├── InvalidEntityException.java
    │   └── InvalidIssueStateException.java
    │
    ├── model/
    │   ├── Assignable.java
    │   ├── Issue.java
    │   ├── Bug.java
    │   ├── Feature.java
    │   ├── Task.java
    │   ├── Project.java
    │   ├── User.java
    │   └── Enums/
    │
    ├── service/
    │   └── IssueService.java
    │
    ├── utils/
    │   └── MainUtils.java
    │
    └── Main.java
```

## Issue Lifecycle

Issues follow controlled state transitions:

```text
OPEN → IN_PROGRESS → DONE
```

Issues can also be moved back when necessary:

```text
OPEN ⇄ IN_PROGRESS ⇄ DONE
```

Invalid state transitions are prevented through domain validation and custom exceptions.

## Issue Types

### Bug

Represents a software defect and includes a severity level.

### Feature

Represents a new feature or improvement and can contain acceptance criteria.

### Task

Represents a general development task and includes an estimated number of working hours.

## Issue Service

`IssueService` provides operations for querying and processing project issues using the Java Streams API.

Current functionality includes:

- Filter by status
- Filter by priority
- Find issues assigned to a user
- Search issues by title
- Sort issues by priority
- Count open issues

## Testing

The project uses JUnit 5 for unit testing.

Tests cover behaviour such as:

- Valid and invalid issue state transitions
- Issue assignment
- Task validation
- Project issue management
- Protection of internal collections

Tests can be executed with:

```bash
mvn test
```

## Roadmap

DevTrack is being developed incrementally. Planned future stages include:

- Improved service architecture
- Persistence layer
- PostgreSQL database
- Spring Boot REST API
- Spring Data JPA / Hibernate
- DTOs and API validation
- Next.js frontend
- Data visualization and project analytics

The long-term goal is to evolve DevTrack into a full-stack project management web application.

## Author

**Vasco Geada**

GitHub: [Vasco-Geada](https://github.com/Vasco-Geada)
