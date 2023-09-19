package com.yabancikelimedefteri.data.datasource.local.word

import com.yabancikelimedefteri.data.datasource.local.word.db.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.WordEntity

interface WordsLocalDataSource {

    suspend fun createCategory(categoryEntity: CategoryEntity)

    suspend fun getCategories(): List<CategoryEntity>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun updateCategoryName(categoryId: Int, newCategoryName: String)

    suspend fun createWord(wordEntity: WordEntity)

    suspend fun getAllWords(): List<WordEntity>

    suspend fun getWords(categoryIds: List<Int>): List<WordEntity>

    suspend fun deleteWord(wordId: Int)
}