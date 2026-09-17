package com.example.swu_planner.core.di

import android.content.Context
import androidx.room.Room
import com.example.swu_planner.data.api.RoutingApi
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.local.AppDatabase
import com.example.swu_planner.data.local.dao.SavedAddressDao
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
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides infrastructure-related dependencies.
 *
 * This includes network clients (Retrofit, OkHttpClient) and local storage (Room Database, DAOs).
 */
@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {

    private const val SWU_BASE_URL = "https://api.swu.de/mobility/v1/"
    private const val DING_BASE_URL = "https://www.ding.eu/ding3/"

    /**
     * Provides a singleton [OkHttpClient] with logging and timeout configurations.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides a singleton [Retrofit] instance configured for the SWU mobility API.
     */
    @Provides
    @Singleton
    @Named("SWU")
    fun provideSwuRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SWU_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides a singleton [Retrofit] instance configured for the DING routing API.
     */
    @Provides
    @Singleton
    @Named("Routing")
    fun provideRoutingRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DING_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides the [SwuMobilityApi] service implementation.
     */
    @Provides
    @Singleton
    fun provideSwuMobilityApi(@Named("SWU") retrofit: Retrofit): SwuMobilityApi {
        return retrofit.create(SwuMobilityApi::class.java)
    }

    /**
     * Provides the [RoutingApi] service implementation.
     */
    @Provides
    @Singleton
    fun provideRoutingApi(@Named("Routing") retrofit: Retrofit): RoutingApi {
        return retrofit.create(RoutingApi::class.java)
    }

    /**
     * Provides the singleton [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "swu_planner_db"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    /**
     * Provides the [StopDao] from the [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideStopDao(database: AppDatabase): StopDao {
        return database.stopDao()
    }

    /**
     * Provides the [SavedAddressDao] from the [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideSavedAddressDao(database: AppDatabase): SavedAddressDao {
        return database.savedAddressDao()
    }
}
