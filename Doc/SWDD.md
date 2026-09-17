# Software Detailed Design Document (SWDD) - SWU Planner

## 1. Package Structure
The code is organized into three primary top-level packages:

- `core/`: Application-wide services like Dependency Injection (`InfrastructureModule`), custom Theme, and shared utilities.
- `data/`: The entire data layer, including the API interface, Room Database, DTOs, Mappers, and Repository implementations.
- `features/`: The UI layer and feature-specific logic, grouped by functionality (e.g., `map`, `stops`, `departures`, `routing`).

## 2. Component Design

### 2.1 Dependency Injection
Hilt is used to manage object lifetimes and dependencies. The `InfrastructureModule` provides singleton instances of the database and network clients for both SWU (Stops/Departures) and DING (Routing).

### 2.2 Local Persistence
The app uses Room to store bus stop data and user addresses.
- **StopEntity**: Stores all stop metadata.
- **SavedAddressEntity**: Persists user-defined locations (Home, Work) with custom names and icons.
- **DAO**: `StopDao` and `SavedAddressDao` provide CRUD operations.
- **Serialization**: Nested objects are serialized to JSON strings using GSON via `Converters.kt`.

### 2.3 Shared UI Components
The `routing` feature leverages a shared component architecture to ensure visual consistency:
- **TransitBadge**: A unified way to display line numbers with official colors.
- **TransitLegSummary**: Standardizes the "Icon + Badge" layout across list items and detail popups.
- **AutocompleteField**: A reusable search input with integrated stop suggestion handling.

## 3. Class Diagram

The following diagram illustrates the relationship between the main components of the application.

[View Class Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/ClassDiagram.puml)

## 4. API Integration
The `SwuMobilityApi` interface defines endpoints for:
- Retrieving stop attributes and coordinates.
- Fetching real-time departures with delay information.
- Tracking active vehicle trips and their precise GPS coordinates.
