package com.spoelt.plant_ai.domain.repository

import android.graphics.Bitmap
import arrow.core.Either
import com.spoelt.plant_ai.data.repository.PlantAIException
import com.spoelt.plant_ai.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    suspend fun getLatestPlants(): Flow<List<Plant>>
    suspend fun identifyPlant(plantImage: Bitmap): Either<PlantAIException, String>
}