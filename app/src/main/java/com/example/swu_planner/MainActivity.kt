package com.example.swu_planner

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swu_planner.ui.theme.SWU_plannerTheme
import com.example.swu_planner.ui.theme.departures.DeparturesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SWU_plannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Render(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


    }
}

@Composable
fun Render(name: String, modifier: Modifier = Modifier) {

    val departuresViewModel = DeparturesViewModel()
    val departuresText = departuresViewModel.departures.collectAsState()

    departuresViewModel.departures.collectAsState().value.forEach {
        Log.d("MainActivity  --- new list- : ", it.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                Log.d("MainActivity", "Button clicked")
                departuresViewModel.loadDepartures("1")
            })
        {
            Text(text = "Get SWU departures")
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
        text = "Hello $name!",
        modifier = modifier
        )

        Text(
            text = departuresText.value.toString(),
            modifier = Modifier.width(200.dp).height(200.dp)
        )
    }



}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SWU_plannerTheme {
        Render("Android")
    }
}