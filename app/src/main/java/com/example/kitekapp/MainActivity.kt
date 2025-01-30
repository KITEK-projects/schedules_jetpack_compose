package com.example.kitekapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitekapp.ui.screens.ChangeClientScreen
import com.example.kitekapp.ui.screens.MainScreen
import com.example.kitekapp.ui.theme.AppTheme
import com.example.kitekapp.data.DataStoreManager
import com.example.kitekapp.ui.screens.SettingsScreen
import com.example.kitekapp.ui.theme.customColors
import com.example.kitekapp.ui.theme.customTypography
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.viewmodel.MyViewModelFactory


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            )
        )
        setContent {
            AppTheme {
                val dataStoreManager = DataStoreManager(LocalContext.current)
                val viewModel: MyViewModel = viewModel(
                    factory = MyViewModelFactory(dataStoreManager)
                )

                // Перезагрузка в случае сворацивания приложения
                LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                    viewModel.responseCode = 0
                    viewModel.apiError = ""
                }


                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main",
                    modifier = Modifier
                        .background(customColors.background)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {

                    composable(
                        "main",
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { it })
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { it })
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { it })
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { it })
                        },
                    ) {
                        MainScreen(navController, viewModel)

//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(bottom = 20.dp),
//                            verticalArrangement = Arrangement.Bottom,
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            val uriHandler = LocalUriHandler.current
//                            Text(
//                                text = "Нашли ошибку — дайте знать",
//                                style = customTypography.robotoRegular12,
//                                color = customColors.secondaryTextAndIcons,
//                                textDecoration = TextDecoration.Underline,
//                                modifier = Modifier.clickable {
//                                    uriHandler.openUri("https://forms.gle/hr3SpBWYM5wDFXEL6")
//                                }
//                            )
//                        }
                    }
                    composable(
                        "change_schedule",
                        enterTransition = {
                            slideInVertically(initialOffsetY = { it })
                        },
                        exitTransition = {
                            slideOutVertically(targetOffsetY = { it })
                        },
                        popEnterTransition = {
                            slideInVertically(initialOffsetY = { it })
                        },
                        popExitTransition = {
                            slideOutVertically(targetOffsetY = { it })
                        },
                    ) {
                        ChangeClientScreen(
                            navController,
                            viewModel,
                        )
                    }
                    composable(
                        "settings",
                        enterTransition = {
                            slideInVertically(initialOffsetY = { it })
                        },
                        exitTransition = {
                            slideOutVertically(targetOffsetY = { it })
                        },
                        popEnterTransition = {
                            slideInVertically(initialOffsetY = { it })
                        },
                        popExitTransition = {
                            slideOutVertically(targetOffsetY = { it })
                        },
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
