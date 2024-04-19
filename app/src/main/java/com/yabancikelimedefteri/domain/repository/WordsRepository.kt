package com.yabancikelimedefteri.domain.repository

import com.yabancikelimedefteri.domain.model.word.Category
import com.yabancikelimedefteri.domain.model.word.CategoryWithId
import com.yabancikelimedefteri.domain.model.word.Word
import com.yabancikelimedefteri.domain.model.word.WordWithId
import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    suspend fun addCategory(category: Category)

    fun observeCategories(): Flow<List<CategoryWithId>>

    suspend fun deleteCategory(categoryId: Int)

    suspend fun updateCategory(categoryId: Int, newCategoryName: String)

    suspend fun addWord(word: Word)

    fun observeAllWords(): Flow<List<WordWithId>>

    fun observeSpecificWords(categoryId: Int): Flow<List<WordWithId>>

    suspend fun deleteWord(wordId: Int)

    suspend fun getSpecificWords(categoryId: Int): List<WordWithId>
}