package com.leishmaniapp.infrastructure.module

import android.content.Context
import androidx.room.Room
import com.leishmaniapp.persistance.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provide dependency injection for the application database
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationDatabaseModule {

    /**
     * Provides [ApplicationDatabase] implementation
     */
    @Provides
    @Singleton
    fun providerApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, "database").build()
}