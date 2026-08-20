package com.example.swu_planner

import android.os.Bundle
import com.google.android.gms.maps.MapsInitializer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.swu_planner.composables.DeparturesScreen
import com.example.swu_planner.composables.MapScreen
import com.example.swu_planner.composables.StopsScreen
import com.example.swu_planner.data.api.SwuApiClient
import com.example.swu_planner.data.repository.DeparturesRepository
import com.example.swu_planner.data.repository.StopsRepository
import com.example.swu_planner.data.repository.VehicleRepository
import com.example.swu_planner.ui.theme.SWU_plannerTheme
import com.example.swu_planner.ui.departures.DeparturesViewModel
import com.example.swu_planner.ui.stops.StopsViewModel
import com.example.swu_planner.ui.vehicles.VehicleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            SWU_plannerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("map") }

    // Create dependencies manually (bottom-up) and remember them
    val departuresViewModel = remember {
        val api = SwuApiClient.api
        val departuresRepository = DeparturesRepository(api)
        DeparturesViewModel(departuresRepository)
    }
    val stopsViewModel = remember {
        val api = SwuApiClient.api
        val stopsRepository = StopsRepository(api)
        StopsViewModel(stopsRepository)
    }
    val vehicleViewModel = remember {
        val api = SwuApiClient.api
        val vehicleRepository = VehicleRepository(api)
        VehicleViewModel(vehicleRepository)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == "departures",
                    onClick = { currentScreen = "departures" },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Departures") },
                    label = { Text("Departures") }
                )
                NavigationBarItem(
                    selected = currentScreen == "stops",
                    onClick = { currentScreen = "stops" },
                    icon = { Icon(Icons.Default.Place, contentDescription = "Stops") },
                    label = { Text("Stops") }
                )
                NavigationBarItem(
                    selected = currentScreen == "map",
                    onClick = { currentScreen = "map" },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                    label = { Text("Map") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "departures" -> DeparturesScreen(viewModel = departuresViewModel)
                "stops" -> StopsScreen(viewModel = stopsViewModel)
                "map" -> MapScreen(
                    stopsViewModel = stopsViewModel,
                    departuresViewModel = departuresViewModel,
                    vehicleViewModel = vehicleViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SWU_plannerTheme {
        MainScreen()
    }
}
