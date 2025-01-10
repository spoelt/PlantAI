package com.spoelt.plant_ai.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spoelt.plant_ai.presentation.home.components.HomeScreenContent

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    openImageCapture: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // show loading shimmer when getting data

    HomeScreenContent(
        uiState = uiState,
        openImageCapture = openImageCapture
    )
}