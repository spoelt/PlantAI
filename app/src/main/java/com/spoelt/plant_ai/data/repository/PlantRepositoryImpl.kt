package com.spoelt.plant_ai.data.repository

import android.graphics.Bitmap
import android.util.Log
import arrow.core.Either
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.spoelt.plant_ai.data.local.dao.PlantDao
import com.spoelt.plant_ai.data.local.model.toPlantList
import com.spoelt.plant_ai.data.remote.model.PlantDto
import com.spoelt.plant_ai.data.remote.model.toPlantEntity
import com.spoelt.plant_ai.domain.model.Plant
import com.spoelt.plant_ai.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val dao: PlantDao,
    private val generativeModel: GenerativeModel
) : PlantRepository {

    // returns the last three plants that were added, this cleans up the home screen display
    override suspend fun getLatestPlants(): Flow<List<Plant>> =
        dao.getLatestThreePlants().map { it.toPlantList() }

    override suspend fun identifyPlant(plantImage: Bitmap): Either<PlantAIException, String> {
        return runCatching {
            queryGemini(plantImage)?.let { plant ->
                if (plant.error != null) {
                    handlePlantError(plant.error)
                } else {
                    handlePlantDatabaseUpdate(plant)
                }
            } ?: Either.Left(PlantAIException.GeneralException)
        }.getOrElse { exception ->
            logError(exception)
            Either.Left(PlantAIException.GeneralException)
        }
    }

    private fun handlePlantError(error: String): Either<PlantAIException, String> {
        // extend as necessary
        return if (error == PLANT_NOT_FOUND) {
            Either.Left(PlantAIException.PlantNotFoundException)
        } else {
            Either.Left(PlantAIException.GeneralException)
        }
    }

    private suspend fun handlePlantDatabaseUpdate(plant: PlantDto): Either<PlantAIException, String> {
        // given that there is no UUID, check if plant exists already by querying its name.
        // this will probably lead to delays with large data sets
        dao.getPlantByName(plant.name)?.let { existingPlant ->
            return Either.Left(PlantAIException.PlantExistsAlreadyException(existingPlant.name))
        }

        return if (dao.insertPlant(plant.toPlantEntity()) != -1L) {
            Either.Right(plant.name)
        } else {
            Either.Left(PlantAIException.DatabaseInsertException)
        }
    }

    private suspend fun queryGemini(image: Bitmap): PlantDto? {
        val response = generativeModel.generateContent(
            content {
                image(image)
                text(IDENTIFY_PLANT_PROMPT)
            }
        )
        return response.text
            ?.extractJsonString()
            ?.decodeJson()
    }

    private fun String.extractJsonString(
        startChar: Char = OPEN_CURLY_BRACKET,
        endChar: Char = CLOSED_CURLY_BRACKET
    ): String? {
        val startIndex = indexOf(startChar)
        val endIndex = lastIndexOf(endChar) + 1
        return if (startIndex >= 0 && endIndex > 0) substring(startIndex, endIndex) else null
    }

    private fun String.decodeJson(): PlantDto? {
        return runCatching { Json.decodeFromString<PlantDto>(this) }
            .getOrNull()
            .also {
                if (it == null) Log.e(TAG, "Failed to decode JSON: $this")
            }
    }

    private fun logError(e: Throwable) {
        Log.e(TAG, "Error: ${e.message}", e)
    }

    companion object {
        private const val TAG = "PlantRepository"
        private const val IDENTIFY_PLANT_PROMPT = """
            I have taken a photo of a plant, and I would like you to identify it and provide 
            detailed information suitable for adding to my houseplant database. Please analyze the 
            photo and return the plant's name, optimal temperature range in °C, humidity level, light 
            requirements, fertilizer information, additional tips for taking care of it and the 
            number of times the plant should be watered per week. Format the output as a valid JSON 
            object with the following structure:
            {
                "name": "Plant Name",
                "photo": "photo_path_or_url",
                "temperature": "Optimal temperature range, e.g., 20-25°C",
                "humidity": "Optimal humidity, e.g., 40-60%",
                "light": "Light requirements, e.g., 'Medium indirect light'",
                "wateringDays": 3,
                "error": null,
                "fertilizer": "Use coffee grounds once per month",
                "additionalTips": "Water should only be applied directly to the root zone, not to 
                                   the leaf surface."
            }
            Use `null` for any values that are not available. If the plant cannot be identified, 
            return `null` for all properties except for "error" where you should return 
            "PLANT_NOT_FOUND".
            Do not escape any property names or values unnecessarily. Return the JSON object exactly 
            as specified without extra characters, escaping, or formatting.
            If there is more than one plant pictured in the image, use the one that is most
            prominently displayed. Do not display the Latin name of a plant, only the English one.
        """
        private const val OPEN_CURLY_BRACKET = '{'
        private const val CLOSED_CURLY_BRACKET = '}'
        private const val PLANT_NOT_FOUND = "PLANT_NOT_FOUND"
    }
}

sealed interface PlantAIException {
    data object PlantNotFoundException : PlantAIException
    data class PlantExistsAlreadyException(val name: String) : PlantAIException
    data object GeneralException : PlantAIException
    data object DatabaseInsertException : PlantAIException
}