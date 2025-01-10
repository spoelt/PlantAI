package com.spoelt.plant_ai.di

import com.spoelt.plant_ai.data.repository.PlantRepositoryImpl
import com.spoelt.plant_ai.domain.repository.PlantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for defining the creation of any repository dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPlantRepository(
        impl: PlantRepositoryImpl
    ): PlantRepository
}