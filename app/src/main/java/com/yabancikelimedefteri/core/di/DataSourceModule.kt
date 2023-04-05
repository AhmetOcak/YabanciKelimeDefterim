package com.yabancikelimedefteri.core.di

import com.yabancikelimedefteri.data.datasource.local.word.WordsLocalDataSource
import com.yabancikelimedefteri.data.datasource.local.word.WordsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWordsDataSource(wordsLocalDataSourceImpl: WordsLocalDataSourceImpl): WordsLocalDataSource
}