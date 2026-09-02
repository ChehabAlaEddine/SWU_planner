# SWU Planner - Ulm Public Transport Assistant

SWU Planner is a modern Android application designed to help users navigate public transport in Ulm, Germany. It provides real-time vehicle tracking, stop information, and departure schedules using the SWU Mobility API.

## 🚀 Features

- **Interactive Map**:
  - View all bus and tram stops in Ulm.
  - Track real-time vehicle positions and routes.
  - "My Location" feature to quickly center the map on your current position.
  - Interactive markers that show departure boards or vehicle passage details.
- **Stop Directory**:
  - Search and browse a comprehensive list of public transport stops.
  - Detailed view for each stop, including platform information and servicing lines.
  - **Persistent Caching**: Stops are cached locally using Room for fast loading and offline access.
- **Departures & Schedules**:
  - Real-time departure boards for any stop number.
  - Delay and deviation tracking (On-time/Late indicators).
- **Dark Mode Support**: Fully adaptive UI that respects system theme settings.

## 🛠 Tech Stack

- **Language**: Kotlin 2.4.10 (using the K2 compiler).
- **UI Framework**: Jetpack Compose with Material 3.
- **Asynchronous Programming**: Kotlin Coroutines & Flow.
- **Dependency Injection**: Hilt (Dagger) for robust and testable architecture.
- **Networking**: Retrofit 2 with GSON for API communication.
- **Local Persistence**: Room Database for offline caching.
- **Maps**: Google Maps Compose library with Play Services Location.
- **Build System**: Gradle 9.5.0 with KSP (Kotlin Symbol Processing).

## 📂 Project Structure

The project follows a **Feature-Based Architecture** for high maintainability and readability:

- `core/`: Core infrastructure, Dependency Injection (Hilt modules), Theme, and Shared Utilities.
- `data/`: Data layer containing:
  - `api/`: Retrofit interface definitions.
  - `dto/`: Data Transfer Objects for API responses.
  - `local/`: Room Database, DAOs, and Entities for caching.
  - `mapper/`: Logic to convert DTOs to clean Domain models.
  - `repository/`: Implementation of the repository pattern for data orchestration.
- `features/`: UI and business logic separated by feature:
  - `map/`: Real-time map tracking and location logic.
  - `stops/`: Stop listing and detail views.
  - `departures/`: Departure searching and schedules.
  - `common/`: Reusable UI components (Error/Empty states).

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```
2. **API Keys**:
   - Ensure you have a valid Google Maps API Key.
   - Add it to your `AndroidManifest.xml` under the `<meta-data android:name="com.google.android.geo.API_KEY" ... />` tag.
3. **Build**:
   - Open the project in Android Studio (Ladybug or newer recommended).
   - Sync Gradle and run the `:app:assembleDebug` task.
4. **Run**:
   - Deploy the app to an emulator or a physical device running Android 7.0 (API 24) or higher.

---
*Developed for the Ulm community to make public transport more accessible.*
