package com.yabancikelimedefteri.data.datasource.local

import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

interface WordsLocalDataSource {

    suspend fun addCategory(categoryEntity: CategoryEntity)

    fun observeCategories(): Flow<List<CategoryEntity>>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun updateCategory(categoryId: Int, newCategoryName: String)

    suspend fun addWord(wordEntity: WordEntity)

    fun observeAllWords(): Flow<List<WordEntity>>

    fun observeSpecificWords(categoryId: Int): Flow<List<WordEntity>>

    suspend fun deleteWord(wordId: Int)

    suspend fun getSpecificWords(categoryId: Int): List<WordEntity>

    suspend fun getAllWords(): List<WordEntity>

    suspend fun updateWord(wordEntity: WordEntity)
}