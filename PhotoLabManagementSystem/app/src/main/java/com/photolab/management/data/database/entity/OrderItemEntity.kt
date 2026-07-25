package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(entity = OrderEntity::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ProductEntity::class, parentColumns = ["productId"], childColumns = ["productId"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index("orderId"), Index("productId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val orderItemId: Long = 0,
    val orderId: Long,
    val productId: Long,
    val description: String? = null,
    val size: String? = null,
    val quantity: Double,
    val rate: Double,
    val discount: Double = 0.0,
    val gstPercent: Double = 0.0,
    val lineTotal: Double,
    val referenceImagePath: String? = null
)
