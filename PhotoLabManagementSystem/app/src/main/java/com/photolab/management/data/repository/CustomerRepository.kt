package com.photolab.management.data.repository

import com.photolab.management.data.database.dao.CustomerDao
import com.photolab.management.data.database.entity.CustomerEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    fun getAllCustomers() = customerDao.getAllCustomers()
    fun search(query: String) = customerDao.search(query)

    suspend fun addCustomer(customer: CustomerEntity): Result<Long> {
        if (customerDao.countByPhone(customer.phone) > 0) {
            return Result.failure(IllegalStateException("A customer with this phone number already exists"))
        }
        return Result.success(customerDao.insert(customer))
    }

    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.update(customer)
    suspend fun deleteCustomer(customerId: Long) = customerDao.softDelete(customerId)
}
