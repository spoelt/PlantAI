package com.spoelt.plant_ai.di

import android.content.Context
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.spoelt.plant_ai.BuildConfig
import com.spoelt.plant_ai.data.local.dao.PlantDao
import com.spoelt.plant_ai.data.local.database.PlantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module is responsible for defining the creation of any dependencies used in the
 * data module, e.g. dao, database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePlantDatabase(
        @ApplicationContext applicationContext: Context,
    ): PlantDatabase {
        return Room.databaseBuilder(
            applicationContext,
            PlantDatabase::class.java,
            "plant-database.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideTickerDAO(
        database: PlantDatabase,
    ): PlantDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )
}