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
import com.example.kitekapp.ui.screens.ChangeClientScreen
import com.example.kitekapp.ui.screens.MainScreen
import com.example.kitekapp.ui.theme.KITEKAPPTheme
import com.example.kitekapp.data.DataStoreManager
import com.example.kitekapp.ui.screens.SettingsScreen
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.viewmodel.MyViewModelFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KITEKAPPTheme {
                val dataStoreManager = DataStoreManager(LocalContext.current)
                val viewModel: MyViewModel = viewModel(
                    factory = MyViewModelFactory(dataStoreManager)
                )

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
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                MainScreen(navController, viewModel)
                            }

                            Column(
                                modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val uriHandler = LocalUriHandler.current
                                Text(
                                    text = "Нашли ошибку — дайте знать",
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
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background),
                            ) {
                                ChangeClientScreen(
                                    navController,
                                    viewModel,
                                )
                            }
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
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background),
                            ) {
                                SettingsScreen(
                                    navController,
                                    viewModel,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
