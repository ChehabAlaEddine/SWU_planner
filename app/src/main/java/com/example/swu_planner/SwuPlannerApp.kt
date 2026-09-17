package com.example.swu_planner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The [Application] class for the SWU Planner app.
 *
 * This class is annotated with [HiltAndroidApp] to trigger Hilt's code generation,
 * including a base class for the application that serves as the application-level
 * dependency container.
 */
@HiltAndroidApp
class SwuPlannerApp : Application()
