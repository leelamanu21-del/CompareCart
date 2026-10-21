package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MerchantPrice
import com.example.data.model.Product
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

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

    // Sort merchants by Net Effective Price ascending (lowest net price first)
    val sortedCalculations = product.merchantPrices.map { merchant ->
        Pair(merchant, merchant.calculateEffectivePrice(activeBankNames))
    }.sortedBy { it.second.netEffectivePrice }

    val lowestNetPrice = sortedCalculations.firstOrNull()?.second?.netEffectivePrice ?: 0.0

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Store Price Comparison",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Prices updated in real-time across verified merchants",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SurfaceVariantDark,
                modifier = Modifier.border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
            ) {
                Text(
                    text = "${sortedCalculations.size} Stores",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandCyan,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        sortedCalculations.forEachIndexed { index, (merchant, calc) ->
            val isCheapest = (calc.netEffectivePrice <= lowestNetPrice)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCheapest) SurfaceCard else SurfaceDark
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (isCheapest) {
                        Brush.linearGradient(listOf(BrandCyan, BrandBlue))
                    } else {
                        androidx.compose.ui.graphics.SolidColor(BorderSubtle)
                    }
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Top Row: Merchant Brand & Rating & Delivery
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MerchantLogoBadge(merchant = merchant.merchantName, size = 24.dp)
                            Text(
                                text = merchant.merchantName,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            if (isCheapest) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AsliGreenContainer
                                ) {
                                    Text(
                                        text = "BEST DEAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AsliGreen,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

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
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Middle Row: Price Breakdown & "My Cards" savings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            // Effective Final Price
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currencyFormat.format(calc.netEffectivePrice),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isCheapest) BrandCyan else TextPrimary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Net Price",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }

                            // Base Price and Strikethrough
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Store price: ${currencyFormat.format(calc.basePrice)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    textDecoration = if (calc.cardDiscount > 0) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }
                        }

                        // Buy Now Button with Cuelinks Icon
                        Button(
                            onClick = { onBuyNow(merchant) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCheapest) BrandBlue else SurfaceVariantDark,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Buy Now",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Monetized Cuelinks Link",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    // Card Offer Highlight
                    if (calc.appliedOffer != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AsliGreenContainer.copy(alpha = 0.6f),
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
                                    text = "Applied ${calc.appliedOffer.bankName} Card: Save ${currencyFormat.format(calc.cardDiscount)}",
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
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${merchant.deliverySpeed} • ${merchant.returnPolicy}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
