package com.photolab.management.data.database.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.photolab.management.data.database.entity.CompanySettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanySettingsDao {

    @Upsert
    suspend fun upsert(settings: CompanySettingsEntity)

    @Query("SELECT * FROM company_settings WHERE id = 1")
    fun observe(): Flow<CompanySettingsEntity?>

    @Query("SELECT * FROM company_settings WHERE id = 1")
    suspend fun get(): CompanySettingsEntity?
}
