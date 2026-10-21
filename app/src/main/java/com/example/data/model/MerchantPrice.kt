package com.example.data.model

enum class DiscountType {
    PERCENTAGE,
    FLAT
}

data class CardOffer(
    val id: String,
    val bankName: String, // e.g. "HDFC", "SBI", "ICICI", "Axis", "Kotak", "OneCard"
    val cardType: String, // "Credit Card", "Debit Card", "EMI"
    val discountType: DiscountType,
    val discountValue: Double, // e.g. 10.0 for 10% or 1500.0 for flat ₹1,500
    val maxDiscount: Double,   // e.g. 2000.0
    val minSpend: Double,      // e.g. 5000.0
    val promoCode: String? = null,
    val offerDescription: String
) {
    fun calculateDiscount(amount: Double): Double {
        if (amount < minSpend) return 0.0
        return when (discountType) {
            DiscountType.PERCENTAGE -> {
                val calculated = (amount * discountValue) / 100.0
                minOf(calculated, maxDiscount)
            }
            DiscountType.FLAT -> minOf(discountValue, maxDiscount)
        }
    }
}

data class MerchantPrice(
    val id: String,
    val merchantName: String, // "Amazon", "Flipkart", "Croma", "Reliance Digital", "Tata CLiQ", "Myntra"
    val originalUrl: String,
    val currentPrice: Double,
    val discountPct: Int,
    val inStock: Boolean,
    val sellerRating: Double,
    val deliverySpeed: String, // "Tomorrow by 11 AM", "2 Days", "Same-Day Delivery"
    val returnPolicy: String,  // "7-Day Replacement", "10-Day Return"
    val offers: List<CardOffer> = emptyList(),
    val qualityBadge: String? = null,
    val isRecommended: Boolean = false
) {
    /**
     * Calculates effective net out-of-pocket price based on user's active bank cards
     */
    fun calculateEffectivePrice(activeBankNames: Set<String>): EffectivePriceCalculation {
        var bestDiscount = 0.0
        var appliedOffer: CardOffer? = null

        for (offer in offers) {
            if (activeBankNames.contains(offer.bankName)) {
                val discount = offer.calculateDiscount(currentPrice)
                if (discount > bestDiscount) {
                    bestDiscount = discount
                    appliedOffer = offer
                }
            }
        }

        val netPrice = maxOf(0.0, currentPrice - bestDiscount)
        return EffectivePriceCalculation(
            merchantName = merchantName,
            basePrice = currentPrice,
            cardDiscount = bestDiscount,
            netEffectivePrice = netPrice,
            appliedOffer = appliedOffer
        )
    }
}

data class EffectivePriceCalculation(
    val merchantName: String,
    val basePrice: Double,
    val cardDiscount: Double,
    val netEffectivePrice: Double,
    val appliedOffer: CardOffer?
)
