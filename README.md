# 🐾 Pet Clinic

A modern **veterinary clinic management platform** built with **Java, Spring Boot, PostgreSQL, and React**.

The project is being developed as a backend-first application with a focus on clean architecture, security,
maintainability, and real-world enterprise backend practices.

The backend provides the core domain, security, persistence, and REST API capabilities. A dedicated **React.js
administration panel** will be added to provide the user interface for clinic administrators and staff.

> 🚧 **Project status:** Active development
> The backend is currently under development. The React administration panel is planned for a future phase.

---

## 🎯 Project Goals

Pet Clinic is intended to provide a complete management system for veterinary clinics.

The long-term goal is to support:

* Pet management
* Owner management
* Veterinarian management
* Veterinary visits
* Medical records
* Pet types and breeds
* Appointment management
* User and role management
* Clinic administration
* Audit/history tracking
* Secure authentication and authorization
* Administrative dashboards and statistics

The project is also being used as a practical implementation of modern **Spring Boot backend development**, including
security, persistence, validation, API design, and application architecture.

---

## 🏗️ Architecture

The project follows a **backend-first architecture**.

```text
┌──────────────────────────────┐
│        React Admin Panel     │
│          (Planned)           │
└──────────────┬───────────────┘
               │ REST API
               ▼
┌──────────────────────────────┐
│       Spring Boot API        │
│                              │
│  Controllers                 │
│  Services                    │
│  Domain                      │
│  Repositories                │
│  Security                    │
│  Validation                  │
└──────────────┬───────────────┘
               │
       ┌───────┼────────┐
       ▼       ▼        ▼
 PostgreSQL  Redis   Keycloak
```

The backend is designed so that the frontend remains independent from the domain and business logic.

---

## 🛠️ Technology Stack

### Backend

| Technology             | Purpose                         |
|------------------------|---------------------------------|
| Java 21                | Programming language            |
| Spring Boot 4.1        | Application framework           |
| Spring Web             | REST APIs                       |
| Spring Data JPA        | Data access                     |
| Hibernate              | ORM                             |
| Spring Security        | Application security            |
| OAuth2 Resource Server | JWT-based API security          |
| Keycloak               | Identity and access management  |
| PostgreSQL             | Relational database             |
| Redis                  | Caching / distributed data      |
| Redisson               | Redis integration               |
| Spring Validation      | Request validation              |
| Spring Mail            | Email capabilities              |
| Springdoc OpenAPI      | API documentation               |
| Maven                  | Build and dependency management |
| Docker Compose         | Local infrastructure            |

### Frontend

The administration panel is planned to be built with:

* React.js
* JavaScript / TypeScript
* REST API integration
* Modern component-based UI

> The React application is intentionally planned as a separate phase so that the backend API and domain model can be
> developed independently.

---

## 🔐 Security

Security is an important part of the project architecture.

The backend is being designed around:

* Spring Security
* OAuth2 Resource Server
* JWT authentication
* Keycloak
* Role-based access control (RBAC)
* Permission-based authorization
* Secure API endpoints
* Validation of incoming requests

The goal is to keep authentication and authorization responsibilities clearly separated from business logic.

---

## 🐕 Core Domain

The application is centered around the following concepts:

```text
Owner
  │
  └─── Pet
         │
         ├── Pet Type
         │
         └── Visits
                │
                ├── Veterinarian
                └── Medical Information
```

The domain will evolve as the backend implementation progresses.

Planned areas include:

### Owners

Manage information about pet owners and their pets.

### Pets

Manage:

* Name
* Pet type
* Breed
* Birth information
* Owner
* Medical information
* Status

### Veterinarians

Manage veterinary staff and their professional information.

### Visits

Manage interactions between pets and veterinarians, including:

* Visit date/time
* Veterinarian
* Pet
* Reason for visit
* Medical notes
* Treatment information

### Administration

Provide administrative capabilities for managing:

* Users
* Roles
* Permissions
* Owners
* Pets
* Veterinarians
* Visits
* Clinic configuration

---

## 📊 Auditing

The application uses Spring Data auditing for common entity metadata such as:

* Creation timestamp
* Last modification timestamp
* Creator
* Last modifier
* Record status

Hibernate Envers can be selectively enabled for entities where historical changes are important.

This allows the project to avoid creating unnecessary audit tables for every entity.

---

## 📚 API Documentation

The backend uses **OpenAPI** for API documentation.

Once the application is running, the API documentation will be available through the configured Springdoc/OpenAPI
endpoints.

The exact endpoints and API contract will evolve together with the backend implementation.

---

## ⚙️ Configuration

The application supports environment-specific configuration through Spring profiles.

Current profiles include:

```text
home
company
```

Example configuration structure:

```text
src/main/resources/
├── application.yml
├── application-home.yml
└── application-company.yml
```

Sensitive configuration such as:

