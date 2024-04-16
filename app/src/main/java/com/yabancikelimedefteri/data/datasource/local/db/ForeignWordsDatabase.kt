package com.yabancikelimedefteri.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yabancikelimedefteri.data.datasource.local.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.dao.WordDao
import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity

@Database(
    entities = [CategoryEntity::class, WordEntity::class],
    version = 1
)
abstract class ForeignWordsDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun wordDao(): WordDao
}