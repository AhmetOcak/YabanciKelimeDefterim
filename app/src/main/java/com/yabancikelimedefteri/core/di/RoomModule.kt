package com.yabancikelimedefteri.core.di

import android.content.Context
import androidx.room.Room
import com.yabancikelimedefteri.data.datasource.local.db.ForeignWordsDatabase
import com.yabancikelimedefteri.data.datasource.local.db.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideCategoryDatabase(@ApplicationContext context: Context): ForeignWordsDatabase {
        return Room.databaseBuilder(
            context,
            ForeignWordsDatabase::class.java,
            "app_database"
        ).addMigrations(MIGRATION_1_2)
            .build()
    }
}