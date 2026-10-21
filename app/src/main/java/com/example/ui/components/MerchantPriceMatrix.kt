package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MerchantPrice
import com.example.data.model.Product
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

private data class ProcessedMerchantDeal(
    val merchant: MerchantPrice,
    val netEffectivePrice: Double,
    val basePrice: Double,
    val cardDiscount: Double,
    val appliedOfferName: String?,
    val priority: Int,
    val qualityBadge: String,
    val isRecommended: Boolean
)

@Composable
fun MerchantPriceMatrix(
    product: Product,
    activeBankNames: Set<String>,
    onBuyNow: (MerchantPrice) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    // Process, prioritize and limit to Top 4 to 5 stores
    val topDeals = remember(product.merchantPrices, activeBankNames) {
        if (product.merchantPrices.isEmpty()) return@remember emptyList<ProcessedMerchantDeal>()

        // 1. Calculate effective prices & assign merchant priorities
        val calculated = product.merchantPrices.map { merchant ->
            val calc = merchant.calculateEffectivePrice(activeBankNames)
            val nameLower = merchant.merchantName.lowercase().trim()
            val (priority, badge) = when {
                nameLower.contains("amazon") -> 1 to "Trusted • Fast Delivery"
                nameLower.contains("flipkart") -> 2 to "Best Value • Assured"
                nameLower.contains("myntra") -> 2 to "100% Original Style"
                nameLower.contains("jiomart") || nameLower.contains("jio") -> 3 to "Verified Indian Store"
                nameLower.contains("tata") || nameLower.contains("cliq") -> 3 to "Tata Brand Warranty"
                nameLower.contains("croma") -> 3 to "Tata Tech Warranty"
                nameLower.contains("reliance") -> 3 to "Official Reliance Store"
                nameLower.contains("ajio") -> 3 to "Official Store"
                nameLower.contains("nykaa") -> 3 to "100% Genuine Beauty"
                nameLower.contains("snapdeal") -> 4 to "Budget Choice"
                nameLower.contains("meesho") -> 5 to "Wholesale Price"
                else -> 3 to "Verified Merchant"
            }

            Triple(merchant, calc, Pair(priority, badge))
        }

        // 2. Identify Amazon and Flipkart prices for Meesho discount threshold check
        val amazonPrice = calculated.find { it.first.merchantName.contains("amazon", ignoreCase = true) }?.second?.netEffectivePrice
        val flipkartPrice = calculated.find { it.first.merchantName.contains("flipkart", ignoreCase = true) }?.second?.netEffectivePrice
        val majorMinPrice = when {
            amazonPrice != null && flipkartPrice != null -> minOf(amazonPrice, flipkartPrice)
            amazonPrice != null -> amazonPrice
            flipkartPrice != null -> flipkartPrice
            else -> Double.MAX_VALUE
        }

        // 3. Filter Meesho unless it is >15% cheaper than major stores
        val filtered = calculated.filter { (merchant, calc, _) ->
            if (merchant.merchantName.contains("meesho", ignoreCase = true)) {
                if (majorMinPrice == Double.MAX_VALUE) true
                else calc.netEffectivePrice < (majorMinPrice * 0.85)
            } else {
                true
            }
        }

        // 4. Sort with Priority 1 (Amazon) and Priority 2 (Flipkart & Myntra) ranked first
        val sorted = filtered.sortedWith(
            compareBy<Triple<MerchantPrice, com.example.data.model.EffectivePriceCalculation, Pair<Int, String>>> { it.third.first }
                .thenBy { it.second.netEffectivePrice }
        )

        // 5. Strictly limit to Top 4 to 5 store deal options
        val topCandidates = sorted.take(5)
        if (topCandidates.isEmpty()) return@remember emptyList<ProcessedMerchantDeal>()

        // 6. Identify Single Best Deal (App Recommended) based on price & reliability
        val minNetPrice = topCandidates.minOf { it.second.netEffectivePrice }
        val amazonCandidate = topCandidates.find { it.first.merchantName.contains("amazon", ignoreCase = true) }
        val flipkartCandidate = topCandidates.find { it.first.merchantName.contains("flipkart", ignoreCase = true) }

        val recommendedMerchantId = when {
            amazonCandidate != null && amazonCandidate.second.netEffectivePrice <= minNetPrice * 1.03 -> amazonCandidate.first.id
            flipkartCandidate != null && flipkartCandidate.second.netEffectivePrice <= minNetPrice * 1.03 -> flipkartCandidate.first.id
            else -> topCandidates.minByOrNull { it.second.netEffectivePrice }?.first?.id
        }

        topCandidates.map { (merchant, calc, meta) ->
            val isRec = (merchant.id == recommendedMerchantId)
            ProcessedMerchantDeal(
                merchant = merchant,
                netEffectivePrice = calc.netEffectivePrice,
                basePrice = calc.basePrice,
                cardDiscount = calc.cardDiscount,
                appliedOfferName = calc.appliedOffer?.let { "${it.bankName} Card" },
                priority = meta.first,
                qualityBadge = meta.second,
                isRecommended = isRec
            )
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Store Price Comparison",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Stores",
                        tint = AmazonTeal,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "Live prices across Amazon, Flipkart, Myntra & top verified stores",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMutedGrey
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SurfaceLightGrey,
                modifier = Modifier.border(1.dp, BorderSubtleGrey, RoundedCornerShape(6.dp))
            ) {
                Text(
                    text = "Top ${topDeals.size} Stores",
                    style = MaterialTheme.typography.labelSmall,
                    color = AmazonNavy,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Render Top 4-5 Store Cards
        topDeals.forEachIndexed { index, deal ->
            val merchant = deal.merchant
            val isRecommended = deal.isRecommended

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .testTag("merchant_card_${merchant.merchantName.lowercase()}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRecommended) SurfaceWhite else SurfaceLightGrey.copy(alpha = 0.7f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (isRecommended) {
                        Brush.linearGradient(listOf(RecommendedGreen, AmazonTeal))
                    } else {
                        androidx.compose.ui.graphics.SolidColor(BorderLight)
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isRecommended) 2.dp else 0.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Recommendation Banner / Quality Badge Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MerchantLogoBadge(merchant = merchant.merchantName, size = 26.dp)
                            Column {
                                Text(
                                    text = merchant.merchantName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = deal.qualityBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (deal.priority <= 2) AmazonTeal else TextMutedGrey,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // App Recommended Badge (Single Best Deal) OR Star Rating
                        if (isRecommended) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RecommendedGreenContainer,
                                modifier = Modifier.border(1.dp, RecommendedGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = RecommendedGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "★ Best Value / App Recommended",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RecommendedGreen,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldStar,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${merchant.sellerRating}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Middle Row: Price Breakdown & Buy Now Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            // Net Effective Price
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currencyFormat.format(deal.netEffectivePrice),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isRecommended) RecommendedGreen else TextDark,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Net Price",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMutedGrey
                                )
                            }

                            // Base Price and Strikethrough
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Store price: ${currencyFormat.format(deal.basePrice)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMutedGrey,
                                    textDecoration = if (deal.cardDiscount > 0) TextDecoration.LineThrough else TextDecoration.None,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // COLOR-CODED BUY NOW BUTTON
                        // Recommended Best Deal: Vivid Green (#2ECC71)
                        // Other Deals (Top 2-5): Attractive Yellow / Amber (#F1C40F)
                        Button(
                            onClick = { onBuyNow(merchant) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRecommended) RecommendedGreen else DealYellow,
                                contentColor = if (isRecommended) Color.White else Color(0xFF131921)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag(
                                if (isRecommended) "buy_now_recommended_${merchant.merchantName.lowercase()}"
                                else "buy_now_${merchant.merchantName.lowercase()}"
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isRecommended) "Buy Best Deal" else "Buy Now",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isRecommended) Color.White else Color(0xFF131921)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Buy on ${merchant.merchantName}",
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isRecommended) Color.White else Color(0xFF131921)
                                )
                            }
                        }
                    }

                    // Card Offer Highlight
                    if (deal.appliedOfferName != null && deal.cardDiscount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AsliGreenContainer.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = "Card Offer",
                                    tint = AsliGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Applied ${deal.appliedOfferName}: Save ${currencyFormat.format(deal.cardDiscount)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AsliGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Delivery Speed & Return Policy
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = TextMutedGrey,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${merchant.deliverySpeed} • ${merchant.returnPolicy}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMutedGrey,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
