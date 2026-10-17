package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserCard
import com.example.ui.theme.*

@Composable
fun MyCardsFilterBar(
    userCards: List<UserCard>,
    onToggleCard: (String, Boolean) -> Unit,
    onManageCardsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = BrandCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "My Bank Cards",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(Instant Net Price)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            TextButton(
                onClick = onManageCardsClicked,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Manage Cards",
                    tint = BrandCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Manage Cards",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userCards, key = { it.id }) { card ->
                val isActive = card.isEnabled
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isActive) BrandBlue else SurfaceVariantDark,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (isActive) BrandCyan else BorderSubtle,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onToggleCard(card.id, !isActive) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isActive) AsliGreen else TextMuted,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${card.bankName} ${if (card.cardType.contains("Debit")) "DC" else "CC"}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isActive) Color.White else TextSecondary,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
