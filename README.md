# Backend Setup Guide

Follow the steps below to set up and run the backend application:

---

## 1. Clone the Repository

Clone the backend repository to your local machine:

```bash
git clone https://github.com/Matiaszz/BackendSpringTaskManagement.git
cd ./BackendSpringTaskManagement/TaskManagement
```

---

## 2. Create the `.env` File

Create a `.env` file in the root directory of the project, following the structure provided in the `.env.example` file. Update the values according to your local environment.

```bash
cp .env.example .env
```

---

## 3. Start Docker Containers

Use Docker Compose to start the required services:

```bash
docker-compose up -d
```

This will start the database and any other services defined in `docker-compose.yml`.

---

## 4. Update `application.yml`

Open the `application.yml` file and replace the default password (`19112006`) with the default root password used in your environment:

```yaml
spring:
  datasource:
    password: <your-root-password>
```

---

## 5. Run the Spring Application

Start the Spring Boot application using your preferred IDE or run it from the command line:

### If using Maven:
```bash
./mvnw spring-boot:run
```

### If using Gradle:
```bash
./gradlew bootRun
```

---
