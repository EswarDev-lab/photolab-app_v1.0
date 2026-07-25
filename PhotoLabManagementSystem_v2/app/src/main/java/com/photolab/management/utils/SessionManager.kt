package com.photolab.management.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.photolab.management.data.database.entity.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session_prefs")

/**
 * Persists the logged-in user's session locally (encrypted at the OS filesystem level via
 * Android's app sandbox). Backs "Remember Login" and role-based access checks app-wide.
 */
class SessionManager(private val context: Context) {

    private object Keys {
        val USER_ID = longPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val FULL_NAME = stringPreferencesKey("full_name")
        val ROLE = stringPreferencesKey("role")
        val REMEMBER = booleanPreferencesKey("remember_login")
    }

    val userId: Flow<Long?> = context.sessionDataStore.data.map { it[Keys.USER_ID] }
    val fullName: Flow<String?> = context.sessionDataStore.data.map { it[Keys.FULL_NAME] }
    val role: Flow<UserRole?> = context.sessionDataStore.data.map { prefs ->
        prefs[Keys.ROLE]?.let { UserRole.valueOf(it) }
    }
    val isLoggedIn: Flow<Boolean> = context.sessionDataStore.data.map { it[Keys.USER_ID] != null }

    suspend fun saveSession(userId: Long, username: String, fullName: String, role: UserRole, remember: Boolean) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USERNAME] = username
            prefs[Keys.FULL_NAME] = fullName
            prefs[Keys.ROLE] = role.name
            prefs[Keys.REMEMBER] = remember
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { it.clear() }
    }
}
