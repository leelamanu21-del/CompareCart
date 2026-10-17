package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(getSeedProducts())
    val products: Flow<List<Product>> = _products.asStateFlow()

    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    fun searchProducts(
        query: String,
        category: String? = null,
        minRating: Double? = null,
        maxPrice: Double? = null,
        asliDiscountOnly: Boolean = false,
        buyNowOnly: Boolean = false
    ): List<Product> {
        val q = query.trim().lowercase()
        return _products.value.filter { product ->
            val matchesQuery = if (q.isBlank()) true else {
                product.title.lowercase().contains(q) ||
                product.brand.lowercase().contains(q) ||
                product.category.lowercase().contains(q) ||
                product.specs.any { it.lowercase().contains(q) }
            }

            val matchesCategory = if (category == null || category == "All") true else {
                product.category.equals(category, ignoreCase = true)
            }

            val matchesRating = if (minRating == null) true else product.rating >= minRating

            val matchesPrice = if (maxPrice == null) true else product.currentLowestPrice <= maxPrice

            val matchesAsli = if (asliDiscountOnly) product.fakeDiscountAnalysis.isAsliDiscount else true

            val matchesBuySignal = if (buyNowOnly) product.buyWaitSignal == BuyWaitSignal.BUY_NOW else true

            matchesQuery && matchesCategory && matchesRating && matchesPrice && matchesAsli && matchesBuySignal
        }
    }

    companion object {
        fun getSeedProducts(): List<Product> {
            return listOf(
                // 1. iPhone 16 - Genuine Asli Discount & Lowest Ever
                Product(
                    id = "prod-iphone16-128",
                    title = "Apple iPhone 16 (128 GB) - Ultramarine",
                    category = "Smartphones",
                    brand = "Apple",
                    imageUrl = "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop&q=80",
                    rating = 4.7,
                    reviewCount = 14280,
                    mrp = 79900.0,
                    currentLowestPrice = 67900.0,
                    historicalLow = 67900.0,
                    historicalHigh = 79900.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 79900.0),
                        PriceHistoryPoint(75, "Aug 01", 79900.0),
                        PriceHistoryPoint(60, "Aug 15", 76900.0, true, "Independence Day Sale"),
                        PriceHistoryPoint(45, "Sep 01", 78900.0),
                        PriceHistoryPoint(30, "Sep 15", 74900.0),
                        PriceHistoryPoint(15, "Oct 01", 71900.0),
                        PriceHistoryPoint(0, "Today", 67900.0, true, "Dussehra Special")
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-ip16-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B0DGH7XZK6",
                            currentPrice = 67900.0,
                            discountPct = 15,
                            inStock = true,
                            sellerRating = 4.8,
                            deliverySpeed = "Tomorrow by 10 AM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-1", "HDFC", "Credit Card", DiscountType.FLAT, 4000.0, 4000.0, 50000.0, "HDFC4K", "Flat ₹4,000 Instant Discount on HDFC Bank Credit Cards"),
                                CardOffer("co-2", "SBI", "Credit Card", DiscountType.PERCENTAGE, 7.5, 3500.0, 40000.0, "SBISAVE", "7.5% Instant Discount up to ₹3,500 on SBI Credit Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-ip16-fk",
                            merchantName = "Flipkart",
                            originalUrl = "https://www.flipkart.com/apple-iphone-16-ultramarine-128-gb/p/itm123",
                            currentPrice = 68499.0,
                            discountPct = 14,
                            inStock = true,
                            sellerRating = 4.7,
                            deliverySpeed = "2 Days",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-3", "ICICI", "Credit Card", DiscountType.FLAT, 3500.0, 3500.0, 40000.0, "ICICI35", "Flat ₹3,500 off on ICICI Bank Cards"),
                                CardOffer("co-4", "Axis", "Credit Card", DiscountType.PERCENTAGE, 5.0, 3000.0, 30000.0, "AXIS5", "5% Unlimited Cashback on Flipkart Axis Bank Card")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-ip16-croma",
                            merchantName = "Croma",
                            originalUrl = "https://www.croma.com/apple-iphone-16-128gb-ultramarine/p/308000",
                            currentPrice = 69900.0,
                            discountPct = 12,
                            inStock = true,
                            sellerRating = 4.6,
                            deliverySpeed = "Express Store Pickup (3 hrs)",
                            returnPolicy = "Hassle-Free Return",
                            offers = listOf(
                                CardOffer("co-5", "Tata Neu", "Credit Card", DiscountType.PERCENTAGE, 5.0, 3500.0, 25000.0, "NEU5", "5% NeuCoins on Tata Neu Infinity HDFC Card")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-ip16-rel",
                            merchantName = "Reliance Digital",
                            originalUrl = "https://www.reliancedigital.in/apple-iphone-16-128gb/p/494422",
                            currentPrice = 69400.0,
                            discountPct = 13,
                            inStock = true,
                            sellerRating = 4.5,
                            deliverySpeed = "Tomorrow by 2 PM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-6", "OneCard", "Credit Card", DiscountType.FLAT, 3000.0, 3000.0, 35000.0, "ONECARD", "Instant ₹3,000 off on OneCard Metal Card")
                            )
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.BUY_NOW,
                    buyWaitReason = "Price is at historical all-time low (₹67,900). With HDFC card net effective price drops to ₹63,900. Strong Buy!",
                    upcomingSaleCalendar = "Next sale: Diwali Dhamaka in 14 days (Unlikely to drop further for base 128GB)",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 9.4,
                        ratingScore = 2.4,
                        priceCompetitiveness = 3.4,
                        discountGenuineness = 1.9,
                        merchantReliability = 1.7,
                        summaryText = "Exceptional value at lowest ever price across Amazon & Flipkart."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = false,
                        claimedDiscountPct = 15,
                        realHistoricalAverage = 76200.0,
                        trueSavingsPct = 11,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Real 90-day rolling price was ₹76,200. Current ₹67,900 represents a genuine ₹8,300 price drop without artificial pre-sale price hikes.",
                        isAsliDiscount = true
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Overall Choice", "Top rated flagship smartphone with verified authentic discount"),
                        QualityBadge(QualityBadgeType.LOWEST_EVER_PRICE, "Lowest Ever Price", "₹67,900 is the lowest price recorded in 90 days"),
                        QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Zero pre-sale price manipulation detected")
                    ),
                    specs = listOf("A18 Bionic Chip", "48MP Fusion Camera", "Camera Control Button", "Super Retina XDR OLED", "All-day Battery")
                ),

                // 2. Nothing Phone (2a) - FAKE DISCOUNT INFLATION DETECTED
                Product(
                    id = "prod-nothing-2a",
                    title = "Nothing Phone (2a) 5G (Black, 128 GB, 8 GB RAM)",
                    category = "Smartphones",
                    brand = "Nothing",
                    imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&auto=format&fit=crop&q=80",
                    rating = 4.3,
                    reviewCount = 8900,
                    mrp = 34999.0,
                    currentLowestPrice = 23999.0,
                    historicalLow = 21999.0,
                    historicalHigh = 34999.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 23999.0),
                        PriceHistoryPoint(75, "Aug 01", 23499.0),
                        PriceHistoryPoint(60, "Aug 15", 21999.0, true, "Freedom Sale Lowest Ever"),
                        PriceHistoryPoint(45, "Sep 01", 23999.0),
                        PriceHistoryPoint(15, "Oct 01", 31999.0, false, "Pre-Sale Price Hike!"),
                        PriceHistoryPoint(0, "Today", 23999.0, true, "Advertised as '31% Mega Off'")
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-np2-fk",
                            merchantName = "Flipkart",
                            originalUrl = "https://www.flipkart.com/nothing-phone-2a-5g-black-128-gb/p/itm2a",
                            currentPrice = 23999.0,
                            discountPct = 31,
                            inStock = true,
                            sellerRating = 4.4,
                            deliverySpeed = "Tomorrow by 6 PM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-np-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 1500.0, 15000.0, "SAVE10", "10% Instant Discount up to ₹1,500 on HDFC Cards"),
                                CardOffer("co-np-2", "Axis", "Credit Card", DiscountType.FLAT, 1000.0, 1000.0, 15000.0, "AXIS1K", "Flat ₹1,000 off on Axis Bank Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-np2-croma",
                            merchantName = "Croma",
                            originalUrl = "https://www.croma.com/nothing-phone-2a-5g/p/290111",
                            currentPrice = 24499.0,
                            discountPct = 30,
                            inStock = true,
                            sellerRating = 4.3,
                            deliverySpeed = "Store Delivery (1 Day)",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf()
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.WAIT_FOR_SALE,
                    buyWaitReason = "Fake pre-sale MRP inflation! Price was raised to ₹31,999 15 days ago to claim a fake 31% discount. Was ₹21,999 in August. Wait for next sale.",
                    upcomingSaleCalendar = "Diwali Big Bang Sale starting in 10 days. Expected drop to ₹21,499.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 6.8,
                        ratingScore = 1.9,
                        priceCompetitiveness = 2.1,
                        discountGenuineness = 0.9,
                        merchantReliability = 1.9,
                        summaryText = "Decent hardware but inflated discount. Real savings are modest compared to historical low."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = true,
                        claimedDiscountPct = 31,
                        realHistoricalAverage = 23500.0,
                        trueSavingsPct = 0,
                        preSalePriceHike = 8000.0,
                        detectionMessage = "⚠️ FAKE DISCOUNT DETECTED: Merchant raised the price from ₹23,999 to ₹31,999 right before the sale, then dropped it back to ₹23,999 to claim a bogus '31% Off' banner. True 90-day average is identical to today's price!",
                        isAsliDiscount = false
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.SASTA_HAI_PAR_RATING_KAM, "Fake Inflation Detected", "Price was artificially inflated right before sale", isWarning = true)
                    ),
                    specs = listOf("MediaTek Dimensity 7200 Pro", "Glyph Interface LED", "50MP Dual Camera with OIS", "120Hz Flexible AMOLED", "5000mAh Battery")
                ),

                // 3. Sony WH-1000XM5 - Wait 2 Weeks Signal
                Product(
                    id = "prod-sony-xm5",
                    title = "Sony WH-1000XM5 Wireless Noise Cancelling Headphones",
                    category = "Audio",
                    brand = "Sony",
                    imageUrl = "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=600&auto=format&fit=crop&q=80",
                    rating = 4.6,
                    reviewCount = 9410,
                    mrp = 34990.0,
                    currentLowestPrice = 28990.0,
                    historicalLow = 24990.0,
                    historicalHigh = 32990.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 29990.0),
                        PriceHistoryPoint(60, "Aug 15", 24990.0, true, "Independence Sale (Lowest Ever)"),
                        PriceHistoryPoint(45, "Sep 01", 29990.0),
                        PriceHistoryPoint(30, "Sep 15", 29490.0),
                        PriceHistoryPoint(15, "Oct 01", 29990.0),
                        PriceHistoryPoint(0, "Today", 28990.0)
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-xm5-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B09XS7JWHH",
                            currentPrice = 28990.0,
                            discountPct = 17,
                            inStock = true,
                            sellerRating = 4.8,
                            deliverySpeed = "Tomorrow by 11 AM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-xm-1", "ICICI", "Credit Card", DiscountType.FLAT, 2000.0, 2000.0, 20000.0, "ICICI2K", "Flat ₹2,000 Instant Discount on ICICI Bank Cards"),
                                CardOffer("co-xm-2", "SBI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 2000.0, 15000.0, "SBISAVE", "10% Instant Discount on SBI Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-xm5-croma",
                            merchantName = "Croma",
                            originalUrl = "https://www.croma.com/sony-wh-1000xm5-bluetooth-headphone/p/262700",
                            currentPrice = 29990.0,
                            discountPct = 14,
                            inStock = true,
                            sellerRating = 4.6,
                            deliverySpeed = "Tomorrow by 4 PM",
                            returnPolicy = "Store Replacement",
                            offers = listOf(
                                CardOffer("co-xm-3", "HDFC", "Credit Card", DiscountType.FLAT, 2000.0, 2000.0, 20000.0, "HDFCXM", "Flat ₹2,000 Instant Discount")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-xm5-rel",
                            merchantName = "Reliance Digital",
                            originalUrl = "https://www.reliancedigital.in/sony-wh-1000xm5/p/492850",
                            currentPrice = 29490.0,
                            discountPct = 16,
                            inStock = true,
                            sellerRating = 4.5,
                            deliverySpeed = "2 Days",
                            returnPolicy = "Standard Policy",
                            offers = listOf()
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.WAIT_FOR_SALE,
                    buyWaitReason = "WAIT 2 WEEKS! Price drops to ₹24,990 during festive sales (Save ₹4,000 extra). Buy only if urgent.",
                    upcomingSaleCalendar = "Amazon Great Indian Festival starts in 11 days. Expected deal: ₹24,990 with bank card.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 8.1,
                        ratingScore = 2.4,
                        priceCompetitiveness = 2.2,
                        discountGenuineness = 1.6,
                        merchantReliability = 1.9,
                        summaryText = "Industry-leading active noise cancellation, but current price is ₹4,000 above festive lowest."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = false,
                        claimedDiscountPct = 17,
                        realHistoricalAverage = 29200.0,
                        trueSavingsPct = 1,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Discount is genuine against MRP, but not close to the promotional floor of ₹24,990.",
                        isAsliDiscount = true
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best ANC Headphones", "Gold standard in active noise cancellation")
                    ),
                    specs = listOf("Industry Leading Noise Cancellation", "Auto NC Optimizer", "30-Hour Battery Life", "Speak-to-Chat & Multipoint", "Ultra-comfortable Lightweight Design")
                ),

                // 4. Samsung S24 Ultra - Best Overall Choice Flagship
                Product(
                    id = "prod-s24-ultra",
                    title = "Samsung Galaxy S24 Ultra 5G (Titanium Gray, 12GB, 256GB)",
                    category = "Smartphones",
                    brand = "Samsung",
                    imageUrl = "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop&q=80",
                    rating = 4.8,
                    reviewCount = 11200,
                    mrp = 134999.0,
                    currentLowestPrice = 119999.0,
                    historicalLow = 116999.0,
                    historicalHigh = 134999.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 129999.0),
                        PriceHistoryPoint(60, "Aug 15", 124999.0),
                        PriceHistoryPoint(45, "Sep 01", 122999.0),
                        PriceHistoryPoint(30, "Sep 15", 119999.0),
                        PriceHistoryPoint(15, "Oct 01", 118999.0),
                        PriceHistoryPoint(0, "Today", 119999.0)
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-s24-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B0CQ234F5T",
                            currentPrice = 119999.0,
                            discountPct = 11,
                            inStock = true,
                            sellerRating = 4.9,
                            deliverySpeed = "Tomorrow by 9 AM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-s24-1", "HDFC", "Credit Card", DiscountType.FLAT, 10000.0, 10000.0, 70000.0, "HDFC10K", "Instant ₹10,000 off on HDFC Credit Cards & EMI"),
                                CardOffer("co-s24-2", "SBI", "Credit Card", DiscountType.FLAT, 8000.0, 8000.0, 70000.0, "SBI8K", "Flat ₹8,000 Instant Discount on SBI Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-s24-fk",
                            merchantName = "Flipkart",
                            originalUrl = "https://www.flipkart.com/samsung-galaxy-s24-ultra-5g-titanium-gray-256-gb/p/itm12345",
                            currentPrice = 121999.0,
                            discountPct = 10,
                            inStock = true,
                            sellerRating = 4.7,
                            deliverySpeed = "2 Days",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-s24-3", "ICICI", "Credit Card", DiscountType.FLAT, 8000.0, 8000.0, 60000.0, "ICICI8K", "Flat ₹8,000 off with ICICI Bank")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-s24-croma",
                            merchantName = "Croma",
                            originalUrl = "https://www.croma.com/samsung-galaxy-s24-ultra/p/304120",
                            currentPrice = 124999.0,
                            discountPct = 7,
                            inStock = true,
                            sellerRating = 4.7,
                            deliverySpeed = "Today Store Pickup",
                            returnPolicy = "Brand Replacement",
                            offers = listOf(
                                CardOffer("co-s24-4", "Axis", "Credit Card", DiscountType.FLAT, 7000.0, 7000.0, 50000.0, "AXIS7K", "Flat ₹7,000 off on Axis Bank")
                            )
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.BUY_NOW,
                    buyWaitReason = "BUY NOW: Effective price drops to ₹1,09,999 with HDFC Bank card offer. Closest to historical low with bundled warranty.",
                    upcomingSaleCalendar = "Diwali Electronics Blast in 12 days. Card offers already live at peak value.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 9.3,
                        ratingScore = 2.5,
                        priceCompetitiveness = 3.2,
                        discountGenuineness = 1.8,
                        merchantReliability = 1.8,
                        summaryText = "Ultimate Android smartphone with massive bank discount and genuine pricing."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = false,
                        claimedDiscountPct = 11,
                        realHistoricalAverage = 122500.0,
                        trueSavingsPct = 3,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Consistent price decline over 90 days. Bank discount provides genuine out-of-pocket savings.",
                        isAsliDiscount = true
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Overall Choice", "Top Android Camera & Display of the Year"),
                        QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Bank Offer Verified", "₹10,000 instant discount confirmed active")
                    ),
                    specs = listOf("Snapdragon 8 Gen 3 for Galaxy", "200MP Quad Camera with AI Zoom", "Built-in S Pen", "Titanium Frame", "Flat Dynamic AMOLED 2X 2600 nits")
                ),

                // 5. Cheap Speaker with Warning Badge: "Sasta Hai Par Rating Kam Hai"
                Product(
                    id = "prod-cheap-soundbar",
                    title = "Zebronics Turbo Bass 2.1 Bluetooth Soundbar with Subwoofer (80W)",
                    category = "Audio",
                    brand = "Zebronics",
                    imageUrl = "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&auto=format&fit=crop&q=80",
                    rating = 3.1,
                    reviewCount = 1840,
                    mrp = 7999.0,
                    currentLowestPrice = 1899.0,
                    historicalLow = 1799.0,
                    historicalHigh = 3499.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 2499.0),
                        PriceHistoryPoint(60, "Aug 15", 1999.0),
                        PriceHistoryPoint(30, "Sep 15", 2299.0),
                        PriceHistoryPoint(0, "Today", 1899.0)
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-zeb-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B08XTRBO12",
                            currentPrice = 1899.0,
                            discountPct = 76,
                            inStock = true,
                            sellerRating = 3.2,
                            deliverySpeed = "3 Days",
                            returnPolicy = "Replacement Only",
                            offers = listOf(
                                CardOffer("co-zeb-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 200.0, 1500.0, "SAVE10", "10% Instant Discount on HDFC Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-zeb-fk",
                            merchantName = "Flipkart",
                            originalUrl = "https://www.flipkart.com/zebronics-turbo-bass/p/itmzeb",
                            currentPrice = 1999.0,
                            discountPct = 75,
                            inStock = true,
                            sellerRating = 3.3,
                            deliverySpeed = "4 Days",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf()
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.NEUTRAL,
                    buyWaitReason = "⚠️ Low customer satisfaction (3.1/5 stars). Over 28% of reviews report Bluetooth connectivity dropouts and muffled bass.",
                    upcomingSaleCalendar = "Frequently on flash sale around ₹1,800 - ₹2,000.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 5.2,
                        ratingScore = 1.1,
                        priceCompetitiveness = 3.0,
                        discountGenuineness = 0.5,
                        merchantReliability = 0.6,
                        summaryText = "Very cheap price but high return rate and poor long-term durability reported."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = true,
                        claimedDiscountPct = 76,
                        realHistoricalAverage = 2100.0,
                        trueSavingsPct = 10,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Artificial ₹7,999 MRP exaggeration. This soundbar has never sold above ₹2,999 anywhere in India.",
                        isAsliDiscount = false
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.SASTA_HAI_PAR_RATING_KAM, "Sasta Hai Par Rating Kam Hai", "Low 3.1 rating with 28% defect complaints", isWarning = true)
                    ),
                    specs = listOf("80W Peak Output", "Bluetooth 5.0", "Wired Subwoofer", "HDMI ARC / Optical / AUX", "LED Display")
                ),

                // 6. MacBook Air M3 - High Value Laptop
                Product(
                    id = "prod-macbook-air-m3",
                    title = "Apple MacBook Air 13-inch (M3 chip, 16GB Unified Memory, 256GB SSD)",
                    category = "Laptops",
                    brand = "Apple",
                    imageUrl = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop&q=80",
                    rating = 4.8,
                    reviewCount = 5300,
                    mrp = 114900.0,
                    currentLowestPrice = 104900.0,
                    historicalLow = 102900.0,
                    historicalHigh = 114900.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 114900.0),
                        PriceHistoryPoint(60, "Aug 15", 109900.0),
                        PriceHistoryPoint(30, "Sep 15", 107900.0),
                        PriceHistoryPoint(0, "Today", 104900.0)
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-mac-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B0CX21MP6R",
                            currentPrice = 104900.0,
                            discountPct = 9,
                            inStock = true,
                            sellerRating = 4.9,
                            deliverySpeed = "Tomorrow by 11 AM",
                            returnPolicy = "7 Days Replacement",
                            offers = listOf(
                                CardOffer("co-mac-1", "ICICI", "Credit Card", DiscountType.FLAT, 5000.0, 5000.0, 80000.0, "ICICIMAC", "Flat ₹5,000 Instant Discount on ICICI Bank Cards"),
                                CardOffer("co-mac-2", "SBI", "Credit Card", DiscountType.FLAT, 5000.0, 5000.0, 80000.0, "SBIMAC", "Flat ₹5,000 Instant Discount on SBI Cards")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-mac-croma",
                            merchantName = "Croma",
                            originalUrl = "https://www.croma.com/apple-macbook-air-m3-16gb-256gb/p/306712",
                            currentPrice = 106900.0,
                            discountPct = 7,
                            inStock = true,
                            sellerRating = 4.8,
                            deliverySpeed = "Express Delivery",
                            returnPolicy = "Brand Service",
                            offers = listOf(
                                CardOffer("co-mac-3", "HDFC", "Credit Card", DiscountType.FLAT, 5000.0, 5000.0, 80000.0, "HDFCMAC", "Flat ₹5,000 off on HDFC Cards")
                            )
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.BUY_NOW,
                    buyWaitReason = "BUY NOW! Upgraded 16GB base RAM model at ₹99,900 net effective price with ICICI/SBI card offer.",
                    upcomingSaleCalendar = "Apple student offers ended. Current bank deals represent best Q4 value.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 9.5,
                        ratingScore = 2.5,
                        priceCompetitiveness = 3.3,
                        discountGenuineness = 1.9,
                        merchantReliability = 1.8,
                        summaryText = "Exceptional performance, silent fanless thermal design, and all-day 18-hour battery."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = false,
                        claimedDiscountPct = 9,
                        realHistoricalAverage = 108900.0,
                        trueSavingsPct = 4,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Consistent downward trend from original launch price. Zero artificial price manipulation.",
                        isAsliDiscount = true
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Overall Laptop", "Top rated ultraportable for developers & creators"),
                        QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified ₹5,000 bank savings")
                    ),
                    specs = listOf("Apple M3 8-core CPU / 8-core GPU", "16GB Unified Memory (Standard)", "13.6-inch Liquid Retina Display", "Up to 18 hours battery life", "MagSafe 3 Charging")
                ),

                // 7. LG 1.5 Ton 5 Star AI Inverter Split AC
                Product(
                    id = "prod-lg-ac-15",
                    title = "LG 1.5 Ton 5 Star AI DUAL Inverter Split AC (Copper, Super Convertible 6-in-1 Cooling)",
                    category = "Appliances",
                    brand = "LG",
                    imageUrl = "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=600&auto=format&fit=crop&q=80",
                    rating = 4.5,
                    reviewCount = 6840,
                    mrp = 78990.0,
                    currentLowestPrice = 43990.0,
                    historicalLow = 42490.0,
                    historicalHigh = 48990.0,
                    priceHistory = listOf(
                        PriceHistoryPoint(90, "Jul 15", 47990.0),
                        PriceHistoryPoint(60, "Aug 15", 45990.0),
                        PriceHistoryPoint(30, "Sep 15", 44490.0),
                        PriceHistoryPoint(0, "Today", 43990.0)
                    ),
                    merchantPrices = listOf(
                        MerchantPrice(
                            id = "mp-lgac-amz",
                            merchantName = "Amazon",
                            originalUrl = "https://www.amazon.in/dp/B0BRVYZ74P",
                            currentPrice = 43990.0,
                            discountPct = 44,
                            inStock = true,
                            sellerRating = 4.6,
                            deliverySpeed = "Tomorrow + Free Installation",
                            returnPolicy = "10 Days Replacement",
                            offers = listOf(
                                CardOffer("co-ac-1", "HDFC", "Credit Card", DiscountType.FLAT, 2000.0, 2000.0, 35000.0, "HDFC2K", "Flat ₹2,000 Instant Discount on HDFC Cards"),
                                CardOffer("co-ac-2", "ICICI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 2000.0, 30000.0, "ICICISAVE", "10% Instant Discount up to ₹2,000")
                            )
                        ),
                        MerchantPrice(
                            id = "mp-lgac-rel",
                            merchantName = "Reliance Digital",
                            originalUrl = "https://www.reliancedigital.in/lg-1-5-ton-5-star-inverter-split-ac/p/493655",
                            currentPrice = 44990.0,
                            discountPct = 43,
                            inStock = true,
                            sellerRating = 4.7,
                            deliverySpeed = "Express Installation (24 hrs)",
                            returnPolicy = "Brand Installation",
                            offers = listOf(
                                CardOffer("co-ac-3", "OneCard", "Credit Card", DiscountType.FLAT, 1500.0, 1500.0, 30000.0, "ONECARD", "Instant ₹1,500 off on OneCard")
                            )
                        )
                    ),
                    buyWaitSignal = BuyWaitSignal.BUY_NOW,
                    buyWaitReason = "Off-season sweet spot! Price is ₹4,000 lower than peak summer rates with free installation bundle.",
                    upcomingSaleCalendar = "Prices stabilize during winter, expected to climb back to ₹47,000 by February.",
                    valueScore = ValueScoreBreakdown(
                        totalScore = 9.1,
                        ratingScore = 2.3,
                        priceCompetitiveness = 3.2,
                        discountGenuineness = 1.8,
                        merchantReliability = 1.8,
                        summaryText = "High energy efficiency (ISEER 5.2) with low maintenance and robust copper condenser."
                    ),
                    fakeDiscountAnalysis = FakeDiscountAnalysis(
                        isFakeDiscount = false,
                        claimedDiscountPct = 44,
                        realHistoricalAverage = 45800.0,
                        trueSavingsPct = 4,
                        preSalePriceHike = 0.0,
                        detectionMessage = "Genuine seasonal off-peak pricing. Solid buy before summer manufacturing rate hikes.",
                        isAsliDiscount = true
                    ),
                    badges = listOf(
                        QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Energy Saver", "5-Star rating with AI dual inverter cooling")
                    ),
                    specs = listOf("1.5 Ton Capacity", "5 Star Energy Rating (ISEER 5.2)", "AI Dual Inverter Compressor", "6-in-1 Convertible Modes", "100% Copper with Ocean Black Protection")
                )
            )
        }
    }
}
