package com.yabancikelimedefteri.data.repository

import com.yabancikelimedefteri.data.datasource.local.word.WordsLocalDataSource
import com.yabancikelimedefteri.data.mapper.toCategoryEntity
import com.yabancikelimedefteri.data.mapper.toListCategory
import com.yabancikelimedefteri.data.mapper.toListWord
import com.yabancikelimedefteri.data.mapper.toWordEntity
import com.yabancikelimedefteri.domain.model.Category
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.Word
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.repository.WordsRepository
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(private val localDataSource: WordsLocalDataSource): WordsRepository {

    override suspend fun createCategory(category: Category) =
        localDataSource.createCategory(category.toCategoryEntity())

    override suspend fun getCategories(): List<CategoryWithId> =
        localDataSource.getCategories().toListCategory()

    override suspend fun deleteCategory(categoryId: Int) =
        localDataSource.deleteCategory(categoryId)

    override suspend fun createWord(word: Word) =
        localDataSource.createWord(word.toWordEntity())

    override suspend fun getAllWords(): List<WordWithId> =
        localDataSource.getAllWords().toListWord()

    override suspend fun getWords(categoryIds: List<Int>): List<WordWithId> =
        localDataSource.getWords(categoryIds).toListWord()

    override suspend fun deleteWord(wordId: Int) =
        localDataSource.deleteWord(wordId)
}