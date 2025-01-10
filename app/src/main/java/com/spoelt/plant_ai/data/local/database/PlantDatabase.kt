package com.spoelt.plant_ai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spoelt.plant_ai.data.local.dao.PlantDao
import com.spoelt.plant_ai.data.local.model.PlantEntity
import com.spoelt.plant_ai.data.local.model.WateringDayEntity

@Database(
    entities = [
        PlantEntity::class,
        WateringDayEntity::class
    ],
    version = 1
)
abstract class PlantDatabase : RoomDatabase() {
    abstract val dao: PlantDao
}