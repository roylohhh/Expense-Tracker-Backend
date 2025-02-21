# Expense Tracker Application Backend with Spring Boot
## Project Overview
This project is designed to demonstrate my understanding of backend development, DevOps, and cloud computing by building a backend web API using Spring Boot and Java. 
It follows best practices for software development, CI/CD automation, and cloud deployment.

### Key Features:
- Backend API Development: Built with Spring Boot and Java, following industry best practices.
- Continuous Integration (CI): Automated unit and integration tests using GitHub Actions.
- Continuous Deployment (CD): The application is containerized and pushed to Amazon ECR upon successful tests.
- Infrastructure as Code (IaC): A separate repository contains Terraform configurations for deploying the application to AWS: https://github.com/roylohhh/Expense-Tracker-Infra

By working on this project, I aim to strengthen my expertise in backend engineering, cloud infrastructure, and DevOps automation.

## Application Overview
This backend API follows RESTful principles and is built using Spring Boot and Java, with Gradle as the build tool. 
It manages users and their expenses, enforcing a one-to-many relationship where each user can have multiple expenses.

### Key Concepts:
Gradle for Build & Dependency Management:
- The project uses Gradle to manage dependencies, build automation, and testing workflows efficiently.

Stateless Authentication:
- Authentication is handled using JWT tokens. 
When a user logs in, a JWT token is generated and must be included in subsequent API requests as a Bearer token.

Authorization & Security:
- The token contains the username and is validated before processing requests.
Users can only access their own expenses—attempting to access another user's data is forbidden.

Entity Relationships:
- One-to-Many: A User can have multiple Expenses.
The API ensures proper ownership validation before allowing any expense-related actions.
This design ensures security, scalability, and maintainability, following best practices in backend development, authentication, and build automation.

### Setting up PostGreSQL database locally in a docker container
Most of the development and testing was conducted locally, below are instructions to set up a PostGreSQl database for local development and testing:

1. Pull docker image:
   ```docker pull postgres```

2. Create and run PostGreSQL container locally:
   ```docker run --name my-postgres -e POSTGRES_USER=yourusername -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=yourdatabase -p 5432:5432 -d postgres```

3. Verify the Container is Running:
   ```docker ps```

4. Connect to PostGreSQL:
   ```psql -h localhost -U yourusername -d yourdatabase```


## CI Pipeline
### Workflow Overview
The pipeline is implemented using GitHub Actions and automates unit testing and integration testing of the application.
The pipeline runs automatically on push and pull requests to specific branches. The pipeline consists of two jobs:

#### Unit Testing
Runs on every push and pull request to `main`, `develop`, `release/*`, and `feature/*` branches.

Steps:

1. Checkout the code.
2. Set up JDK 23.
3. Run unit tests using Gradle.

#### Integration Testing
Runs only on develop and release branches. It sets up a PostGreSQl service for database-related tests.

Steps:

1. Checkout the code.
2. Set up JDK 23.
3. Start a PostGreSQl database.
4. Run integration tests.


