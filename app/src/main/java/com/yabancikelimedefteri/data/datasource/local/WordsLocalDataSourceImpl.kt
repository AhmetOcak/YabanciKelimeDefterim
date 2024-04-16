package com.yabancikelimedefteri.data.datasource.local

import com.yabancikelimedefteri.data.datasource.local.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.dao.WordDao
import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordsLocalDataSourceImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val wordDao: WordDao
) : WordsLocalDataSource {

    override suspend fun addCategory(categoryEntity: CategoryEntity) =
        categoryDao.addCategory(categoryEntity)

    override fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.observeCategories()

    override suspend fun deleteCategory(categoryId: Int) = categoryDao.deleteCategory(categoryId)

    override suspend fun updateCategory(categoryId: Int, newCategoryName: String) =
        categoryDao.updateCategory(categoryId, newCategoryName)

    override suspend fun addWord(wordEntity: WordEntity) = wordDao.addWord(wordEntity)

    override fun observeAllWords(): Flow<List<WordEntity>> = wordDao.observeAllWords()

    override fun observeSpecificWords(categoryId: Int): Flow<List<WordEntity>> =
        wordDao.observeSpecificWords(categoryId)

    override suspend fun deleteWord(wordId: Int) = wordDao.deleteWord(wordId)

    override suspend fun getSpecificWords(categoryId: Int): List<WordEntity> =
        wordDao.getSpecificWords(categoryId)
}