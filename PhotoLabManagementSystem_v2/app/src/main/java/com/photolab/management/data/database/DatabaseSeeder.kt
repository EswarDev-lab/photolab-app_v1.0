package com.photolab.management.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.photolab.management.utils.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Seeds a default Admin account (username: admin / password: admin123) the first time the
 * database is created, so the app is usable immediately after install. The admin should change
 * this password from Settings > User Management on first login.
 */
class DatabaseSeeder(
    private val databaseLazy: dagger.Lazy<AppDatabase>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = databaseLazy.get()
            val userDao = database.userDao()

            val salt = PasswordHasher.generateSalt()
            val hash = PasswordHasher.hash("admin123", salt)

            userDao.insert(
                com.photolab.management.data.database.entity.UserEntity(
                    username = "admin",
                    passwordHash = hash,
                    salt = salt,
                    fullName = "Administrator",
                    role = com.photolab.management.data.database.entity.UserRole.ADMIN
                )
            )

            val categoryDao = database.categoryDao()
            listOf("Photo Print", "Album", "Frame", "Canvas", "Lamination", "Accessories", "Photo Gift")
                .forEach { categoryDao.insert(com.photolab.management.data.database.entity.CategoryEntity(name = it)) }
        }
    }
}
