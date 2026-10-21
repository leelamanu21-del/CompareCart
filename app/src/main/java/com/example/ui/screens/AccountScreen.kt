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
import com.example.data.local.UserSessionManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: CompareCartViewModel,
    sessionManager: UserSessionManager,
    onManageCardsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onAffiliateInfoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val userCards by viewModel.userCards.collectAsState()
    val priceAlerts by viewModel.priceAlerts.collectAsState()
    val userSession by sessionManager.session.collectAsState()

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Account & Profile",
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
            // User Profile Header with Live Auth State
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLightGrey),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(if (userSession.isLoggedIn) AmazonTeal else Color(0xFF64748B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userSession.avatarInitials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userSession.userName,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (userSession.userContact.isNotBlank()) userSession.userContact else "Guest Mode (Local Device)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMutedGrey
                            )
                        }

                        if (userSession.isLoggedIn) {
                            OutlinedButton(
                                onClick = { sessionManager.logout(); onLoginClick() },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("logout_button")
                            ) {
                                Text("Log Out", fontSize = 11.sp, color = AmazonRed, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = onLoginClick,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmazonNavy),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("signin_header_button")
                            ) {
                                Text("Sign In", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    if (userSession.isGuest) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Browsing as Guest. Sign in with Phone/Google to sync price alerts.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
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

            // PRIMARY SHIFT: Card Management & Saved Payment Methods in Profile
            Text(
                text = "Saved Bank Cards & Payment Methods",
                style = MaterialTheme.typography.titleSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Instant Card Discount Math",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Toggle active cards to compute net out-of-pocket prices",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMutedGrey,
                                fontSize = 11.sp
                            )
                        }

                        TextButton(
                            onClick = onManageCardsClick,
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Manage All", color = AmazonTeal, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Inline list of user cards with instant toggling
                    userCards.forEach { card ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (card.isEnabled) AmazonNavyLight else SurfaceLightGrey),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = if (card.isEnabled) AmazonOrange else TextLightGrey,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${card.bankName} ${card.cardType}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${card.cardNetwork} • Instant Discount Active",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (card.isEnabled) AsliGreen else TextLightGrey,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Switch(
                                checked = card.isEnabled,
                                onCheckedChange = { active -> viewModel.toggleCard(card.id, active) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AmazonTeal,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = BorderLight
                                ),
                                modifier = Modifier.testTag("toggle_card_${card.id}")
                            )
                        }
                        if (card != userCards.lastOrNull()) {
                            HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 2.dp))
                        }
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
                title = "Card Portfolio Settings",
                subtitle = "Add new cards, set credit limits, customize bank reward rates",
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
                        text = "CompareCart Android v2.0 • Buyhatke Style",
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
