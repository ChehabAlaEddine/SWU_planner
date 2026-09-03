# Software Architecture Document (SWAD) - SWU Planner

## 1. High-Level Overview
The SWU Planner is an Android application designed for public transport users in Ulm. It leverages real-time data from the SWU Mobility API and provides an offline-first experience for stop information.

## 2. Tech Stack
- **Kotlin 2.4.10 (K2)**: Modern language features and performance.
- **Jetpack Compose**: Declarative UI development.
- **Hilt**: Standardized Dependency Injection.
- **Room**: SQLite persistence for local caching.
- **Retrofit**: Type-safe REST client for network calls.
- **Google Maps Compose**: Map visualization and interaction.

## 3. System Architecture

The application is built using a feature-based organization combined with the MVVM (Model-View-ViewModel) pattern.

[View Architecture Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/Architecture.puml)

## 4. Data Strategy
The application prioritizes local data for bus stops to ensure immediate UI feedback and offline support.

- **Cache-First**: The `StopsRepository` always attempts to read from the local Room database first. 
- **Network Fallback**: If the local database is empty, the repository triggers a network call to the SWU API.
- **Background Sync**: Real-time vehicle positions are fetched periodically (every 15 seconds) but only when the UI is in the foreground (`STARTED` state).
