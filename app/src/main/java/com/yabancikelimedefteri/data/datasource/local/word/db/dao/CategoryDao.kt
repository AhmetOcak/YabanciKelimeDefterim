package com.yabancikelimedefteri.data.datasource.local.word.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yabancikelimedefteri.data.datasource.local.word.db.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    suspend fun createCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM category_table")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("DELETE FROM category_table WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)

    @Query("UPDATE category_table SET category_name = :newCategoryName WHERE id = :categoryId")
    suspend fun updateCategoryName(categoryId: Int, newCategoryName: String)
}