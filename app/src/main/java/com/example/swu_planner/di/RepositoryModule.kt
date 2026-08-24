package com.example.swu_planner.di

import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.repository.DeparturesRepository
import com.example.swu_planner.data.repository.StopsRepository
import com.example.swu_planner.data.repository.VehicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideStopsRepository(api: SwuMobilityApi, stopDao: StopDao): StopsRepository {
        return StopsRepository(api, stopDao)
    }

    @Provides
    @Singleton
    fun provideDeparturesRepository(api: SwuMobilityApi): DeparturesRepository {
        return DeparturesRepository(api)
    }

    @Provides
    @Singleton
    fun provideVehicleRepository(api: SwuMobilityApi): VehicleRepository {
        return VehicleRepository(api)
    }
}
