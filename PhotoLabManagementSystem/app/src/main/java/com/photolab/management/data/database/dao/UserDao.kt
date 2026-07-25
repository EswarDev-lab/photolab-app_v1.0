package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun delete(userId: Long)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY fullName ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET lastLoginAt = :timestamp WHERE userId = :userId")
    suspend fun updateLastLogin(userId: Long, timestamp: Long)

    @Query("UPDATE users SET isEnabled = :enabled WHERE userId = :userId")
    suspend fun setEnabled(userId: Long, enabled: Boolean)
}
