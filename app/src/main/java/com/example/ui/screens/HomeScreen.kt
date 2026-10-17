package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.components.AiVoiceSearchSheet
import com.example.ui.components.MyCardsFilterBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CompareCartViewModel,
    onProductClick: (Product) -> Unit,
    onManageCardsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onAffiliateInfoClick: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val asliDiscountOnly by viewModel.asliDiscountOnly.collectAsState()
    val buyNowOnly by viewModel.buyNowOnly.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()
    val userCards by viewModel.userCards.collectAsState()
    val activeBankNames by viewModel.activeBankNames.collectAsState()
    val aiAdviceMessage by viewModel.aiAdviceMessage.collectAsState()

    var showVoiceSheet by remember { mutableStateOf(false) }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    val categories = listOf("All", "Smartphones", "Laptops", "Audio", "Appliances")

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .statusBarsPadding()
            ) {
                // Top Brand Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Brand Shield Icon
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(BrandBlue, BrandCyan)),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "CompareCart Shield",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "COMPARECART",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Sirf Sasta Nahi, Sabse Sahi",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Cuelinks Network Transparency Button
                        IconButton(
                            onClick = onAffiliateInfoClick,
                            modifier = Modifier.testTag("cuelinks_info_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = "Cuelinks Aggregator Status",
                                tint = BrandCyan
                            )
                        }

                        // Alerts Icon
                        IconButton(
                            onClick = onAlertsClick,
                            modifier = Modifier.testTag("price_alerts_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Price Drop Alerts",
                                tint = TextPrimary
                            )
                        }
                    }
                }

                // Search Bar with Mic Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceVariantDark,
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderSubtle))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = {
                                    Text(
                                        "Search phones, laptops, deals...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextMuted
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("search_input_field")
                            )
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Voice / Hinglish Search Mic Button
                    IconButton(
                        onClick = { showVoiceSheet = true },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(BrandBlue, BrandCyan)),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .testTag("voice_search_mic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "AI Voice Search",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // "My Cards" Horizontal Quick Filter Strip
                MyCardsFilterBar(
                    userCards = userCards,
                    onToggleCard = { id, active -> viewModel.toggleCard(id, active) },
                    onManageCardsClicked = onManageCardsClick
                )

                // Category Tabs & Filter Badges
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == cat)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onCategorySelected(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandCyan,
                                selectedLabelColor = BackgroundDark,
                                containerColor = SurfaceVariantDark,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) BrandCyan else BorderSubtle
                            )
                        )
                    }

                    // Asli Discount Only toggle chip
                    item {
                        FilterChip(
                            selected = asliDiscountOnly,
                            onClick = { viewModel.toggleAsliDiscountFilter() },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = if (asliDiscountOnly) BackgroundDark else AsliGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            label = { Text("Asli Discount Only", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AsliGreen,
                                selectedLabelColor = BackgroundDark,
                                containerColor = SurfaceVariantDark,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    // Buy Now Only toggle chip
                    item {
                        FilterChip(
                            selected = buyNowOnly,
                            onClick = { viewModel.toggleBuyNowFilter() },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (buyNowOnly) BackgroundDark else FakeWarningOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            label = { Text("Buy Now Deals", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BuyNowBadge,
                                selectedLabelColor = BackgroundDark,
                                containerColor = SurfaceVariantDark,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AI Advice message banner if present
            if (aiAdviceMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceCard,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(listOf(BrandCyan, BrandBlue))
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BrandCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AI Shopping Advisor",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = aiAdviceMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Products List
            if (products.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching products found",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "Try adjusting your query or resetting filters",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            } else {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        activeBankNames = activeBankNames,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }

    if (showVoiceSheet) {
        AiVoiceSearchSheet(
            onQuerySubmitted = { query ->
                viewModel.processVoiceHinglishQuery(query)
            },
            onDismiss = { showVoiceSheet = false }
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    activeBankNames: Set<String>,
    onClick: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    // Calculate best effective price across all merchants with active cards
    val merchantCalcs = product.merchantPrices.map { it.calculateEffectivePrice(activeBankNames) }
    val bestCalc = merchantCalcs.minByOrNull { it.netEffectivePrice } ?: merchantCalcs.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderSubtle))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quality / Scam Guard Badge
                val topBadge = product.badges.firstOrNull()
                if (topBadge != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (topBadge.isWarning) FakeWarningContainer else AsliGreenContainer,
                        modifier = Modifier.border(
                            1.dp,
                            if (topBadge.isWarning) FakeWarningOrange else AsliGreen,
                            RoundedCornerShape(6.dp)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (topBadge.isWarning) "⚠️ ${topBadge.title}" else "✓ ${topBadge.title}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (topBadge.isWarning) FakeWarningOrange else AsliGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Buy vs Wait Pill
                val (pillBg, pillColor, pillText) = when (product.buyWaitSignal) {
                    BuyWaitSignal.BUY_NOW -> Triple(Color(0xFF064E3B), AsliGreen, "BUY NOW")
                    BuyWaitSignal.WAIT_FOR_SALE -> Triple(Color(0xFF451A03), WaitBadge, "WAIT 2 WEEKS")
                    BuyWaitSignal.NEUTRAL -> Triple(SurfaceVariantDark, TextSecondary, "FAIR PRICE")
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = pillBg
                ) {
                    Text(
                        text = pillText,
                        style = MaterialTheme.typography.labelSmall,
                        color = pillColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle: Image + Details
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.title,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldStar,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${product.rating} (${product.reviewCount})",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "•", color = TextMuted)
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Score: ${product.valueScore.totalScore}/10",
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Price display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currencyFormat.format(bestCalc.netEffectivePrice),
                            style = MaterialTheme.typography.titleMedium,
                            color = BrandCyan,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currencyFormat.format(product.mrp),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    if (bestCalc.cardDiscount > 0) {
                        Text(
                            text = "Includes ${currencyFormat.format(bestCalc.cardDiscount)} discount with your card",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsliGreen,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom strip: Fake discount audit or historical low notice
            HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.fakeDiscountAnalysis.isFakeDiscount) {
                    Text(
                        text = "⚠️ Fake discount detected: Pre-sale MRP hiked",
                        style = MaterialTheme.typography.labelSmall,
                        color = FakeWarningOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "🛡️ Real 90-day low: ${currencyFormat.format(product.historicalLow)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AsliGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "${product.merchantPrices.size} Stores • Tap for Graph",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandCyan
                )
            }
        }
    }
}
