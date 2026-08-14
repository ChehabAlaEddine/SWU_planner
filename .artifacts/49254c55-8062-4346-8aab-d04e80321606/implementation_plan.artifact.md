# Implementation Plan - Integrate Google Maps

This plan outlines the steps to replace the current map placeholder with a real interactive Google Map focused on Ulm, Germany.

## User Review Required

> [!IMPORTANT]
> **Manual Action Required**: You will need a Google Maps API Key to see the map.
> 1.  Go to the [Google Cloud Console](https://console.cloud.google.com/).
> 2.  Create a project and enable the "Maps SDK for Android".
> 3.  Create an API Key under "Credentials".
> 4.  I will provide a placeholder in `AndroidManifest.xml` where you must paste this key.

## Proposed Changes

### [Component] Dependencies

#### [MODIFY] [libs.versions.toml](file:///home/alch273107/AndroidStudioProjects/SWU_planner/gradle/libs.versions.toml)
- Add versions for `maps-compose` and `play-services-maps`.
- Add library entries for `maps-compose` and `play-services-maps`.

#### [MODIFY] [build.gradle.kts](file:///home/alch273107/AndroidStudioProjects/SWU_planner/app/build.gradle.kts)
- Add the new dependencies to the `dependencies` block.

### [Component] Android Manifest

#### [MODIFY] [AndroidManifest.xml](file:///home/alch273107/AndroidStudioProjects/SWU_planner/app/src/main/AndroidManifest.xml)
- Add `<meta-data>` tag with the API key placeholder within the `<application>` tag.
- Ensure necessary permissions (`INTERNET`, `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION`) are present.

### [Component] Map Screen UI

#### [MODIFY] [MapScreen.kt](file:///home/alch273107/AndroidStudioProjects/SWU_planner/app/src/main/java/com/example/swu_planner/composables/MapScreen.kt)
- Import `GoogleMap`, `CameraPositionState`, and `LatLng`.
- Replace the `Box` placeholder with the `GoogleMap` composable.
- Set the initial camera position to Ulm (48.3996° N, 9.9915° E).

## Verification Plan

### Automated Tests
- `gradle build` to verify dependencies are resolved and code compiles.
- `analyze_file` on `MapScreen.kt`.

### Manual Verification
- After adding the API key, verify the map renders in the app.
- Check that the camera is correctly centered on Ulm.
