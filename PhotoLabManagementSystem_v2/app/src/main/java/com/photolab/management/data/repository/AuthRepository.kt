package com.photolab.management.data.repository

import com.photolab.management.data.database.dao.UserDao
import com.photolab.management.data.database.entity.UserEntity
import com.photolab.management.data.database.entity.UserRole
import com.photolab.management.utils.PasswordHasher
import javax.inject.Inject
import javax.inject.Singleton

sealed class LoginResult {
    data class Success(val user: UserEntity) : LoginResult()
    object InvalidCredentials : LoginResult()
    object AccountDisabled : LoginResult()
}

/**
 * Repository is the single source of truth for auth. Written against the local Room DAO now;
 * when a Spring Boot backend is added, this class swaps its internals for a Retrofit/Ktor call
 * while every ViewModel that depends on it stays unchanged.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(username: String, password: String): LoginResult {
        val user = userDao.getByUsername(username.trim()) ?: return LoginResult.InvalidCredentials
        if (!PasswordHasher.verify(password, user.salt, user.passwordHash)) {
            return LoginResult.InvalidCredentials
        }
        if (!user.isEnabled) return LoginResult.AccountDisabled
        userDao.updateLastLogin(user.userId, System.currentTimeMillis())
        return LoginResult.Success(user)
    }

    suspend fun createUser(
        username: String,
        password: String,
        fullName: String,
        role: UserRole,
        phone: String? = null
    ): Long {
        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hash(password, salt)
        return userDao.insert(
            UserEntity(
                username = username.trim(),
                passwordHash = hash,
                salt = salt,
                fullName = fullName,
                role = role,
                phone = phone
            )
        )
    }

    fun getAllUsers() = userDao.getAllUsers()

    suspend fun setUserEnabled(userId: Long, enabled: Boolean) = userDao.setEnabled(userId, enabled)
}
