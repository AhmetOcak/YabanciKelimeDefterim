package com.yabancikelimedefteri.core.di

import com.yabancikelimedefteri.data.datasource.local.WordsLocalDataSource
import com.yabancikelimedefteri.data.datasource.local.WordsLocalDataSourceImpl
import com.yabancikelimedefteri.data.datasource.local.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideWordsLocalDataSource(
        wordDao: WordDao,
        categoryDao: CategoryDao
    ): WordsLocalDataSource {
        return WordsLocalDataSourceImpl(categoryDao, wordDao)
    }
}