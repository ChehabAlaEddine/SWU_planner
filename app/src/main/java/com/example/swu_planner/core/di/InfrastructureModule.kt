package com.example.swu_planner.core.di

import android.content.Context
import androidx.room.Room
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.local.AppDatabase
import com.example.swu_planner.data.local.dao.StopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {

    private const val BASE_URL = "https://api.swu.de/mobility/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideSwuMobilityApi(okHttpClient: OkHttpClient): SwuMobilityApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SwuMobilityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "swu_planner_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStopDao(database: AppDatabase): StopDao {
        return database.stopDao()
    }
}
