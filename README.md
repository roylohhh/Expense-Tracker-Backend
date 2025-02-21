# Expense Tracker Application Backend with Spring Boot

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


## Setting up PostGreSQL database locally in a docker container
Below are instructions to set up a PostGreSQl database for local development and testing:

1. Pull docker image:
   ```docker pull postgres```

2. Create and run PostGreSQL container locally:
   ```docker run --name my-postgres -e POSTGRES_USER=yourusername -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=yourdatabase -p 5432:5432 -d postgres```

3. Verify the Container is Running:
   ```docker ps```

4. Connect to PostGreSQL:
   ```psql -h localhost -U yourusername -d yourdatabase```