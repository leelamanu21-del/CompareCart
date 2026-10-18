package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Product
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: CompareCartViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val activeBankNames by viewModel.activeBankNames.collectAsState()

    var showPriceAlertDialog by remember { mutableStateOf(false) }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite,
                    navigationIconContentColor = TextDark,
                    titleContentColor = TextDark,
                    actionIconContentColor = TextDark
                ),
                title = {
                    Text(
                        text = product.brand,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDark
                        )
                    }
                },
                actions = {
                    // Price Alert Bell Icon
                    IconButton(
                        onClick = { showPriceAlertDialog = true },
                        modifier = Modifier.testTag("set_alert_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Set Price Alert",
                            tint = AmazonTeal
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, "Check out ${product.title} on CompareCart: ₹${product.currentLowestPrice.toInt()}! Sirf Sasta Nahi, Sabse Sahi.")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Deal"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = TextMutedGrey
                        )
                    }
                }
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
            // Hero Image & Badges
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldStar,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${product.rating} / 5",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${product.reviewCount} customer reviews)",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMutedGrey
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Badges list
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            product.badges.forEach { badge ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (badge.isWarning) FakeWarningContainer else AsliGreenContainer,
                                    modifier = Modifier.border(
                                        1.dp,
                                        if (badge.isWarning) FakeWarningOrange else AsliGreen,
                                        RoundedCornerShape(6.dp)
                                    )
                                ) {
                                    Text(
                                        text = if (badge.isWarning) "⚠️ ${badge.title}" else "🏆 ${badge.title}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (badge.isWarning) FakeWarningOrange else AsliGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 1. "Buy vs Wait" AI Decision Signal
            item {
                BuyWaitIndicator(
                    signal = product.buyWaitSignal,
                    reason = product.buyWaitReason,
                    upcomingSaleCalendar = product.upcomingSaleCalendar
                )
            }

            // 2. Fake Discount & Inflation Detector Card
            item {
                AsliDiscountCard(analysis = product.fakeDiscountAnalysis)
            }

            // 3. Interactive 90-Day Price History Graph
            item {
                PriceHistoryGraph(
                    priceHistory = product.priceHistory,
                    historicalLow = product.historicalLow,
                    historicalHigh = product.historicalHigh,
                    currentPrice = product.currentLowestPrice
                )
            }

            // 4. Smart Value Score Card
            item {
                SmartValueScoreCard(breakdown = product.valueScore)
            }

            // 5. Store Price Comparison Matrix (with "My Cards" and Cuelinks "Buy Now")
            item {
                MerchantPriceMatrix(
                    product = product,
                    activeBankNames = activeBankNames,
                    onBuyNow = { merchant ->
                        viewModel.onBuyNowClicked(product, merchant)
                    }
                )
            }

            // 6. Key Specifications
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Key Specifications",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        product.specs.forEach { spec ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(AmazonTeal, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = spec,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPriceAlertDialog) {
        PriceAlertDialog(
            product = product,
            onDismiss = { showPriceAlertDialog = false },
            onSetAlert = { targetPrice ->
                viewModel.createPriceAlert(product, targetPrice)
                showPriceAlertDialog = false
            }
        )
    }
}
