package com.example.data.repository

import com.example.data.model.*

object UniversalProductCatalog {

    fun getAllUniversalProducts(): List<Product> {
        return listOf(
            // ================= FASHION =================
            Product(
                id = "fsh-puma-smash",
                title = "Puma Unisex-Adult Smashic Low-Top Sneakers",
                category = "Fashion",
                brand = "Puma",
                imageUrl = "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=600&auto=format&fit=crop&q=80",
                rating = 4.4,
                reviewCount = 8240,
                mrp = 4499.0,
                currentLowestPrice = 1799.0,
                historicalLow = 1699.0,
                historicalHigh = 3499.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 3199.0),
                    PriceHistoryPoint(60, "Aug 15", 2499.0),
                    PriceHistoryPoint(30, "Sep 15", 1999.0),
                    PriceHistoryPoint(0, "Today", 1799.0, true, "End of Season Sale")
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-puma-myntra",
                        merchantName = "Myntra",
                        originalUrl = "https://www.myntra.com/casual-shoes/puma/puma-unisex-smashic-sneakers/2510294/buy",
                        currentPrice = 1799.0,
                        discountPct = 60,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Tomorrow by 2 PM",
                        returnPolicy = "14 Days Return",
                        offers = listOf(
                            CardOffer("co-fsh-1", "Kotak", "Credit Card", DiscountType.PERCENTAGE, 10.0, 300.0, 1500.0, "KOTAK10", "10% Instant Discount on Kotak Bank Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-puma-ajio",
                        merchantName = "Ajio",
                        originalUrl = "https://www.ajio.com/puma-smashic-low-top-sneakers/p/469123000",
                        currentPrice = 1899.0,
                        discountPct = 58,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "2 Days",
                        returnPolicy = "15 Days Return",
                        offers = listOf(
                            CardOffer("co-fsh-2", "ICICI", "Credit Card", DiscountType.FLAT, 200.0, 200.0, 1500.0, "ICICI200", "Flat ₹200 off with ICICI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-puma-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B09V7XZQW2",
                        currentPrice = 1949.0,
                        discountPct = 57,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "10 Days Return",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: 60% off is authentic. Price is within ₹100 of all-time low recorded during Freedom Sale.",
                upcomingSaleCalendar = "Next fashion sale in 18 days. Stock is currently available in all sizes.",
                valueScore = ValueScoreBreakdown(9.2, 2.3, 3.4, 1.8, 1.7, "Exceptional build quality, soft foam cushioning, and authentic discount."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 60, 2600.0, 31, 0.0, "Genuine discount. No artificial MRP inflation detected.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Value Sneaker", "Top customer satisfaction in daily casual wear"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 60% off against rolling market price")
                ),
                specs = listOf("SoftFoam+ sockliner", "Durable Rubber Outsole", "Classic Tennis silhouette", "Cushioned collar")
            ),

            Product(
                id = "fsh-levis-511",
                title = "Levi's Men's 511 Slim Fit Stretch Denim Jeans",
                category = "Fashion",
                brand = "Levi's",
                imageUrl = "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=600&auto=format&fit=crop&q=80",
                rating = 4.5,
                reviewCount = 12900,
                mrp = 3999.0,
                currentLowestPrice = 1899.0,
                historicalLow = 1799.0,
                historicalHigh = 3499.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 2899.0),
                    PriceHistoryPoint(60, "Aug 15", 2199.0),
                    PriceHistoryPoint(30, "Sep 15", 2499.0),
                    PriceHistoryPoint(0, "Today", 1899.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-lev-ajio",
                        merchantName = "Ajio",
                        originalUrl = "https://www.ajio.com/levis-511-slim-fit-jeans/p/460001",
                        currentPrice = 1899.0,
                        discountPct = 52,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "15 Days Return",
                        offers = listOf(
                            CardOffer("co-lev-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 200.0, 1500.0, "AJIO10", "10% Instant Discount on HDFC Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-lev-myntra",
                        merchantName = "Myntra",
                        originalUrl = "https://www.myntra.com/jeans/levis/levis-511-slim-fit/24101/buy",
                        currentPrice = 1999.0,
                        discountPct = 50,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "2 Days",
                        returnPolicy = "14 Days Return",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: True 52% markdown on authentic Levi's 511 stretch denim.",
                upcomingSaleCalendar = "Price drops below ₹1,900 only during mid-season clearances.",
                valueScore = ValueScoreBreakdown(9.1, 2.4, 3.2, 1.8, 1.7, "Premium stretch cotton blend with high wash durability."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 52, 2450.0, 22, 0.0, "Genuine discount against official ₹3,999 Levi's tag.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Slim Jeans", "Rated #1 for fit and longevity")
                ),
                specs = listOf("98% Cotton, 2% Elastane", "Zip fly with button closure", "Mid rise, slim from hip to ankle")
            ),

            // ================= HOME & KITCHEN =================
            Product(
                id = "hm-prestige-svachh",
                title = "Prestige Svachh Hard Anodised Triply Pressure Cooker (3 Litre)",
                category = "Home & Kitchen",
                brand = "Prestige",
                imageUrl = "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=600&auto=format&fit=crop&q=80",
                rating = 4.6,
                reviewCount = 21400,
                mrp = 2890.0,
                currentLowestPrice = 1799.0,
                historicalLow = 1699.0,
                historicalHigh = 2490.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 2290.0),
                    PriceHistoryPoint(60, "Aug 15", 1990.0),
                    PriceHistoryPoint(30, "Sep 15", 1899.0),
                    PriceHistoryPoint(0, "Today", 1799.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-prest-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B085VPRSTZ",
                        currentPrice = 1799.0,
                        discountPct = 38,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "10 Days Return",
                        offers = listOf(
                            CardOffer("co-hm-1", "SBI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 200.0, 1500.0, "SBISAVE", "10% Instant Discount on SBI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-prest-fk",
                        merchantName = "Flipkart",
                        originalUrl = "https://www.flipkart.com/prestige-svachh-3-l-pressure-cooker/p/itm1234",
                        currentPrice = 1849.0,
                        discountPct = 36,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-prest-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/prestige-svachh-3l-cooker/490001",
                        currentPrice = 1899.0,
                        discountPct = 34,
                        inStock = true,
                        sellerRating = 4.4,
                        deliverySpeed = "2 Days",
                        returnPolicy = "Replacement Guarantee",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Svachh deep lid design prevents frothy water spillage. Current price is near festival low.",
                upcomingSaleCalendar = "Diwali kitchen discounts live now.",
                valueScore = ValueScoreBreakdown(9.4, 2.4, 3.4, 1.8, 1.8, "Top rated Indian kitchen essential with gas & induction compatibility."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 38, 2050.0, 12, 0.0, "Authentic price reduction against Prestige catalog MRP.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Kitchen Essential", "Zero-spillage patented Svachh lid design"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 38% off")
                ),
                specs = listOf("Deep lid controls frothing", "Hard Anodised Triply Body", "Gas and Induction compatible", "5-Year Manufacturer Warranty")
            ),

            Product(
                id = "hm-philips-airfryer",
                title = "Philips Digital Air Fryer (4.1 Litre, Rapid Air Technology, 1400W)",
                category = "Home & Kitchen",
                brand = "Philips",
                imageUrl = "https://images.unsplash.com/photo-1585515320310-259814833e62?w=600&auto=format&fit=crop&q=80",
                rating = 4.5,
                reviewCount = 18500,
                mrp = 11995.0,
                currentLowestPrice = 6499.0,
                historicalLow = 5999.0,
                historicalHigh = 8995.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 7999.0),
                    PriceHistoryPoint(60, "Aug 15", 6999.0),
                    PriceHistoryPoint(30, "Sep 15", 6799.0),
                    PriceHistoryPoint(0, "Today", 6499.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-phil-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B097NPH3B5",
                        currentPrice = 6499.0,
                        discountPct = 46,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 10 AM",
                        returnPolicy = "7 Days Replacement",
                        offers = listOf(
                            CardOffer("co-hm-2", "HDFC", "Credit Card", DiscountType.FLAT, 500.0, 500.0, 5000.0, "HDFC500", "Flat ₹500 off on HDFC Bank Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-phil-croma",
                        merchantName = "Croma",
                        originalUrl = "https://www.croma.com/philips-essential-4-1l-air-fryer/p/242200",
                        currentPrice = 6799.0,
                        discountPct = 43,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Store Pickup Available",
                        returnPolicy = "Store Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Saves up to 90% oil with NutriU app integration. Net price drops to ₹5,999 with HDFC card.",
                upcomingSaleCalendar = "Price rarely drops below ₹6,499 outside Black Friday/Diwali.",
                valueScore = ValueScoreBreakdown(9.3, 2.3, 3.4, 1.8, 1.8, "Market leader in air fryers with consistent cooking and easy wash basket."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 46, 7200.0, 10, 0.0, "Legitimate discount backed by consistent historical drops.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Healthy Fryer", "Cooks with up to 90% less fat")
                ),
                specs = listOf("Rapid Air Technology", "Touchscreen with 7 presets", "Keep warm function", "Dishwasher-safe parts")
            ),

            // ================= GROCERIES & GOURMET =================
            Product(
                id = "grc-daawat-biryani",
                title = "Daawat Biryani Basmati Rice (5 Kg, Longest Grains)",
                category = "Groceries",
                brand = "Daawat",
                imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=600&auto=format&fit=crop&q=80",
                rating = 4.6,
                reviewCount = 34500,
                mrp = 1175.0,
                currentLowestPrice = 699.0,
                historicalLow = 649.0,
                historicalHigh = 950.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 820.0),
                    PriceHistoryPoint(60, "Aug 15", 740.0),
                    PriceHistoryPoint(30, "Sep 15", 720.0),
                    PriceHistoryPoint(0, "Today", 699.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-daawat-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/daawat-biryani-basmati-rice-5-kg/490001394",
                        currentPrice = 699.0,
                        discountPct = 40,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Today in 2 Hours",
                        returnPolicy = "Replacement on Damaged Seal",
                        offers = listOf(
                            CardOffer("co-grc-1", "SBI", "Debit Card", DiscountType.PERCENTAGE, 5.0, 50.0, 500.0, "JIOSBI", "5% Instant Savings with SBI Debit Card")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-daawat-amz",
                        merchantName = "Amazon Fresh",
                        originalUrl = "https://www.amazon.in/dp/B00V5E955M",
                        currentPrice = 729.0,
                        discountPct = 38,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow by 8 AM",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-daawat-fk",
                        merchantName = "Flipkart Grocery",
                        originalUrl = "https://www.flipkart.com/daawat-biryani-basmati-rice-5-kg/p/itm54321",
                        currentPrice = 749.0,
                        discountPct = 36,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "Non-returnable consumable",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: JioMart offers direct farm-to-door ₹699 price with 2-hour delivery.",
                upcomingSaleCalendar = "Pantry essential with consistent seasonal pricing.",
                valueScore = ValueScoreBreakdown(9.5, 2.5, 3.4, 1.8, 1.8, "Fluffy non-sticky elongated grains ideal for royal biryanis."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 40, 770.0, 9, 0.0, "Authentic grocery discount. Standard market retail is ₹850.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Biryani Rice", "#1 best selling aged Basmati grain"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 40% grocery saving")
                ),
                specs = listOf("Aged over 2 years", "Extends up to 24mm when cooked", "Pristine aroma and pearls", "Pack of 5kg")
            ),

            Product(
                id = "grc-nutraj-almonds",
                title = "Nutraj California Raw Almonds (1 Kg Value Pack)",
                category = "Groceries",
                brand = "Nutraj",
                imageUrl = "https://images.unsplash.com/photo-1508061252224-40394caa3030?w=600&auto=format&fit=crop&q=80",
                rating = 4.4,
                reviewCount = 14200,
                mrp = 1299.0,
                currentLowestPrice = 749.0,
                historicalLow = 699.0,
                historicalHigh = 999.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 899.0),
                    PriceHistoryPoint(60, "Aug 15", 799.0),
                    PriceHistoryPoint(30, "Sep 15", 769.0),
                    PriceHistoryPoint(0, "Today", 749.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-nutraj-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/nutraj-california-almonds-1-kg/491000",
                        currentPrice = 749.0,
                        discountPct = 42,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "Today",
                        returnPolicy = "Replacement if seal broken",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-nutraj-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B07D3NXT4G",
                        currentPrice = 789.0,
                        discountPct = 39,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "Replacement",
                        offers = listOf(
                            CardOffer("co-grc-2", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 100.0, 700.0, "SAVE10", "10% Instant Savings")
                        )
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: 100% natural, vacuum packed California almonds under ₹750/kg.",
                upcomingSaleCalendar = "Dry fruits demand spikes during festive season. Buy before prices rise.",
                valueScore = ValueScoreBreakdown(9.1, 2.2, 3.3, 1.8, 1.8, "High dietary fiber, zero trans fats, and crunchy fresh taste."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 42, 830.0, 10, 0.0, "Authentic dry fruits market rate.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Dry Fruits Deal", "Rich in Vitamin E and Magnesium")
                ),
                specs = listOf("100% Real California Almonds", "Gluten-free & Vegan", "Resealable pouch", "1 Kg Net Weight")
            ),

            // ================= BEAUTY & PERSONAL CARE =================
            Product(
                id = "bty-derma-vitc",
                title = "The Derma Co 10% Vitamin C Face Serum with Niacinamide & Hyaluronic Acid (30 ml)",
                category = "Beauty",
                brand = "The Derma Co",
                imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80",
                rating = 4.5,
                reviewCount = 28900,
                mrp = 649.0,
                currentLowestPrice = 419.0,
                historicalLow = 399.0,
                historicalHigh = 599.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 549.0),
                    PriceHistoryPoint(60, "Aug 15", 479.0),
                    PriceHistoryPoint(30, "Sep 15", 449.0),
                    PriceHistoryPoint(0, "Today", 419.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-derma-nykaa",
                        merchantName = "Nykaa",
                        originalUrl = "https://www.nykaa.com/the-derma-co-10-percent-vitamin-c-serum/p/1029381",
                        currentPrice = 419.0,
                        discountPct = 35,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "15 Days Return",
                        offers = listOf(
                            CardOffer("co-bty-1", "Kotak", "Credit Card", DiscountType.PERCENTAGE, 10.0, 100.0, 400.0, "NYKAA10", "10% off with Kotak Card")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-derma-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B08GS24C9R",
                        currentPrice = 439.0,
                        discountPct = 32,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-derma-myntra",
                        merchantName = "Myntra",
                        originalUrl = "https://www.myntra.com/face-serum/the-derma-co/the-derma-co-10-vitamin-c-serum/2049182/buy",
                        currentPrice = 449.0,
                        discountPct = 31,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "2 Days",
                        returnPolicy = "14 Days Return",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Effective brightening serum at authentic 35% discount with high buyer ratings.",
                upcomingSaleCalendar = "Nykaa Hot Pink Sale ongoing.",
                valueScore = ValueScoreBreakdown(9.3, 2.4, 3.3, 1.8, 1.8, "Dermatologist-formulated with stable 3-O-Ethyl Ascorbic Acid."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 35, 490.0, 14, 0.0, "Genuine cosmetic discount backed by verified Nykaa listing.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Radiance Serum", "Over 28,000 verified reviews"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 35% off")
                ),
                specs = listOf("10% Vitamin C & 5% Niacinamide", "Centella Asiatica Extract", "Treats dark spots & hyperpigmentation", "Suitable for all skin types")
            ),

            // ================= BOOKS & STATIONERY =================
            Product(
                id = "bk-atomic-habits",
                title = "Atomic Habits: An Easy & Proven Way to Build Good Habits by James Clear",
                category = "Books",
                brand = "Random House",
                imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=600&auto=format&fit=crop&q=80",
                rating = 4.8,
                reviewCount = 78000,
                mrp = 799.0,
                currentLowestPrice = 399.0,
                historicalLow = 349.0,
                historicalHigh = 650.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 520.0),
                    PriceHistoryPoint(60, "Aug 15", 450.0),
                    PriceHistoryPoint(30, "Sep 15", 420.0),
                    PriceHistoryPoint(0, "Today", 399.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-atom-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/1847941834",
                        currentPrice = 399.0,
                        discountPct = 50,
                        inStock = true,
                        sellerRating = 4.9,
                        deliverySpeed = "Tomorrow by 9 AM",
                        returnPolicy = "Replacement on Damaged Copy",
                        offers = listOf(
                            CardOffer("co-bk-1", "ICICI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 50.0, 300.0, "ICICI50", "10% off with ICICI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-atom-fk",
                        merchantName = "Flipkart",
                        originalUrl = "https://www.flipkart.com/atomic-habits/p/itmbk12345",
                        currentPrice = 429.0,
                        discountPct = 46,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "2 Days",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Global #1 self-improvement bestseller at half price (₹399). Official paperback edition.",
                upcomingSaleCalendar = "Consistent pricing year-round.",
                valueScore = ValueScoreBreakdown(9.8, 2.5, 3.5, 1.9, 1.9, "Transformative insights backed by cognitive psychology."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 50, 470.0, 15, 0.0, "Authentic half-price book deal.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "#1 International Bestseller", "Over 15 million copies sold globally"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 50% discount")
                ),
                specs = listOf("Paperback Edition", "320 Pages", "English Language", "ISBN-13: 978-1847941831")
            ),

            // ================= DAILY ESSENTIALS & HEALTH =================
            Product(
                id = "ess-dettol-refill",
                title = "Dettol Liquid Handwash Refill Pouch - Skincare (1500 ml)",
                category = "Daily Essentials",
                brand = "Dettol",
                imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=600&auto=format&fit=crop&q=80",
                rating = 4.7,
                reviewCount = 42100,
                mrp = 289.0,
                currentLowestPrice = 179.0,
                historicalLow = 169.0,
                historicalHigh = 250.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 229.0),
                    PriceHistoryPoint(60, "Aug 15", 199.0),
                    PriceHistoryPoint(30, "Sep 15", 189.0),
                    PriceHistoryPoint(0, "Today", 179.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-dettol-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/dettol-skincare-liquid-handwash-refill-1500-ml/490001899",
                        currentPrice = 179.0,
                        discountPct = 38,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Today in 2 Hours",
                        returnPolicy = "Replacement if Damaged",
                        offers = listOf(
                            CardOffer("co-ess-1", "HDFC", "Debit Card", DiscountType.PERCENTAGE, 5.0, 20.0, 150.0, "JIOHDFC", "Instant 5% Savings")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-dettol-amz",
                        merchantName = "Amazon Fresh",
                        originalUrl = "https://www.amazon.in/dp/B07Z8LPRQZ",
                        currentPrice = 189.0,
                        discountPct = 35,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "Non-returnable",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-dettol-fk",
                        merchantName = "Flipkart Grocery",
                        originalUrl = "https://www.flipkart.com/dettol-skincare-liquid-handwash-refill-1500-ml/p/itm1288",
                        currentPrice = 195.0,
                        discountPct = 33,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: 1.5L bulk pack at ₹179 gives maximum cost-per-ml efficiency.",
                upcomingSaleCalendar = "Essential staple with constant low pricing.",
                valueScore = ValueScoreBreakdown(9.5, 2.4, 3.5, 1.8, 1.8, "Trusted 99.9% germ protection with moisturizing formulation."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 38, 205.0, 12, 0.0, "Genuine supermarket bulk discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Hygiene Value", "99.9% Germ Protection with added moisturizers")
                ),
                specs = listOf("1500 ml Refill Pouch", "pH-Balanced Skincare formula", "Tested by dermatologists", "10x better protection than normal soaps")
            ),

            // ================= ELECTRONICS =================
            Product(
                id = "prod-iphone16-128",
                title = "Apple iPhone 16 (128 GB) - Ultramarine",
                category = "Electronics",
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
                    PriceHistoryPoint(60, "Aug 15", 76900.0),
                    PriceHistoryPoint(30, "Sep 15", 74900.0),
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
                            CardOffer("co-3", "ICICI", "Credit Card", DiscountType.FLAT, 3500.0, 3500.0, 40000.0, "ICICI35", "Flat ₹3,500 off on ICICI Bank Cards")
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
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "Price is at historical all-time low (₹67,900). With HDFC card net effective price drops to ₹63,900. Strong Buy!",
                upcomingSaleCalendar = "Next sale: Diwali Dhamaka in 14 days",
                valueScore = ValueScoreBreakdown(9.4, 2.4, 3.4, 1.9, 1.7, "Exceptional value at lowest ever price across Amazon & Flipkart."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 15, 76200.0, 11, 0.0, "Real 90-day rolling price was ₹76,200. Current ₹67,900 represents genuine savings.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Overall Choice", "Top rated flagship smartphone with authentic discount"),
                    QualityBadge(QualityBadgeType.LOWEST_EVER_PRICE, "Lowest Ever Price", "Lowest price recorded in 90 days"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Zero pre-sale manipulation")
                ),
                specs = listOf("A18 Bionic Chip", "48MP Fusion Camera", "Camera Control Button", "Super Retina XDR OLED", "All-day Battery")
            ),

            Product(
                id = "prod-s24-ultra",
                title = "Samsung Galaxy S24 Ultra 5G (Titanium Gray, 12GB, 256GB)",
                category = "Electronics",
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
                    PriceHistoryPoint(30, "Sep 15", 119999.0),
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
                            CardOffer("co-s24-1", "HDFC", "Credit Card", DiscountType.FLAT, 10000.0, 10000.0, 70000.0, "HDFC10K", "Instant ₹10,000 off on HDFC Credit Cards & EMI")
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
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "Effective price drops to ₹1,09,999 with HDFC Bank card offer. Closest to historical low with bundled warranty.",
                upcomingSaleCalendar = "Diwali Electronics Blast in 12 days.",
                valueScore = ValueScoreBreakdown(9.3, 2.5, 3.2, 1.8, 1.8, "Ultimate Android camera smartphone with massive bank discount."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 11, 122500.0, 3, 0.0, "Consistent downward trend over 90 days.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Overall Flagship", "Top Android Camera & Display")
                ),
                specs = listOf("Snapdragon 8 Gen 3 for Galaxy", "200MP Quad Camera with AI Zoom", "Built-in S Pen", "Titanium Frame")
            ),

            Product(
                id = "prod-nothing-2a",
                title = "Nothing Phone (2a) 5G (Black, 128 GB, 8 GB RAM)",
                category = "Electronics",
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
                    PriceHistoryPoint(60, "Aug 15", 21999.0, true, "Freedom Sale Lowest Ever"),
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
                            CardOffer("co-np-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 1500.0, 15000.0, "SAVE10", "10% Instant Discount on HDFC Cards")
                        )
                    )
                ),
                buyWaitSignal = BuyWaitSignal.WAIT_FOR_SALE,
                buyWaitReason = "Fake pre-sale MRP inflation! Price was raised to ₹31,999 15 days ago to claim a fake 31% discount. Was ₹21,999 in August. Wait for next sale.",
                upcomingSaleCalendar = "Diwali Big Bang Sale starting in 10 days. Expected drop to ₹21,499.",
                valueScore = ValueScoreBreakdown(6.8, 1.9, 2.1, 0.9, 1.9, "Decent hardware but inflated discount. Real savings are modest."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(true, 31, 23500.0, 0, 8000.0, "⚠️ FAKE DISCOUNT DETECTED: Merchant raised the price from ₹23,999 to ₹31,999 right before the sale.", false),
                badges = listOf(
                    QualityBadge(QualityBadgeType.SASTA_HAI_PAR_RATING_KAM, "Fake Inflation Detected", "Price was artificially inflated right before sale", isWarning = true)
                ),
                specs = listOf("MediaTek Dimensity 7200 Pro", "Glyph Interface LED", "50MP Dual Camera with OIS", "120Hz Flexible AMOLED")
            ),

            Product(
                id = "prod-sony-xm5",
                title = "Sony WH-1000XM5 Wireless Noise Cancelling Headphones",
                category = "Electronics",
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
                    PriceHistoryPoint(60, "Aug 15", 24990.0, true, "Lowest Ever"),
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
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "7 Days Replacement",
                        offers = listOf(
                            CardOffer("co-xm-1", "ICICI", "Credit Card", DiscountType.FLAT, 2000.0, 2000.0, 20000.0, "ICICI2K", "Flat ₹2,000 Instant Discount")
                        )
                    )
                ),
                buyWaitSignal = BuyWaitSignal.WAIT_FOR_SALE,
                buyWaitReason = "WAIT 2 WEEKS! Price drops to ₹24,990 during festive sales (Save ₹4,000 extra).",
                upcomingSaleCalendar = "Amazon Great Indian Festival starts in 11 days.",
                valueScore = ValueScoreBreakdown(8.1, 2.4, 2.2, 1.6, 1.9, "Industry-leading ANC, but current price is ₹4,000 above festive lowest."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 17, 29200.0, 1, 0.0, "Discount is genuine against MRP, but not close to promotional floor.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best ANC Headphones", "Gold standard in noise cancellation")
                ),
                specs = listOf("Industry Leading Noise Cancellation", "Auto NC Optimizer", "30-Hour Battery Life")
            ),

            Product(
                id = "prod-cheap-soundbar",
                title = "Zebronics Turbo Bass 2.1 Bluetooth Soundbar with Subwoofer (80W)",
                category = "Electronics",
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
                        returnPolicy = "Replacement Only"
                    )
                ),
                buyWaitSignal = BuyWaitSignal.NEUTRAL,
                buyWaitReason = "⚠️ Low customer satisfaction (3.1/5 stars). Over 28% of reviews report Bluetooth dropouts.",
                upcomingSaleCalendar = "Frequently on flash sale around ₹1,800.",
                valueScore = ValueScoreBreakdown(5.2, 1.1, 3.0, 0.5, 0.6, "Cheap price but high defect rate reported."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(true, 76, 2100.0, 10, 0.0, "Artificial ₹7,999 MRP exaggeration.", false),
                badges = listOf(
                    QualityBadge(QualityBadgeType.SASTA_HAI_PAR_RATING_KAM, "Sasta Hai Par Rating Kam Hai", "Low 3.1 rating with 28% defect complaints", isWarning = true)
                ),
                specs = listOf("80W Peak Output", "Bluetooth 5.0", "Wired Subwoofer")
            ),

            // Additional Universal Popular Products
            Product(
                id = "fsh-nike-monarch",
                title = "Nike Air Monarch IV Men's Cross-Training & Walking Shoes",
                category = "Fashion",
                brand = "Nike",
                imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80",
                rating = 4.5,
                reviewCount = 18400,
                mrp = 5995.0,
                currentLowestPrice = 3499.0,
                historicalLow = 3299.0,
                historicalHigh = 4995.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 4495.0),
                    PriceHistoryPoint(60, "Aug 15", 3899.0),
                    PriceHistoryPoint(30, "Sep 15", 3599.0),
                    PriceHistoryPoint(0, "Today", 3499.0, true, "Festive Deal")
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-nik-myntra",
                        merchantName = "Myntra",
                        originalUrl = "https://www.myntra.com/casual-shoes/nike/nike-air-monarch-iv/1892010/buy",
                        currentPrice = 3499.0,
                        discountPct = 41,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "14 Days Return",
                        offers = listOf(
                            CardOffer("co-nm-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 350.0, 2000.0, "MYNTHDFC", "10% Instant Discount on HDFC Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-nik-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B000G7572M",
                        currentPrice = 3699.0,
                        discountPct = 38,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "10 Days Return",
                        offers = listOf(
                            CardOffer("co-nm-2", "SBI", "Credit Card", DiscountType.FLAT, 300.0, 300.0, 3000.0, "SBISAVE", "Flat ₹300 off on SBI Cards")
                        )
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: True 41% discount on official Nike Air cushioning. Near all-time low.",
                upcomingSaleCalendar = "Price drops below ₹3,500 only during mid-season clearance.",
                valueScore = ValueScoreBreakdown(9.3, 2.4, 3.4, 1.8, 1.7, "Legendary comfort with full-length encapsulated Air-Sole unit."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 41, 4100.0, 15, 0.0, "Verified authentic discount against Nike MRP.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Gym & Walking Shoe", "Full-length Air-Sole cushioning"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 41% off")
                ),
                specs = listOf("Full-length Air-Sole cushioning", "Leather upper for support", "Multi-surface traction outsole")
            ),

            Product(
                id = "fsh-allen-polo",
                title = "Allen Solly Men's Regular Fit Solid Pure Cotton Polo T-Shirt",
                category = "Fashion",
                brand = "Allen Solly",
                imageUrl = "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop&q=80",
                rating = 4.3,
                reviewCount = 14500,
                mrp = 1499.0,
                currentLowestPrice = 699.0,
                historicalLow = 649.0,
                historicalHigh = 1299.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 1099.0),
                    PriceHistoryPoint(60, "Aug 15", 899.0),
                    PriceHistoryPoint(30, "Sep 15", 799.0),
                    PriceHistoryPoint(0, "Today", 699.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-as-ajio",
                        merchantName = "Ajio",
                        originalUrl = "https://www.ajio.com/allen-solly-solid-polo-t-shirt/p/460111",
                        currentPrice = 699.0,
                        discountPct = 53,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "15 Days Return",
                        offers = listOf(
                            CardOffer("co-as-1", "ICICI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 100.0, 500.0, "AJIO10", "10% off with ICICI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-as-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B07P969WYZ",
                        currentPrice = 749.0,
                        discountPct = 50,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "10 Days Return",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: 53% off on 100% combed cotton premium polo.",
                upcomingSaleCalendar = "End of season sale active.",
                valueScore = ValueScoreBreakdown(9.1, 2.3, 3.3, 1.8, 1.7, "Signature stag embroidery with ribbed collar and sleeve hems."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 53, 980.0, 28, 0.0, "Authentic brand discount verified.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Casual Polo", "100% Breathable Combed Cotton")
                ),
                specs = listOf("100% Pure Cotton", "Ribbed collar and cuffs", "Regular fit", "Machine wash safe")
            ),

            Product(
                id = "grc-fortune-oil",
                title = "Fortune Sunlite Refined Sunflower Oil (5 Litre Can)",
                category = "Groceries",
                brand = "Fortune",
                imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=600&auto=format&fit=crop&q=80",
                rating = 4.6,
                reviewCount = 29400,
                mrp = 895.0,
                currentLowestPrice = 619.0,
                historicalLow = 599.0,
                historicalHigh = 780.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 720.0),
                    PriceHistoryPoint(60, "Aug 15", 680.0),
                    PriceHistoryPoint(30, "Sep 15", 640.0),
                    PriceHistoryPoint(0, "Today", 619.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-for-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/fortune-sunlite-sunflower-oil-5-l/490001201",
                        currentPrice = 619.0,
                        discountPct = 30,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Today in 2 Hours",
                        returnPolicy = "Replacement on Damaged Seal",
                        offers = listOf(
                            CardOffer("co-fo-1", "HDFC", "Debit Card", DiscountType.PERCENTAGE, 5.0, 50.0, 500.0, "JIO5", "5% Instant Savings")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-for-amz",
                        merchantName = "Amazon Fresh",
                        originalUrl = "https://www.amazon.in/dp/B00V5E482M",
                        currentPrice = 649.0,
                        discountPct = 27,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 8 AM",
                        returnPolicy = "Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Daily cooking essential enriched with Vitamins A & D under ₹125/Litre.",
                upcomingSaleCalendar = "Pantry essential with low price volatility.",
                valueScore = ValueScoreBreakdown(9.4, 2.4, 3.4, 1.8, 1.8, "Light and healthy oil with high smoke point for Indian cooking."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 30, 690.0, 10, 0.0, "Authentic FMCG cooking oil discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Cooking Oil", "Enriched with Vitamins A & D"),
                    QualityBadge(QualityBadgeType.ASLI_DISCOUNT_VERIFIED, "Asli Discount Verified", "Verified 30% saving")
                ),
                specs = listOf("Rich in Omega 6", "Contains Vitamin A & D", "High smoke point", "5 Litre Sturdy Can")
            ),

            Product(
                id = "grc-aashirvaad-atta",
                title = "Aashirvaad Superior MP Shudh Chakki Whole Wheat Atta (10 Kg)",
                category = "Groceries",
                brand = "Aashirvaad",
                imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=600&auto=format&fit=crop&q=80",
                rating = 4.7,
                reviewCount = 52000,
                mrp = 510.0,
                currentLowestPrice = 419.0,
                historicalLow = 399.0,
                historicalHigh = 480.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 460.0),
                    PriceHistoryPoint(60, "Aug 15", 440.0),
                    PriceHistoryPoint(30, "Sep 15", 430.0),
                    PriceHistoryPoint(0, "Today", 419.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-ash-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/aashirvaad-whole-wheat-atta-10-kg/490001188",
                        currentPrice = 419.0,
                        discountPct = 18,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Today in 2 Hours",
                        returnPolicy = "Replacement on Damaged Seal",
                        offers = emptyList()
                    ),
                    MerchantPrice(
                        id = "mp-ash-amz",
                        merchantName = "Amazon Fresh",
                        originalUrl = "https://www.amazon.in/dp/B00TZL545K",
                        currentPrice = 435.0,
                        discountPct = 15,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow by 9 AM",
                        returnPolicy = "Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: 100% whole wheat MP grains ground using traditional chakki process.",
                upcomingSaleCalendar = "Consistent pricing year round.",
                valueScore = ValueScoreBreakdown(9.6, 2.5, 3.5, 1.8, 1.8, "Soft fluffy rotis with rich dietary fiber and zero maida."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 18, 450.0, 7, 0.0, "Verified grocery staple discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "#1 Indian Atta Brand", "0% Maida, 100% Whole Wheat Grain")
                ),
                specs = listOf("100% MP Chakki Atta", "Locks in natural dietary fibers", "Soft rotis for up to 6 hours", "10 Kg Pouch")
            ),

            Product(
                id = "bty-minimalist-salicylic",
                title = "Minimalist 2% Salicylic Acid Face Serum with Oligopeptide for Acne & Blackheads (30 ml)",
                category = "Beauty",
                brand = "Minimalist",
                imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80",
                rating = 4.4,
                reviewCount = 31200,
                mrp = 549.0,
                currentLowestPrice = 469.0,
                historicalLow = 449.0,
                historicalHigh = 549.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 529.0),
                    PriceHistoryPoint(60, "Aug 15", 499.0),
                    PriceHistoryPoint(30, "Sep 15", 489.0),
                    PriceHistoryPoint(0, "Today", 469.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-min-nykaa",
                        merchantName = "Nykaa",
                        originalUrl = "https://www.nykaa.com/minimalist-2-salicylic-acid-serum/p/1049281",
                        currentPrice = 469.0,
                        discountPct = 15,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "15 Days Return",
                        offers = listOf(
                            CardOffer("co-min-1", "Kotak", "Credit Card", DiscountType.PERCENTAGE, 10.0, 100.0, 400.0, "NYKAA10", "10% off with Kotak Card")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-min-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B08G8J771Q",
                        currentPrice = 489.0,
                        discountPct = 11,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Tomorrow",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Effective anti-acne formulation with RonaCare Salicylic Acid and Oligopeptide.",
                upcomingSaleCalendar = "Nykaa Hot Pink Sale ongoing.",
                valueScore = ValueScoreBreakdown(9.2, 2.3, 3.3, 1.8, 1.8, "Clinically proven to reduce active acne lesions and clear clogged pores."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 15, 510.0, 8, 0.0, "Authentic skincare market discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Acne Serum", "Fragrance-free & non-comedogenic")
                ),
                specs = listOf("2% Salicylic Acid BHA", "Oligopeptide-10 anti-microbial", "Unclogs deep sebum pores", "Dermatologically tested")
            ),

            Product(
                id = "bk-psychology-money",
                title = "The Psychology of Money: Timeless Lessons on Wealth, Greed, and Happiness by Morgan Housel",
                category = "Books",
                brand = "Harriman House",
                imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=600&auto=format&fit=crop&q=80",
                rating = 4.7,
                reviewCount = 68000,
                mrp = 499.0,
                currentLowestPrice = 279.0,
                historicalLow = 249.0,
                historicalHigh = 450.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 349.0),
                    PriceHistoryPoint(60, "Aug 15", 299.0),
                    PriceHistoryPoint(30, "Sep 15", 289.0),
                    PriceHistoryPoint(0, "Today", 279.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-psyc-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/9390166268",
                        currentPrice = 279.0,
                        discountPct = 44,
                        inStock = true,
                        sellerRating = 4.9,
                        deliverySpeed = "Tomorrow by 9 AM",
                        returnPolicy = "Replacement on Damaged Copy",
                        offers = listOf(
                            CardOffer("co-bk-2", "ICICI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 50.0, 250.0, "ICICI50", "10% off with ICICI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-psyc-fk",
                        merchantName = "Flipkart",
                        originalUrl = "https://www.flipkart.com/the-psychology-of-money/p/itmbk9988",
                        currentPrice = 299.0,
                        discountPct = 40,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "2 Days",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Essential financial literacy book at ₹279 (44% discount).",
                upcomingSaleCalendar = "Price remains consistent under ₹300.",
                valueScore = ValueScoreBreakdown(9.7, 2.5, 3.5, 1.8, 1.9, "Timeless short stories exploring how people think about money."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 44, 320.0, 13, 0.0, "Verified paperback publisher discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "#1 Financial Bestseller", "Over 5 million readers worldwide")
                ),
                specs = listOf("Paperback Edition", "256 Pages", "English Language", "ISBN-13: 978-9390166268")
            ),

            Product(
                id = "ess-surf-excel-matic",
                title = "Surf Excel Matic Top Load Liquid Detergent Pouch (2 Litre)",
                category = "Daily Essentials",
                brand = "Surf Excel",
                imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=600&auto=format&fit=crop&q=80",
                rating = 4.6,
                reviewCount = 38900,
                mrp = 460.0,
                currentLowestPrice = 339.0,
                historicalLow = 319.0,
                historicalHigh = 420.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 399.0),
                    PriceHistoryPoint(60, "Aug 15", 369.0),
                    PriceHistoryPoint(30, "Sep 15", 349.0),
                    PriceHistoryPoint(0, "Today", 339.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-srf-jio",
                        merchantName = "JioMart",
                        originalUrl = "https://www.jiomart.com/p/groceries/surf-excel-matic-top-load-liquid-detergent-2-l/490001712",
                        currentPrice = 339.0,
                        discountPct = 26,
                        inStock = true,
                        sellerRating = 4.7,
                        deliverySpeed = "Today in 2 Hours",
                        returnPolicy = "Replacement if Damaged",
                        offers = listOf(
                            CardOffer("co-sr-1", "HDFC", "Debit Card", DiscountType.PERCENTAGE, 5.0, 30.0, 300.0, "JIOHDFC", "5% Instant Savings")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-srf-amz",
                        merchantName = "Amazon Fresh",
                        originalUrl = "https://www.amazon.in/dp/B07R8B192K",
                        currentPrice = 359.0,
                        discountPct = 22,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 9 AM",
                        returnPolicy = "Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Superior tough stain removal in washing machines without residue.",
                upcomingSaleCalendar = "Regular household staple.",
                valueScore = ValueScoreBreakdown(9.4, 2.4, 3.4, 1.8, 1.8, "Designed specifically for top load washing machines with 100% stain removal."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 26, 380.0, 11, 0.0, "Authentic household grocery discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Machine Detergent", "#1 Recommended by washing machine makers")
                ),
                specs = listOf("2 Litre Value Refill Pouch", "Powerful stain removal technology", "Zero residue formula", "Fresh lasting fragrance")
            ),

            Product(
                id = "elc-boat-airdopes-141",
                title = "boAt Airdopes 141 Bluetooth True Wireless Earbuds (42H Playtime, Beast Mode)",
                category = "Electronics",
                brand = "boAt",
                imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop&q=80",
                rating = 4.3,
                reviewCount = 184000,
                mrp = 4490.0,
                currentLowestPrice = 1099.0,
                historicalLow = 999.0,
                historicalHigh = 1499.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 1399.0),
                    PriceHistoryPoint(60, "Aug 15", 1199.0),
                    PriceHistoryPoint(30, "Sep 15", 1149.0),
                    PriceHistoryPoint(0, "Today", 1099.0, true, "Diwali Deal")
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-boat-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B09N3ZNHTY",
                        currentPrice = 1099.0,
                        discountPct = 76,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "7 Days Replacement",
                        offers = listOf(
                            CardOffer("co-bt-1", "HDFC", "Credit Card", DiscountType.PERCENTAGE, 10.0, 100.0, 1000.0, "HDFC10", "10% Instant Discount on HDFC Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-boat-fk",
                        merchantName = "Flipkart",
                        originalUrl = "https://www.flipkart.com/boat-airdopes-141-true-wireless/p/itm141",
                        currentPrice = 1149.0,
                        discountPct = 74,
                        inStock = true,
                        sellerRating = 4.5,
                        deliverySpeed = "2 Days",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Net price drops to ₹999 with HDFC card. Matches all-time low price.",
                upcomingSaleCalendar = "Amazon Great Indian Festival active.",
                valueScore = ValueScoreBreakdown(9.2, 2.2, 3.5, 1.8, 1.7, "Phenomenal 42-hour battery life and ENx noise-isolating mic for calls."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 76, 1280.0, 14, 0.0, "Authentic market pricing. Actual selling price floats between ₹1,099 - ₹1,399.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "#1 Best Seller TWS", "Over 180,000 verified buyer ratings"),
                    QualityBadge(QualityBadgeType.LOWEST_EVER_PRICE, "Lowest Ever Price", "Matches all-time low")
                ),
                specs = listOf("42 Hours Total Playtime", "BEAST Mode 80ms Low Latency", "ENx Environmental Noise Cancellation", "ASAP Fast Charge")
            ),

            Product(
                id = "elc-spigen-hybrid",
                title = "Spigen Ultra Hybrid Clear Matte Shockproof Case Cover for Flagship Phones",
                category = "Electronics",
                brand = "Spigen",
                imageUrl = "https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=600&auto=format&fit=crop&q=80",
                rating = 4.6,
                reviewCount = 24100,
                mrp = 2499.0,
                currentLowestPrice = 1299.0,
                historicalLow = 1199.0,
                historicalHigh = 1899.0,
                priceHistory = listOf(
                    PriceHistoryPoint(90, "Jul 15", 1599.0),
                    PriceHistoryPoint(60, "Aug 15", 1399.0),
                    PriceHistoryPoint(30, "Sep 15", 1349.0),
                    PriceHistoryPoint(0, "Today", 1299.0)
                ),
                merchantPrices = listOf(
                    MerchantPrice(
                        id = "mp-spg-amz",
                        merchantName = "Amazon",
                        originalUrl = "https://www.amazon.in/dp/B0CFRG1911",
                        currentPrice = 1299.0,
                        discountPct = 48,
                        inStock = true,
                        sellerRating = 4.8,
                        deliverySpeed = "Tomorrow by 11 AM",
                        returnPolicy = "7 Days Replacement",
                        offers = listOf(
                            CardOffer("co-spg-1", "SBI", "Credit Card", DiscountType.PERCENTAGE, 10.0, 150.0, 1000.0, "SBISAVE", "10% off with SBI Cards")
                        )
                    ),
                    MerchantPrice(
                        id = "mp-spg-fk",
                        merchantName = "Flipkart",
                        originalUrl = "https://www.flipkart.com/spigen-ultra-hybrid-case/p/itmspg",
                        currentPrice = 1349.0,
                        discountPct = 46,
                        inStock = true,
                        sellerRating = 4.6,
                        deliverySpeed = "2 Days",
                        returnPolicy = "7 Days Replacement",
                        offers = emptyList()
                    )
                ),
                buyWaitSignal = BuyWaitSignal.BUY_NOW,
                buyWaitReason = "BUY NOW: Military grade drop protection with anti-yellowing resin technology.",
                upcomingSaleCalendar = "Price holds steady at ₹1,299.",
                valueScore = ValueScoreBreakdown(9.5, 2.4, 3.5, 1.8, 1.8, "The gold standard protective case with crystal clear transparency."),
                fakeDiscountAnalysis = FakeDiscountAnalysis(false, 48, 1420.0, 9, 0.0, "Authentic premium accessory discount.", true),
                badges = listOf(
                    QualityBadge(QualityBadgeType.BEST_OVERALL_CHOICE, "Best Protective Case", "Air Cushion Technology (Mil-grade)")
                ),
                specs = listOf("Air Cushion Drop Protection", "Non-Yellowing Anti-Oxidant TPU", "Raised camera lip guard", "Wireless charging compatible")
            )
        )
    }
}
