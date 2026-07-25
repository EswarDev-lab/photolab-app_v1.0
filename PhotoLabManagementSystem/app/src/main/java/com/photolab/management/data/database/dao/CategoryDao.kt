package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: CategoryEntity): Long

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): Flow<List<CategoryEntity>>
}
