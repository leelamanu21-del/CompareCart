package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: CompareCartViewModel,
    onManageCardsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onAffiliateInfoClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val userCards by viewModel.userCards.collectAsState()
    val priceAlerts by viewModel.priceAlerts.collectAsState()

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Account",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Header (Amazon style)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLightGrey),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AmazonTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CC",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Hello, CompareCart Shopper",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Smart E-Commerce Price Intelligence",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMutedGrey
                        )
                    }
                }
            }

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceWhite,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onManageCardsClick)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${userCards.count { it.isEnabled }} Active",
                            style = MaterialTheme.typography.titleMedium,
                            color = AmazonTeal,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bank Cards",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMutedGrey
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceWhite,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onAlertsClick)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${priceAlerts.size} Alerts",
                            style = MaterialTheme.typography.titleMedium,
                            color = AmazonOrange,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tracked Items",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMutedGrey
                        )
                    }
                }
            }

            Text(
                text = "Account Settings & Features",
                style = MaterialTheme.typography.titleSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Settings List Items
            AccountOptionRow(
                icon = Icons.Default.CreditCard,
                title = "My Bank Cards & Instant Discounts",
                subtitle = "Manage HDFC, SBI, ICICI & Axis cards for live out-of-pocket savings",
                onClick = onManageCardsClick
            )

            AccountOptionRow(
                icon = Icons.Default.Notifications,
                title = "Price Drop Alerts",
                subtitle = "Set target prices and get alerted when deals drop below threshold",
                onClick = onAlertsClick
            )

            AccountOptionRow(
                icon = Icons.Default.Hub,
                title = "Cuelinks Affiliate Engine & Network",
                subtitle = "Transparency dashboard, live v3 API status, supported 100+ stores",
                onClick = onAffiliateInfoClick
            )

            AccountOptionRow(
                icon = Icons.Default.VerifiedUser,
                title = "Asli Discount Verification",
                subtitle = "90-day price tracking algorithm detects pre-sale artificial price hikes",
                onClick = { /* Informational */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // App Version Card
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceLightGrey,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CompareCart Android v2.0 • Amazon Style",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMutedGrey,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Real-time Multi-Merchant Engine for India",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextLightGrey,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AccountOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("account_opt_${title.take(10).lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfaceLightGrey),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AmazonTeal,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMutedGrey,
                    fontSize = 11.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextLightGrey,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
