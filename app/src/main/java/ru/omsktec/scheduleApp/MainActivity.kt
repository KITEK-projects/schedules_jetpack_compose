package ru.omsktec.scheduleApp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import ru.omsktec.scheduleApp.ui.screens.ChangeClientScreen
import ru.omsktec.scheduleApp.ui.screens.MainScreen
import ru.omsktec.scheduleApp.ui.theme.AppTheme
import ru.omsktec.scheduleApp.data.DataStoreManager
import ru.omsktec.scheduleApp.ui.screens.SettingsScreen
import ru.omsktec.scheduleApp.ui.theme.customColors
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import ru.omsktec.scheduleApp.viewmodel.MyViewModelFactory


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Firebase.messaging.subscribeToTopic("update")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FCM", "Подписка на тему успешна")
                        } else {
                            Log.e("FCM", "Ошибка подписки на тему")
                        }
                    }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                            1001
                        )
                    }
                }

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
