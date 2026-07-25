package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single-row table holding the shop's branding/settings used across invoices, receipts, and
 * the app header. id is always 1 — this is a singleton settings record, not a list.
 */
@Entity(tableName = "company_settings")
data class CompanySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val companyName: String = "My Photo Lab",
    val address: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val gstNumber: String? = null,
    val logoPath: String? = null,
    val receiptFooter: String? = "Thank you for your business!",
    val invoicePrefix: String = "PL",
    val currencySymbol: String = "₹",
    val isDarkMode: Boolean = false
)
