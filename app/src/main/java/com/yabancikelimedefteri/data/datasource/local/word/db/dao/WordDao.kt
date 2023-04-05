package com.yabancikelimedefteri.data.datasource.local.word.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.WordEntity

@Dao
interface WordDao {

    @Insert
    suspend fun createWord(wordEntity: WordEntity)

    @Query("SELECT * FROM word_table")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT * FROM word_table WHERE category_id = :categoryId")
    suspend fun getWords(categoryId: Int): List<WordEntity>

    @Query("DELETE FROM word_table WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)
}