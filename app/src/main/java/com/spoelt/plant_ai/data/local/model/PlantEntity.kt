package com.spoelt.plant_ai.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spoelt.plant_ai.domain.model.Plant
import kotlinx.serialization.Serializable

@Entity(tableName = "plants")
@Serializable
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val photo: String?,
    val temperature: String?,
    val humidity: String?,
    val light: String?,
    val fertilizer: String?,
    val additionalTips: String?,
)

fun PlantEntity.toPlant() = Plant(
    id = this.id,
    name = this.name,
    photo = this.photo,
    temperature = this.temperature,
    humidity = this.humidity,
    light = this.light,
    fertilizer = this.fertilizer,
    additionalTips = this.additionalTips
)

fun List<PlantEntity>.toPlantList() = map { it.toPlant() }