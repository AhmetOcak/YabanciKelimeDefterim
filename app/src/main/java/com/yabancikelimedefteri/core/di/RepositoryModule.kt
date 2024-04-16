package com.yabancikelimedefteri.core.di

import com.yabancikelimedefteri.data.datasource.local.WordsLocalDataSource
import com.yabancikelimedefteri.data.repository.WordsRepositoryImpl
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
}