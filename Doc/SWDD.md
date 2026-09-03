# Software Detailed Design Document (SWDD) - SWU Planner

## 1. Package Structure
The code is organized into three primary top-level packages:

- `core/`: Application-wide services like Dependency Injection (`InfrastructureModule`), custom Theme, and shared utilities.
- `data/`: The entire data layer, including the API interface, Room Database, DTOs, Mappers, and Repository implementations.
- `features/`: The UI layer and feature-specific logic, grouped by functionality (e.g., `map`, `stops`, `departures`).

## 2. Component Design

### 2.1 Dependency Injection
Hilt is used to manage object lifetimes and dependencies. The `InfrastructureModule` provides singleton instances of the database and network client.

### 2.2 Local Persistence
The app uses Room to store bus stop data.
- **Entity**: `StopEntity` stores all stop metadata.
- **DAO**: `StopDao` provides CRUD operations.
- **Serialization**: Nested objects (Coordinates, StopPoints) are serialized to JSON strings using GSON via `Converters.kt`.

## 3. Class Diagram

The following diagram illustrates the relationship between the main components of the application.

[View Class Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/ClassDiagram.puml)

## 4. API Integration
The `SwuMobilityApi` interface defines endpoints for:
- Retrieving stop attributes and coordinates.
- Fetching real-time departures with delay information.
- Tracking active vehicle trips and their precise GPS coordinates.
