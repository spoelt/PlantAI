package com.spoelt.plant_ai.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spoelt.plant_ai.R
import com.spoelt.plant_ai.domain.model.Plant
import com.spoelt.plant_ai.presentation.home.HomeUiState
import com.spoelt.plant_ai.presentation.theme.PlantAITheme

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    openImageCapture: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding(),
                    )
                    .padding(16.dp),
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = stringResource(R.string.my_plants_title),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        if (uiState is HomeUiState.Loaded) {
            items(uiState.plants) { plant ->
                PlantListItem(
                    plant = plant,
                    onClick = { /*TODO*/ }
                )
            }
        }

        item {
            Button(onClick = openImageCapture) {
                Text("Add plant")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentLoadingPreview() {
    PlantAITheme {
        HomeScreenContent(
            uiState = HomeUiState.Loading,
            openImageCapture = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentLoadedDataPreview() {
    PlantAITheme {
        HomeScreenContent(
            uiState = HomeUiState.Loaded(
                plants = (1..3).map {
                    Plant(
                        id = it,
                        name = "Plant $it",
                        temperature = null,
                        photo = null,
                        humidity = null,
                        light = null,
                        fertilizer = null,
                        additionalTips = null
                    )
                }
            ),
            openImageCapture = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentLoadedDataEmptyPreview() {
    PlantAITheme {
        HomeScreenContent(
            uiState = HomeUiState.Loaded(emptyList()),
            openImageCapture = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenContentErrorPreview() {
    PlantAITheme {
        HomeScreenContent(
            uiState = HomeUiState.Error,
            openImageCapture = {}
        )
    }
}