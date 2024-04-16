package com.yabancikelimedefteri.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yabancikelimedefteri.data.datasource.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun addCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM category_table")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category_table WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)

    @Query("UPDATE category_table SET category_name = :newCategoryName WHERE id = :categoryId")
    suspend fun updateCategory(categoryId: Int, newCategoryName: String)
}