package com.yabancikelimedefteri.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yabancikelimedefteri.data.datasource.local.WordsLocalDataSource
import com.yabancikelimedefteri.data.repository.UserPreferencesRepositoryImpl
import com.yabancikelimedefteri.data.repository.WordsRepositoryImpl
import com.yabancikelimedefteri.domain.repository.UserPreferencesRepository
import com.yabancikelimedefteri.domain.repository.WordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWordsRepository(wordsLocalDataSource: WordsLocalDataSource): WordsRepository {
        return WordsRepositoryImpl(wordsLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }
}