package com.spoelt.plant_ai.data.remote.model

import com.spoelt.plant_ai.data.local.model.PlantEntity
import kotlinx.serialization.Serializable

@Serializable
data class PlantDto(
    val name: String,
    val photo: String?,
    val temperature: String?,
    val humidity: String?,
    val light: String?,
    val wateringDays: Int?,
    val fertilizer: String? = null,
    val additionalTips: String? = null,
    val error: String? = null
)

fun PlantDto.toPlantEntity() = PlantEntity(
    name = this.name,
    photo = this.photo,
    temperature = this.temperature,
    humidity = this.humidity,
    light = this.light,
    fertilizer = this.fertilizer,
    additionalTips = this.additionalTips
)
