package com.example.data.repository

import com.example.data.local.UserCardDao
import com.example.data.model.UserCard
import kotlinx.coroutines.flow.Flow

class UserCardRepository(private val dao: UserCardDao) {

    val allCards: Flow<List<UserCard>> = dao.getAllCards()
    val activeCards: Flow<List<UserCard>> = dao.getActiveCards()

    suspend fun addCard(card: UserCard) {
        dao.insertCard(card)
    }

    suspend fun removeCard(card: UserCard) {
        dao.deleteCard(card)
    }

    suspend fun toggleCard(id: String, isEnabled: Boolean) {
        dao.toggleCard(id, isEnabled)
    }

    suspend fun prepopulateDefaultCardsIfEmpty() {
        if (dao.getCardCount() == 0) {
            val defaultCards = listOf(
                UserCard(id = "card-hdfc", bankName = "HDFC", cardType = "Credit Card", cardNetwork = "Visa", isEnabled = true),
                UserCard(id = "card-sbi", bankName = "SBI", cardType = "Credit Card", cardNetwork = "Mastercard", isEnabled = true),
                UserCard(id = "card-icici", bankName = "ICICI", cardType = "Credit Card", cardNetwork = "Visa", isEnabled = false),
                UserCard(id = "card-axis", bankName = "Axis", cardType = "Credit Card", cardNetwork = "RuPay", isEnabled = false),
                UserCard(id = "card-onecard", bankName = "OneCard", cardType = "Credit Card", cardNetwork = "Visa", isEnabled = false)
            )
            dao.insertCards(defaultCards)
        }
    }
}
