package com.yabancikelimedefteri.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert
    suspend fun addWord(wordEntity: WordEntity)

    @Query("SELECT * FROM word_table")
    fun observeAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM word_table WHERE category_id = :categoryId")
    fun observeSpecificWords(categoryId: Int): Flow<List<WordEntity>>

    @Query("SELECT * FROM word_table WHERE category_id = :categoryId")
    suspend fun getSpecificWords(categoryId: Int): List<WordEntity>

    @Query("DELETE FROM word_table WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)
}