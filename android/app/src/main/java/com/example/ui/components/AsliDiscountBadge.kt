package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FakeDiscountAnalysis
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AsliDiscountCard(
    analysis: FakeDiscountAnalysis,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    var expanded by remember { mutableStateOf(false) }

    val containerColor = if (analysis.isFakeDiscount) FakeWarningContainer else AsliGreenContainer
    val borderColor = if (analysis.isFakeDiscount) FakeWarningOrange else AsliGreen
    val iconTint = if (analysis.isFakeDiscount) FakeWarningOrange else AsliGreen

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(borderColor))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (analysis.isFakeDiscount) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = if (analysis.isFakeDiscount) "Fake Discount Detected" else "Asli Discount Verified",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (analysis.isFakeDiscount) "FAKE DISCOUNT DETECTED!" else "ASLI DISCOUNT VERIFIED",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (analysis.isFakeDiscount) FakeWarningOrange else AsliGreen,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = if (analysis.isFakeDiscount) {
                            "Claimed ${analysis.claimedDiscountPct}% off is based on inflated pre-sale price"
                        } else {
                            "Genuine ${analysis.trueSavingsPct}% savings vs 90-day market average"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }

                TextButton(onClick = { expanded = !expanded }) {
                    Text(
                        text = if (expanded) "Hide" else "Audit Details",
                        color = BrandCyan,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = borderColor.copy(alpha = 0.4f), thickness = 0.8.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = analysis.detectionMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "90-Day Rolling Average:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = currencyFormat.format(analysis.realHistoricalAverage),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (analysis.preSalePriceHike > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pre-Sale Price Raised By:",
                                style = MaterialTheme.typography.labelSmall,
                                color = FakeWarningOrange
                            )
                            Text(
                                text = "+${currencyFormat.format(analysis.preSalePriceHike)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = FakeWarningOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
