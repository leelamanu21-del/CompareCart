package com.example.data.model

data class PriceHistoryPoint(
    val timestampDaysAgo: Int, // e.g. 90, 75, 60, 45, 30, 15, 0 (today)
    val dateLabel: String,     // e.g. "Aug 15", "Sep 1", "Sep 20", "Oct 1", "Today"
    val price: Double,
    val isSaleEvent: Boolean = false,
    val saleEventName: String? = null
)

enum class BuyWaitSignal {
    BUY_NOW,
    WAIT_FOR_SALE,
    NEUTRAL
}

enum class QualityBadgeType {
    BEST_OVERALL_CHOICE,
    LOWEST_EVER_PRICE,
    SASTA_HAI_PAR_RATING_KAM, // Warning badge
    ASLI_DISCOUNT_VERIFIED
}

data class QualityBadge(
    val type: QualityBadgeType,
    val title: String,
    val subtitle: String,
    val isWarning: Boolean = false
)

data class ValueScoreBreakdown(
    val totalScore: Double,          // e.g. 9.4
    val ratingScore: Double,         // max 2.5
    val priceCompetitiveness: Double,// max 3.5
    val discountGenuineness: Double, // max 2.0
    val merchantReliability: Double, // max 2.0
    val summaryText: String
)

data class FakeDiscountAnalysis(
    val isFakeDiscount: Boolean,
    val claimedDiscountPct: Int,     // e.g. 42%
    val realHistoricalAverage: Double,// 90-day average price
    val trueSavingsPct: Int,         // Actual savings vs 90-day average
    val preSalePriceHike: Double,    // How much price was hiked right before sale
    val detectionMessage: String,
    val isAsliDiscount: Boolean
)

data class Product(
    val id: String,
    val title: String,
    val category: String,
    val brand: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val mrp: Double,
    val currentLowestPrice: Double,
    val historicalLow: Double,
    val historicalHigh: Double,
    val priceHistory: List<PriceHistoryPoint>,
    val merchantPrices: List<MerchantPrice>,
    val buyWaitSignal: BuyWaitSignal,
    val buyWaitReason: String,
    val upcomingSaleCalendar: String,
    val valueScore: ValueScoreBreakdown,
    val fakeDiscountAnalysis: FakeDiscountAnalysis,
    val badges: List<QualityBadge>,
    val specs: List<String>,
    val secondaryImageUrl: String? = null,
    val brandLogoUrl: String? = null
)
