package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_cards")
data class UserCard(
    @PrimaryKey
    val id: String,
    val bankName: String,     // "HDFC", "SBI", "ICICI", "Axis", "Kotak", "OneCard", "Amex"
    val cardType: String,     // "Credit Card", "Debit Card"
    val cardNetwork: String,  // "Visa", "Mastercard", "RuPay"
    val isEnabled: Boolean = true,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_alerts")
data class PriceAlert(
    @PrimaryKey
    val id: String,
    val productId: String,
    val productTitle: String,
    val productImage: String,
    val merchantName: String,
    val targetPrice: Double,
    val currentLowestPrice: Double,
    val historicalLow: Double,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
