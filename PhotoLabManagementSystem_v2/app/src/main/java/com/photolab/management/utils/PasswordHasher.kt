package com.photolab.management.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Salted SHA-256 password hashing.
 * For a production cloud-connected build, swap this for BCrypt/Argon2 on the server side;
 * this local implementation keeps the app fully offline-capable with no external crypto deps.
 */
object PasswordHasher {

    fun generateSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(Base64.getDecoder().decode(salt))
        val hashed = digest.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hashed)
    }

    fun verify(password: String, salt: String, expectedHash: String): Boolean {
        return hash(password, salt) == expectedHash
    }
}
