# Tournament

Spring Boot project for managing tournament groups, teams, matches, results, users, and bets.

## Requirements

- Java 17
- Maven, or the included Maven Wrapper (`./mvnw`)
- MySQL running on `localhost:3306`
- A database named `tournament`

## Project Structure

- Main source code: `src/main/java`
- Test source code: `src/test/java`
- Flyway migrations: `src/main/resources/db/migration`

## Running the Project

Run the application from the project root:

```zsh
./mvnw spring-boot:run
```

All API endpoints are exposed under the `/api` prefix.

Example request:

```zsh
curl -X GET "http://localhost:8080/api/groups" -H "Accept: application/json"
```

## Database Migrations

Flyway runs automatically when the application starts.

- Initial schema: `V1__create_tournament_schema.sql`
- Team statistics update: `V2__add_team_stats.sql`

## Unit Tests

Unit test classes must end with `Test`.

Run unit tests:

```zsh
./mvnw test
```

When running `test` or `install`, Maven executes only classes ending in `Test`.

## Integration Tests

Integration test classes must end with `IT` and are located in the `integracion` package.

These tests run with the `integration` profile and use an in-memory H2 database, so they do not modify MySQL data.

Run only integration tests:

```zsh
./mvnw verify -Pintegration-tests
```

## Code Coverage

JaCoCo runs during the `verify` phase and validates the configured minimum line coverage.

Run coverage validation:

```zsh
./mvnw verify
```

JaCoCo HTML report:

- `target/site/jacoco/index.html`

## Packaging the Application

Build the project and run unit tests:

```zsh
./mvnw install
```

