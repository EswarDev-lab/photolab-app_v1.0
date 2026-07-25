package com.photolab.management.data.repository

import android.content.Context
import android.net.Uri
import androidx.hilt.android.qualifiers.ApplicationContext
import com.photolab.management.data.database.dao.CompanySettingsDao
import com.photolab.management.data.database.entity.CompanySettingsEntity
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val companySettingsDao: CompanySettingsDao,
    @ApplicationContext private val context: Context
) {
    fun observeSettings() = companySettingsDao.observe()

    suspend fun getSettings(): CompanySettingsEntity =
        companySettingsDao.get() ?: CompanySettingsEntity()

    suspend fun saveSettings(settings: CompanySettingsEntity) = companySettingsDao.upsert(settings)

    /** Copies the picked logo image into app-private storage and returns its stable file path. */
    fun persistLogo(sourceUri: Uri): String {
        val logosDir = File(context.filesDir, "logos").apply { mkdirs() }
        val destFile = File(logosDir, "company_logo.png")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destFile.outputStream().use { output -> input.copyTo(output) }
        }
        return destFile.absolutePath
    }
}
