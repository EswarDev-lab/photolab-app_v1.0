package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchases",
    foreignKeys = [ForeignKey(entity = SupplierEntity::class, parentColumns = ["supplierId"], childColumns = ["supplierId"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index("supplierId")]
)
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true) val purchaseId: Long = 0,
    val supplierId: Long,
    val invoiceNumber: String,
    val purchaseDate: Long = System.currentTimeMillis(),
    val transportCharges: Double = 0.0,
    val discount: Double = 0.0,
    val gstAmount: Double = 0.0,
    val totalAmount: Double = 0.0
)
