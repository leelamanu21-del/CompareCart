package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MerchantVisualTheme(
    val brandName: String,
    val shortCode: String,
    val primaryColor: Color,
    val textColor: Color,
    val accentColor: Color
)

object MerchantBrandRegistry {
    fun getTheme(merchant: String): MerchantVisualTheme {
        val m = merchant.lowercase().trim()
        return when {
            m.contains("amazon") -> MerchantVisualTheme(
                brandName = "Amazon",
                shortCode = "a",
                primaryColor = Color(0xFFFF9900), // Amazon Prime Orange
                textColor = Color(0xFF131921),
                accentColor = Color(0xFF232F3E)
            )
            m.contains("flipkart") -> MerchantVisualTheme(
                brandName = "Flipkart",
                shortCode = "FK",
                primaryColor = Color(0xFF2874F0), // Flipkart Blue
                textColor = Color.White,
                accentColor = Color(0xFFFFE11B)
            )
            m.contains("myntra") -> MerchantVisualTheme(
                brandName = "Myntra",
                shortCode = "M",
                primaryColor = Color(0xFFE6005C), // Myntra Magenta
                textColor = Color.White,
                accentColor = Color(0xFFFF3F6C)
            )
            m.contains("jiomart") || m.contains("jio") -> MerchantVisualTheme(
                brandName = "JioMart",
                shortCode = "JM",
                primaryColor = Color(0xFF0078AD), // Jio Blue
                textColor = Color.White,
                accentColor = Color(0xFF00A651)
            )
            m.contains("tatacliq") || m.contains("tata") -> MerchantVisualTheme(
                brandName = "Tata CLiQ",
                shortCode = "TC",
                primaryColor = Color(0xFFC8102E), // Tata Red
                textColor = Color.White,
                accentColor = Color(0xFF1E1E1E)
            )
            m.contains("croma") -> MerchantVisualTheme(
                brandName = "Croma",
                shortCode = "CR",
                primaryColor = Color(0xFF00A896), // Croma Teal
                textColor = Color.White,
                accentColor = Color(0xFF028090)
            )
            m.contains("reliance") -> MerchantVisualTheme(
                brandName = "Reliance",
                shortCode = "RD",
                primaryColor = Color(0xFFE42529), // Reliance Red
                textColor = Color.White,
                accentColor = Color(0xFF005696)
            )
            m.contains("snapdeal") -> MerchantVisualTheme(
                brandName = "Snapdeal",
                shortCode = "SD",
                primaryColor = Color(0xFFE40046), // Snapdeal Crimson
                textColor = Color.White,
                accentColor = Color(0xFFB30036)
            )
            m.contains("ajio") -> MerchantVisualTheme(
                brandName = "Ajio",
                shortCode = "AJ",
                primaryColor = Color(0xFF2C4152), // Ajio Navy
                textColor = Color.White,
                accentColor = Color(0xFFDF9B28)
            )
            m.contains("nykaa") -> MerchantVisualTheme(
                brandName = "Nykaa",
                shortCode = "NY",
                primaryColor = Color(0xFFFC2779), // Nykaa Pink
                textColor = Color.White,
                accentColor = Color(0xFFE8005A)
            )
            else -> MerchantVisualTheme(
                brandName = merchant.take(12),
                shortCode = merchant.take(2).uppercase(),
                primaryColor = Color(0xFF0F172A),
                textColor = Color.White,
                accentColor = Color(0xFF38BDF8)
            )
        }
    }
}

/**
 * Compact Merchant Logo / Badge for Product Cards and Comparison Rows
 */
@Composable
fun MerchantLogoBadge(
    merchant: String,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
    showLabel: Boolean = false,
    fontSize: Int = 11
) {
    val theme = MerchantBrandRegistry.getTheme(merchant)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(theme.primaryColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = theme.shortCode,
                color = theme.textColor,
                fontWeight = FontWeight.Black,
                fontSize = (size.value * 0.45).sp
            )
        }

        if (showLabel) {
            Text(
                text = theme.brandName,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF1E293B),
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.sp
            )
        }
    }
}

/**
 * Pill-style badge for store comparisons (e.g. Amazon, Flipkart, JioMart)
 */
@Composable
fun MerchantStorePill(
    merchant: String,
    priceText: String? = null,
    isLowest: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = MerchantBrandRegistry.getTheme(merchant)

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isLowest) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
        modifier = modifier.border(
            width = if (isLowest) 1.5.dp else 1.dp,
            color = if (isLowest) Color(0xFF10B981) else Color(0xFFE2E8F0),
            shape = RoundedCornerShape(6.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(theme.primaryColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = theme.shortCode.take(1),
                    color = theme.textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                )
            }

            Text(
                text = theme.brandName,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155)
            )

            if (priceText != null) {
                Text(
                    text = priceText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLowest) Color(0xFF059669) else Color(0xFF0F172A)
                )
            }
        }
    }
}
