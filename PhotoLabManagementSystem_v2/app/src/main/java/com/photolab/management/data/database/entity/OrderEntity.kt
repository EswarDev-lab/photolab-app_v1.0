package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class OrderStatus { RECEIVED, EDITING, PRINTING, ALBUM_MAKING, FRAME_MAKING, READY, DELIVERED, CANCELLED }
enum class PaymentStatus { PENDING, PARTIALLY_PAID, PAID }

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["customerId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("customerId"), Index("orderNumber", unique = true)]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long = 0,
    val orderNumber: String,
    val customerId: Long,
    val bookingDate: Long = System.currentTimeMillis(),
    val deliveryDate: Long? = null,
    val isUrgent: Boolean = false,
    val notes: String? = null,
    val advancePayment: Double = 0.0,
    val discount: Double = 0.0,
    val gstAmount: Double = 0.0,
    val finalAmount: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val orderStatus: OrderStatus = OrderStatus.RECEIVED,
    val createdByUserId: Long
)
