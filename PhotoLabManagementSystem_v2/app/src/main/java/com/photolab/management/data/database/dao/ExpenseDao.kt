package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: ExpenseEntity): Long

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT COALESCE(SUM(amount),0) FROM expenses WHERE date BETWEEN :start AND :end")
    suspend fun totalBetween(start: Long, end: Long): Double
}
