package com.spoelt.plant_ai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spoelt.plant_ai.presentation.home.HomeScreen
import com.spoelt.plant_ai.presentation.imagecapture.ImageCaptureScreen
import com.spoelt.plant_ai.presentation.navigation.Home
import com.spoelt.plant_ai.presentation.navigation.ImageCapture
import com.spoelt.plant_ai.presentation.theme.PlantAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel by viewModels<MainViewModel>()

            PlantAITheme {
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        HomeScreen(
                            openImageCapture = {
                                navController.navigate(ImageCapture)
                            }
                        )
                    }

                    composable<ImageCapture> {
                        ImageCaptureScreen(
                            onPermissionDenied = {
                                navController.navigate(Home)
                                // add parameter to Home to display Snackbar in Home Scaffold
                            },
                            onIdentificationSuccess = {
                                navController.navigate(Home)
                            }
                        )
                    }
                }
            }
        }
    }
}