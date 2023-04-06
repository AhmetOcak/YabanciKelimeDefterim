package com.yabancikelimedefteri.domain.repository

import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.Word
import com.yabancikelimedefteri.domain.model.WordWithId

interface WordsRepository {

    suspend fun createCategory(category: Category)

    suspend fun getCategories(): List<CategoryWithId>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun createWord(word: Word)

    suspend fun getAllWords(): List<WordWithId>

    suspend fun getWords(categoryIds: List<Int>): List<WordWithId>

    suspend fun deleteWord(wordId: Int)
}