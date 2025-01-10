package com.spoelt.plant_ai.presentation.home

import com.spoelt.plant_ai.domain.model.Plant

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Loaded(val plants: List<Plant>) : HomeUiState()
    data object Error : HomeUiState()
}