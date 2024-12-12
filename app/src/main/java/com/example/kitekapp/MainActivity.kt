package com.example.kitekapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitekapp.ui.screens.Main
import com.example.kitekapp.ui.screens.Change
import com.example.kitekapp.ui.screens.Settings
import com.example.kitekapp.ui.theme.KITEKAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KITEKAPPTheme {
                Navigation()
            }
        }
    }


}

@Composable
fun Navigation() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val vm: MyViewModel = viewModel(
        factory = MyViewModelFactory(dataStoreManager)
    )
    val settings by vm.settings.collectAsState()
    if (settings != null) {
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
        LaunchedEffect(lifecycleState) {
            vm.schedule = Schedules()
            if (settings!!.clientName != "")
                vm.getSchedule(settings!!.clientName)
            vm.updateSelectLessonDuration(settings!!.selectedLessonDuration)
        }
    }



    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "main") {

            composable(
                "main",
                enterTransition = {
                    slideInHorizontally(initialOffsetX = {it})
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = {it})
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = {it})
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = {it})
                },
            ) {
                Main(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    navController,
                    vm,
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val uriHandler = LocalUriHandler.current
                    Text(
                        text = "Если вы обнаружили баг, дайте знать",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://forms.gle/hr3SpBWYM5wDFXEL6")
                        }
                    )
                }
            }
            composable("change_schedule",
                enterTransition = {
                    slideInVertically(initialOffsetY = { it })
                },
                exitTransition = {
                    slideOutVertically(targetOffsetY = {it})
                },
                popEnterTransition = {
                    slideInVertically(initialOffsetY = {it})
                },
                popExitTransition = {
                    slideOutVertically(targetOffsetY = {it})
                },
                ) {
                Change(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    navController,
                    vm,
                    settings
                )
            }
            composable("settings",
                enterTransition = {
                    slideInVertically(initialOffsetY = {it})
                },
                exitTransition = {
                    slideOutVertically(targetOffsetY = {it})
                },
                popEnterTransition = {
                    slideInVertically(initialOffsetY = {it})
                },
                popExitTransition = {
                    slideOutVertically(targetOffsetY = {it})
                },
                ) {
                Settings(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    navController,
                    vm,
                    settings
                )
            }
        }
    }
}