package com.spoelt.plant_ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.spoelt.plant_ai.data.local.model.PlantEntity
import com.spoelt.plant_ai.data.local.model.PlantWithWateringDays
import com.spoelt.plant_ai.data.local.model.WateringDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Query("SELECT * FROM plants WHERE name = :name")
    suspend fun getPlantByName(name: String): PlantEntity?

    @Query("SELECT * FROM plants ORDER BY id DESC LIMIT 3")
    fun getLatestThreePlants(): Flow<List<PlantEntity>>

    @Upsert
    fun upsertWateringDay(wateringDay: WateringDayEntity)

    @Query("SELECT * FROM watering_days WHERE plant_id = :plantId")
    fun getWateringDaysForPlant(plantId: Int): List<WateringDayEntity>

    @Transaction
    @Query("SELECT * FROM plants")
    fun getPlantsWithWateringDays(): List<PlantWithWateringDays>
}