package com.yabancikelimedefteri.data.datasource.local.word

import com.yabancikelimedefteri.data.datasource.local.word.db.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.word.db.dao.WordDao
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.WordEntity
import javax.inject.Inject

class WordsLocalDataSourceImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val wordDao: WordDao
) : WordsLocalDataSource {
    override suspend fun createCategory(categoryEntity: CategoryEntity) =
        categoryDao.createCategory(categoryEntity)

    override suspend fun getCategories(): List<CategoryEntity> =
        categoryDao.getCategories()

    override suspend fun deleteCategory(categoryId: Int) =
        categoryDao.deleteCategory(categoryId)

    override suspend fun updateCategoryName(categoryId: Int, newCategoryName: String) =
        categoryDao.updateCategoryName(categoryId, newCategoryName)

    override suspend fun createWord(wordEntity: WordEntity) =
        wordDao.createWord(wordEntity)

    override suspend fun getAllWords(): List<WordEntity> =
        wordDao.getAllWords()

    override suspend fun getWords(categoryIds: List<Int>): List<WordEntity> {
        val words: MutableList<WordEntity> = mutableListOf()

        categoryIds.forEach {
            words += wordDao.getWords(it).toMutableList()
        }

        return words
    }

    override suspend fun deleteWord(wordId: Int) =
        wordDao.deleteWord(wordId)
}