package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Insert
    suspend fun insert(supplier: SupplierEntity): Long

    @Update
    suspend fun update(supplier: SupplierEntity)

    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    fun getAll(): Flow<List<SupplierEntity>>
}
