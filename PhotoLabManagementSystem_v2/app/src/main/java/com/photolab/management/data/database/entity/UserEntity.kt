package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole { ADMIN, STAFF }

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val username: String,
    val passwordHash: String,
    val salt: String,
    val fullName: String,
    val role: UserRole,
    val phone: String? = null,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long? = null
)
