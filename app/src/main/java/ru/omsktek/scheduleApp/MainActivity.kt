package ru.omsktek.scheduleApp

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.omsktek.scheduleApp.ui.screens.ChangeClientScreen
import ru.omsktek.scheduleApp.ui.screens.MainScreen
import ru.omsktek.scheduleApp.ui.theme.AppTheme
import ru.omsktek.scheduleApp.data.DataStoreManager
import ru.omsktek.scheduleApp.ui.screens.SettingsScreen
import ru.omsktek.scheduleApp.ui.theme.customColors
import ru.omsktek.scheduleApp.viewmodel.MyViewModel
import ru.omsktek.scheduleApp.viewmodel.MyViewModelFactory


class MainActivity : ComponentActivity() {



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
