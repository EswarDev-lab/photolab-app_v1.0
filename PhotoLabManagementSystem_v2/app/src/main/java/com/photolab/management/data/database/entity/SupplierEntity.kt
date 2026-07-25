package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true) val supplierId: Long = 0,
    val name: String,
    val phone: String,
    val email: String? = null,
    val address: String? = null,
    val gstNumber: String? = null,
    val outstandingAmount: Double = 0.0
)
