package com.yabancikelimedefteri.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_table",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = CASCADE
        )
    ]
)
data class WordEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "category_id")
    var categoryId: Int,

    @ColumnInfo(name = "foreign_word")
    var foreignWord: String,

    @ColumnInfo(name = "meaning")
    var meaning: String,

    @ColumnInfo(name = "importance_level")
    var importanceLevel: Int
)
