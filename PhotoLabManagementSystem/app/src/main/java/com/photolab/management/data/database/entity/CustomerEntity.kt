package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Long = 0,
    val name: String,
    val phone: String,
    val whatsappNumber: String? = null,
    val email: String? = null,
    val address: String? = null,
    val gstNumber: String? = null,
    val dateOfBirth: Long? = null,
    val anniversary: Long? = null,
    val notes: String? = null,
    val profilePhotoPath: String? = null,
    val outstandingAmount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
