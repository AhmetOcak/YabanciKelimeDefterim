package com.yabancikelimedefteri.core.di

import com.yabancikelimedefteri.data.repository.WordsRepositoryImpl
import com.yabancikelimedefteri.domain.repository.WordsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWordsRepository(wordsRepositoryImpl: WordsRepositoryImpl): WordsRepository
}