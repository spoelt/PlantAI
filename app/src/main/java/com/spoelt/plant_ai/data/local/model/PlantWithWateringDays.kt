package com.spoelt.plant_ai.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class PlantWithWateringDays(
    @Embedded val plant: PlantEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "plant_id"
    )
    val wateringDays: List<WateringDayEntity>
)