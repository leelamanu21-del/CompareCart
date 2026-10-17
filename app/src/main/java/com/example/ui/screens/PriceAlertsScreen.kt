package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PriceAlert
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceAlertsScreen(
    viewModel: CompareCartViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val alerts by viewModel.priceAlerts.collectAsState()
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    var simulatedNotification by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Price Drop Alerts",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("alerts_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Simulated banner if alert triggered
            if (simulatedNotification != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AsliGreenContainer,
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AsliGreen)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = AsliGreen)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("PRICE DROP NOTIFICATION", style = MaterialTheme.typography.labelSmall, color = AsliGreen, fontWeight = FontWeight.Bold)
                                Text(simulatedNotification!!, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                            }
                            IconButton(onClick = { simulatedNotification = null }) {
                                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Dismiss", tint = TextSecondary)
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = AmazonTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Real-Time Merchant Price Watch",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "CompareCart monitors Amazon, Flipkart, Myntra, and JioMart. When any verified seller breaches your target price, an instant push alert is delivered.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMutedGrey,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            if (alerts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No active price alerts",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "Tap the bell icon on any product to set your target alert",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            } else {
                items(alerts, key = { it.id }) { alert ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(
                                    model = alert.productImage,
                                    contentDescription = alert.productTitle,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceLightGrey),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = alert.productTitle,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextDark,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Current: ${currencyFormat.format(alert.currentLowestPrice)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }

                                IconButton(onClick = { viewModel.removePriceAlert(alert) }) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Remove alert",
                                        tint = TextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandBlue.copy(alpha = 0.2f),
                                        modifier = Modifier.border(1.dp, BrandCyan, RoundedCornerShape(6.dp))
                                    ) {
                                        Text(
                                            text = "Target: ${currencyFormat.format(alert.targetPrice)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BrandCyan,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Active Monitor",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AsliGreen
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        simulatedNotification = "🔥 ALERT TRIGGERED: ${alert.productTitle} just hit ${currencyFormat.format(alert.targetPrice)} on Amazon! Buy before stock runs out."
                                    }
                                ) {
                                    Text("Test Trigger", color = BrandCyan, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
