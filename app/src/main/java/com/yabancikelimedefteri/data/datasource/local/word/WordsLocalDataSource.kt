package com.yabancikelimedefteri.data.datasource.local.word

import com.yabancikelimedefteri.data.datasource.local.word.db.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.WordEntity

interface WordsLocalDataSource {

    suspend fun createCategory(categoryEntity: CategoryEntity)

    suspend fun getCategories(): List<CategoryEntity>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun createWord(wordEntity: WordEntity)

    suspend fun getAllWords(): List<WordEntity>

    suspend fun getWords(categoryId: Int): List<WordEntity>

    suspend fun deleteWord(wordId: Int)
}