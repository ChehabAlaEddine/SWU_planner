# Functional Use Cases - SWU Planner

This document details the user-facing functionality of the SWU Planner application.

## 1. Summary of Use Cases

| ID | Title | Priority | Description |
| :--- | :--- | :--- | :--- |
| UC-01 | View All stops | High | User browsing the list of all available stops in the Ulm transport network. |
| UC-02 | Map Tracking | High | Real-time visualization of bus and tram locations on a Google Map. |
| UC-03 | Departure Board | Medium | Detailed schedule for a specific stop with real-time delay tracking. |
| UC-04 | User Location | Medium | Centering the map on the user's current GPS coordinates. |
| UC-05 | Offline Access | Medium | Ability to see previously loaded stop information without internet. |
| UC-06 | Trip Planning | High | Searching for journeys between two points with detailed leg breakdowns. |
| UC-07 | Saved Addresses | High | Managing personal locations (Home/Work) for instant "from current location" routing. |

## 2. Detailed Data Flows

The following sequence diagrams illustrate the data flow for core functionalities.

### 2.1 Stop Loading Flow (Cache-First)
[View Data Flow Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/DataFlow.puml)

### 2.2 Vehicle Tracking Flow
[View Vehicle Tracking Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/VehicleTrackingFlow.puml)

### 2.3 Real-Time Departures Flow
[View Departures Diagram](file:///home/alch273107/AndroidStudioProjects/SWU_planner/Doc/diagrams/DeparturesFlow.puml)

## 3. UI Interactions
- **Bottom Sheets**: Used to show detailed departure information when a stop marker is clicked.
- **Floating Action Buttons**: Used for the "My Location" feature and other quick actions.
- **Route Badges**: Dynamic icons that represent tram (square) and bus (circle) lines with their official colors.
