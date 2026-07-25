package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    val category: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val createdByUserId: Long
)
