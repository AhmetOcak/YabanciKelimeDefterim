package com.yabancikelimedefteri.core.di

import com.yabancikelimedefteri.data.datasource.local.db.room.ForeignWordsDatabase
import com.yabancikelimedefteri.data.datasource.local.word.db.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.word.db.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun provideCategoryDao(foreignWordsDatabase: ForeignWordsDatabase): CategoryDao =
        foreignWordsDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideWordDao(foreignWordsDatabase: ForeignWordsDatabase): WordDao =
        foreignWordsDatabase.wordDao()
}