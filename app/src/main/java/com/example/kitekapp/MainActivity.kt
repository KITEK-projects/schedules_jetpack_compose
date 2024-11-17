package com.example.kitekapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitekapp.ui.screens.Main
import com.example.kitekapp.ui.screens.Change
import com.example.kitekapp.ui.screens.Settings
import com.example.kitekapp.ui.theme.KITEKAPPTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val vm: MyViewModel = viewModel(
        factory = MyViewModelFactory(dataStoreManager)
    )
    val settings by vm.settings.collectAsState()
    if (settings != null) {
        LaunchedEffect(Unit) {
            if (vm.schedule.schedule.isEmpty()) {
                vm.getSchedule(settings!!.clientName, "20210411T010000+0600")
            }

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