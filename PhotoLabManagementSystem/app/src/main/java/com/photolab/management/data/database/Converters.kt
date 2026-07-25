package com.photolab.management.data.database

import androidx.room.TypeConverter
import com.photolab.management.data.database.entity.*

class Converters {
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name
    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name
    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name
    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = PaymentStatus.valueOf(value)

    @TypeConverter
    fun fromPaymentMode(mode: PaymentMode): String = mode.name
    @TypeConverter
    fun toPaymentMode(value: String): PaymentMode = PaymentMode.valueOf(value)

    @TypeConverter
    fun fromStockTransactionType(type: StockTransactionType): String = type.name
    @TypeConverter
    fun toStockTransactionType(value: String): StockTransactionType = StockTransactionType.valueOf(value)
}
