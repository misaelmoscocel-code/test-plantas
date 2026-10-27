package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.PlantDatabase
import com.example.data.PlantRepository
import com.example.ui.screens.BotanicalGuideScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.DiagnosisScreen
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MintPale
import com.example.ui.theme.PlantCareTheme
import com.example.ui.theme.SageGreen
import com.example.viewmodel.PlantCareViewModel
import com.example.viewmodel.PlantCareViewModelFactory

enum class AppScreen(val title: String) {
    DIAGNOSIS("Diagnóstico"),
    CALENDAR("Mi Jardín"),
    GUIDES("Guías")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase App Check initialization as mandated by the skill
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        val database = PlantDatabase.getInstance(applicationContext)
        val repository = PlantRepository(database.plantDao())
        val viewModelFactory = PlantCareViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[PlantCareViewModel::class.java]

        setContent {
            PlantCareTheme {
                MainPlantApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainPlantApp(viewModel: PlantCareViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.DIAGNOSIS) }

    // Predictive back handler when in secondary screens
    if (currentScreen != AppScreen.DIAGNOSIS) {
        BackHandler {
            currentScreen = AppScreen.DIAGNOSIS
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.DIAGNOSIS,
                    onClick = { currentScreen = AppScreen.DIAGNOSIS },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Diagnóstico",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Diagnóstico") },
                    modifier = Modifier.testTag("nav_diagnosis"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldPrimary,
                        indicatorColor = MintPale
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.CALENDAR,
                    onClick = { currentScreen = AppScreen.CALENDAR },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Mi Jardín y Riegos",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Mi Jardín") },
                    modifier = Modifier.testTag("nav_calendar"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldPrimary,
                        indicatorColor = MintPale
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.GUIDES,
                    onClick = { currentScreen = AppScreen.GUIDES },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Guías Botánicas",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Guías") },
                    modifier = Modifier.testTag("nav_guides"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldPrimary,
                        indicatorColor = MintPale
                    )
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                AppScreen.DIAGNOSIS -> DiagnosisScreen(viewModel = viewModel)
                AppScreen.CALENDAR -> CalendarScreen(viewModel = viewModel)
                AppScreen.GUIDES -> BotanicalGuideScreen()
            }
        }
    }
}
