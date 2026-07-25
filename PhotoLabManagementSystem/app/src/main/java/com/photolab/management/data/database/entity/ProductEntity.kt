package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("barcode", unique = true), Index("productCode", unique = true)]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val productCode: String,
    val barcode: String? = null,
    val name: String,
    val categoryId: Long?,
    val purchasePrice: Double,
    val sellingPrice: Double,
    val gstPercent: Double = 0.0,
    val currentStock: Double = 0.0,
    val minimumStock: Double = 0.0,
    val maximumStock: Double = 0.0,
    val unit: String = "PCS",
    val description: String? = null,
    val imagePath: String? = null,
    val supplierId: Long? = null,
    val brand: String? = null,
    val isActive: Boolean = true
)
