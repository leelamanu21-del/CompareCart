package com.example.data.repository

import com.example.data.local.PriceAlertDao
import com.example.data.model.PriceAlert
import kotlinx.coroutines.flow.Flow

class PriceAlertRepository(private val dao: PriceAlertDao) {

    val allAlerts: Flow<List<PriceAlert>> = dao.getAllAlerts()

    suspend fun createAlert(
        productId: String,
        productTitle: String,
        productImage: String,
        merchantName: String,
        targetPrice: Double,
        currentLowestPrice: Double,
        historicalLow: Double
    ) {
        val alert = PriceAlert(
            id = "alert-${System.currentTimeMillis()}",
            productId = productId,
            productTitle = productTitle,
            productImage = productImage,
            merchantName = merchantName,
            targetPrice = targetPrice,
            currentLowestPrice = currentLowestPrice,
            historicalLow = historicalLow,
            isActive = true
        )
        dao.insertAlert(alert)
    }

    suspend fun removeAlert(alert: PriceAlert) {
        dao.deleteAlert(alert)
    }

    suspend fun deleteAlertById(id: String) {
        dao.deleteAlertById(id)
    }

    suspend fun getAlertForProduct(productId: String): PriceAlert? {
        return dao.getAlertForProduct(productId)
    }
}