* Database credentials
* Keycloak credentials
* Redis configuration
* Mail credentials
* Secrets

should be provided through environment variables or local configuration and **must not be committed to the repository**.

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

* Java 21+
* Git
* Maven (or use the included Maven Wrapper)
* Docker
* Docker Compose
* PostgreSQL

Additional infrastructure such as Keycloak and Redis may be required depending on the active application profile.

---

### Clone the Repository

```bash
git clone https://github.com/farzan6118/pet-clinic.git

cd pet-clinic
```

---

### Build the Project

Using Maven Wrapper:

```bash
./mvnw clean verify
```

On Windows:

```powershell
mvnw.cmd clean verify
```

---

### Run the Application

Using the default profile:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

To explicitly select a profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=home
```

or:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=company
```

---

## 🐳 Docker

Docker Compose is used to simplify local infrastructure setup.

The project includes Spring Boot Docker Compose integration, allowing infrastructure services to be managed alongside
local development.

As the infrastructure evolves, the required services will be documented here.

---

## 🧪 Testing

Testing is an important part of the project.

The backend is intended to include:

* Unit tests
* Service-layer tests
* Repository tests
* Controller/API tests
* Security tests
* Integration tests

Run the test suite with:

```bash
./mvnw test
```

---

## 📁 Project Structure

The project is currently organized as a standard Spring Boot application.

The architecture will evolve as the domain grows, with responsibilities separated between:

```text
src/
└── main/
    ├── java/
    │   └── com.github.farzan6118.petclinic/
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── entity/
    │       ├── dto/
    │       ├── mapper/
    │       ├── security/
    │       └── config/
    │
    └── resources/
        ├── application.yml
        └── application-*.yml
```

The exact package structure may change as the application moves toward a more mature architecture.

---

## 🖥️ React Administration Panel

A React-based administration panel is planned as the next major frontend phase.

The administration panel will consume the Spring Boot REST API and provide interfaces for clinic staff.

Planned features include:

* Dashboard
* Owner management
* Pet management
* Veterinarian management
* Visit management
* User management
* Role and permission management
* Search and filtering
* Pagination
* Statistics
* Authentication
* Authorization-aware navigation

The frontend will remain decoupled from the backend so that the REST API can also support other clients in the future.

---

## 🗺️ Roadmap

### Phase 1 — Backend Foundation

* [x] Spring Boot project
* [x] Java 21
* [x] Maven
* [x] PostgreSQL integration
* [x] Spring Data JPA
* [x] Spring Security foundation
* [x] OAuth2 Resource Server
* [x] Keycloak integration foundation
* [x] Redis / Redisson integration
* [x] Validation
* [x] OpenAPI integration
* [ ] Complete domain model
* [ ] Database migrations
* [ ] Complete RBAC implementation
* [ ] REST API implementation
* [ ] Exception handling
* [ ] Comprehensive testing

### Phase 2 — Veterinary Domain

* [ ] Owners
* [ ] Pets
* [ ] Pet types
* [ ] Breeds
* [ ] Veterinarians
* [ ] Visits
* [ ] Medical records
* [ ] Appointment management

### Phase 3 — Administration API

* [ ] User management
* [ ] Role management
* [ ] Permission management
* [ ] Audit/history
* [ ] Dashboard APIs
* [ ] Statistics
* [ ] Advanced search and filtering

### Phase 4 — React Administration Panel

* [ ] React application
* [ ] Authentication
* [ ] Protected routes
* [ ] Dashboard
* [ ] Owner management UI
* [ ] Pet management UI
* [ ] Veterinarian management UI
* [ ] Visit management UI
* [ ] User/RBAC management UI
* [ ] Responsive design

### Phase 5 — Production Readiness

* [ ] Dockerized deployment
* [ ] CI/CD
* [ ] Integration testing
* [ ] Observability
* [ ] Logging improvements
* [ ] Health checks
* [ ] Production configuration
* [ ] Security hardening
* [ ] Performance optimization

---

## 🎓 Project Focus

This project is not intended to be just a CRUD application.

The main technical goals are to demonstrate practical backend engineering concepts such as:

* Clean code
* SOLID principles
* Layered architecture
* REST API design
* Domain modeling
* JPA/Hibernate
* Database design
* Transaction management
* Authentication and authorization
* RBAC
* Caching
* Validation
* Exception handling
* Auditing
* Testing
* API documentation
* Containerized development

The project will continue to evolve as new backend and frontend capabilities are implemented.

---

## 📌 Project Status

**Current status: Backend development**

The project is currently focused on establishing the backend architecture, domain model, persistence layer, security
model, and REST API.

The React administration panel will be introduced after the backend foundation and API contracts become sufficiently
mature.

---

## 📄 License

License information will be added as the project approaches its first public release.

---

## 👨‍💻 Author

**Farzan Saketi**

Java / Spring Backend Developer

GitHub:
https://github.com/farzan6118
