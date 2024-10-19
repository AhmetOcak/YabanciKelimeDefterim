package com.yabancikelimedefteri.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yabancikelimedefteri.data.datasource.local.dao.CategoryDao
import com.yabancikelimedefteri.data.datasource.local.dao.WordDao
import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import com.yabancikelimedefteri.data.datasource.local.entity.WordEntity

@Database(
    entities = [CategoryEntity::class, WordEntity::class],
    version = 2
)
abstract class ForeignWordsDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun wordDao(): WordDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE word_table ADD COLUMN importance_level INTEGER NOT NULL DEFAULT 0")
    }
}