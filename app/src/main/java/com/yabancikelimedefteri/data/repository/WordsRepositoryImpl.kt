package com.yabancikelimedefteri.data.repository

import com.yabancikelimedefteri.data.datasource.local.WordsLocalDataSource
import com.yabancikelimedefteri.data.mapper.toCategoryEntity
import com.yabancikelimedefteri.data.mapper.toListCategory
import com.yabancikelimedefteri.data.mapper.toListWord
import com.yabancikelimedefteri.data.mapper.toWordEntity
import com.yabancikelimedefteri.domain.model.word.Category
import com.yabancikelimedefteri.domain.model.word.CategoryWithId
import com.yabancikelimedefteri.domain.model.word.Word
import com.yabancikelimedefteri.domain.model.word.WordWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val localDataSource: WordsLocalDataSource
) : WordsRepository {

    override suspend fun addCategory(category: Category) =
        localDataSource.addCategory(category.toCategoryEntity())

    override fun observeCategories(): Flow<List<CategoryWithId>> =
        localDataSource.observeCategories().map { it.toListCategory() }

    override suspend fun deleteCategory(categoryId: Int) =
        localDataSource.deleteCategory(categoryId)

    override suspend fun updateCategory(categoryId: Int, newCategoryName: String) =
        localDataSource.updateCategory(categoryId, newCategoryName)

    override suspend fun addWord(word: Word) = localDataSource.addWord(word.toWordEntity())

    override fun observeAllWords(): Flow<List<WordWithId>> =
        localDataSource.observeAllWords().map { it.toListWord() }

    override fun observeSpecificWords(categoryId: Int): Flow<List<WordWithId>> =
        localDataSource.observeSpecificWords(categoryId).map { it.toListWord() }

    override suspend fun deleteWord(wordId: Int) = localDataSource.deleteWord(wordId)

    override suspend fun getSpecificWords(categoryId: Int): List<WordWithId> =
        localDataSource.getSpecificWords(categoryId).toListWord()

    override suspend fun getAllWords(): List<WordWithId> =
        localDataSource.getAllWords().toListWord()

    override suspend fun updateWord(wordWithId: WordWithId) =
        localDataSource.updateWord(wordWithId.toWordEntity())
}