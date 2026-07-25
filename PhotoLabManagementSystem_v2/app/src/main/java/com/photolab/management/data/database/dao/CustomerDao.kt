package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert
    suspend fun insert(customer: CustomerEntity): Long

    @Update
    suspend fun update(customer: CustomerEntity)

    @Query("UPDATE customers SET isDeleted = 1 WHERE customerId = :customerId")
    suspend fun softDelete(customerId: Long)

    @Query("SELECT * FROM customers WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("""SELECT * FROM customers WHERE isDeleted = 0 AND 
        (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%') ORDER BY name ASC""")
    fun search(query: String): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE customerId = :customerId")
    suspend fun getById(customerId: Long): CustomerEntity?

    @Query("SELECT COUNT(*) FROM customers WHERE phone = :phone AND isDeleted = 0")
    suspend fun countByPhone(phone: String): Int
}
