package com.example.kitekapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitekapp.ui.screens.Main
import com.example.kitekapp.ui.screens.Change
import com.example.kitekapp.ui.screens.Settings
import com.example.kitekapp.ui.theme.KITEKAPPTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KITEKAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {

                        composable("main") {
                            Main(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background),
                                navController
                            )
                        }
                        composable("change_schedule") {
                            Change(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background),
                                navController
                            )
                        }
                        composable("settings") {
                            Settings(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background),
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}