package com.yabancikelimedefteri.data.datasource.local.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yabancikelimedefteri.data.datasource.local.word.db.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.word.db.dao.WordDao
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.WordEntity

@Database(
    entities = [CategoryEntity::class, WordEntity::class],
    version = 1
)
abstract class ForeignWordsDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun wordDao(): WordDao
}