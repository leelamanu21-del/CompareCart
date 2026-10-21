package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.data.model.*
import com.example.ui.components.AiVoiceSearchSheet
import com.example.ui.components.MerchantLogoBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel
import java.text.NumberFormat
import java.util.Locale

enum class SortOption(val label: String) {
    BEST_VALUE("Featured"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    BIGGEST_DISCOUNT("Biggest Discount"),
    TOP_RATED("Avg. Customer Review")
}

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
    val rawProducts by viewModel.filteredProducts.collectAsState()
    val userCards by viewModel.userCards.collectAsState()
    val activeBankNames by viewModel.activeBankNames.collectAsState()
    val aiAdviceMessage by viewModel.aiAdviceMessage.collectAsState()
    val isLiveSearching by viewModel.isLiveSearching.collectAsState()
    val liveSearchStatus by viewModel.liveSearchStatus.collectAsState()

    var showVoiceSheet by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf(SortOption.BEST_VALUE) }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    // Popular real-time search queries requested by user
    val popularSearches = listOf(
        "Shoes",
        "T-Shirt",
        "Rice",
        "Mobile Case",
        "Headphones",
        "Air Fryer",
        "Smartwatch",
        "Sunscreen",
        "Pressure Cooker",
        "Atomic Habits"
    )

    // Comprehensive universal product categories
    val categories = listOf(
        "All",
        "Fashion",
        "Home & Kitchen",
        "Groceries",
        "Beauty",
        "Books",
        "Daily Essentials",
        "Electronics"
    )

    // Sort products based on selected option
    val products = remember(rawProducts, currentSort, activeBankNames) {
        when (currentSort) {
            SortOption.BEST_VALUE -> rawProducts.sortedByDescending { it.valueScore.totalScore }
            SortOption.PRICE_LOW_TO_HIGH -> rawProducts.sortedBy { p ->
                val best = p.merchantPrices.minOfOrNull { it.calculateEffectivePrice(activeBankNames).netEffectivePrice } ?: p.currentLowestPrice
                best
            }
            SortOption.BIGGEST_DISCOUNT -> rawProducts.sortedByDescending { it.fakeDiscountAnalysis.claimedDiscountPct }
            SortOption.TOP_RATED -> rawProducts.sortedByDescending { it.rating }
        }
    }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite)
                    .statusBarsPadding()
            ) {
                // Amazon Style Clean Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(AmazonNavyLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "CC",
                                color = AmazonOrange,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CompareCart",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextDark,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = ".in",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = AmazonOrange,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Compare Amazon, Flipkart & Myntra",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMutedGrey,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onAlertsClick,
                            modifier = Modifier.testTag("price_alerts_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = "Alerts",
                                tint = TextDark
                            )
                        }
                        IconButton(
                            onClick = onAffiliateInfoClick,
                            modifier = Modifier.testTag("cuelinks_info_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = "Aggregator Network",
                                tint = AmazonTeal
                            )
                        }
                    }
                }

                // Amazon Style Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceWhite,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(BorderSubtleGrey)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = {
                                    Text(
                                        "Search CompareCart.in (shoes, t-shirt, rice...)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMutedGrey,
                                        fontSize = 13.sp
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = TextDark,
                                    unfocusedTextColor = TextDark
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
                                        tint = TextMutedGrey,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Voice / Hinglish Search Mic Button (Amazon Warm Accent)
                    IconButton(
                        onClick = { showVoiceSheet = true },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AmazonYellow)
                            .testTag("voice_search_mic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "AI Voice Search",
                            tint = TextDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Auto-Suggestions Strip
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Text(
                            text = "Suggestions:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMutedGrey,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(popularSearches) { term ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceLightGrey,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                            ),
                            modifier = Modifier.clickable {
                                viewModel.onSearchQueryChanged(term)
                            }
                        ) {
                            Text(
                                text = term,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextDark,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Live search loading banner
                AnimatedVisibility(visible = isLiveSearching) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = AmazonOrange,
                        trackColor = SurfaceLightGrey
                    )
                }

                if (liveSearchStatus != null && !isLiveSearching) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AsliGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = liveSearchStatus!!,
                            style = MaterialTheme.typography.labelSmall,
                            color = AsliGreen,
                            fontSize = 11.sp
                        )
                    }
                }

                // Multi-Store Live Intelligence Bar (Clean, clutter-free store ticker)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceLightGrey,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Live Stores:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMutedGrey,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            listOf("Amazon", "Flipkart", "Myntra", "JioMart", "Tata CLiQ", "Croma").forEach { store ->
                                MerchantLogoBadge(merchant = store, size = 16.dp)
                            }
                        }

                        Text(
                            text = "⚡ Live Sync",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmazonTeal,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Category Navigation Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == cat)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onCategorySelected(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmazonNavyLight,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceLightGrey,
                                labelColor = TextDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) AmazonNavyLight else BorderLight
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = asliDiscountOnly,
                            onClick = { viewModel.toggleAsliDiscountFilter() },
                            leadingIcon = {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = if (asliDiscountOnly) Color.White else AsliGreen, modifier = Modifier.size(14.dp))
                            },
                            label = { Text("Asli Discount", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AsliGreen,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceLightGrey,
                                labelColor = TextDark
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = buyNowOnly,
                            onClick = { viewModel.toggleBuyNowFilter() },
                            leadingIcon = {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = if (buyNowOnly) Color.White else AmazonOrange, modifier = Modifier.size(14.dp))
                            },
                            label = { Text("Buy Now Deals", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmazonOrange,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceLightGrey,
                                labelColor = TextDark
                            )
                        )
                    }
                }

                HorizontalDivider(color = BorderDivider, thickness = 1.dp)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Results & Sort Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1-${products.size} of over ${products.size} results for \"${if (searchQuery.isNotBlank()) searchQuery else selectedCategory}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMutedGrey,
                        fontSize = 12.sp
                    )

                    // Sort Filter
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sort: ",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMutedGrey
                        )
                        Text(
                            text = currentSort.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = AmazonTeal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                currentSort = when (currentSort) {
                                    SortOption.BEST_VALUE -> SortOption.PRICE_LOW_TO_HIGH
                                    SortOption.PRICE_LOW_TO_HIGH -> SortOption.BIGGEST_DISCOUNT
                                    SortOption.BIGGEST_DISCOUNT -> SortOption.TOP_RATED
                                    SortOption.TOP_RATED -> SortOption.BEST_VALUE
                                }
                            }
                        )
                    }
                }
            }

            // AI Shopping Advisor message if present
            if (aiAdviceMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceLightGrey,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmazonTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = aiAdviceMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextDark
                            )
                        }
                    }
                }
            }

            // Products List
            if (products.isEmpty()) {
                item {
                    if (isLiveSearching) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = AmazonOrange, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Searching Amazon, Flipkart & Myntra live...",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.SearchOff, contentDescription = null, tint = TextMutedGrey, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No results found for \"$searchQuery\"",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try checking your spelling or use general keywords like shoes, rice, t-shirt...",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMutedGrey
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    viewModel.onSearchQueryChanged("")
                                    viewModel.onCategorySelected("All")
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Reset Search & Filters", color = AmazonTeal)
                            }
                        }
                    }
                }
            } else {
                items(products, key = { it.id }) { product ->
                    AmazonStyleProductCard(
                        product = product,
                        activeBankNames = activeBankNames,
                        onClick = { onProductClick(product) },
                        onQuickBuy = { merchant ->
                            viewModel.onBuyNowClicked(product, merchant)
                        }
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

/**
 * Simplified, clean, familiar Amazon-Style Product Card:
 * - High-res image
 * - Clear title & star ratings
 * - Live prices across Amazon/Flipkart/Myntra
 * - "Compare & Buy" CTA button
 */
@Composable
fun AmazonStyleProductCard(
    product: Product,
    activeBankNames: Set<String>,
    onClick: () -> Unit,
    onQuickBuy: (MerchantPrice) -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    val merchantCalcs = product.merchantPrices.map { it to it.calculateEffectivePrice(activeBankNames) }
    val bestPair = merchantCalcs.minByOrNull { it.second.netEffectivePrice } ?: merchantCalcs.first()
    val bestMerchant = bestPair.first
    val bestCalc = bestPair.second
    val discountPct = (((product.mrp - bestMerchant.currentPrice) / product.mrp) * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.fakeDiscountAnalysis.isFakeDiscount) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = FakeWarningContainer
                    ) {
                        Text(
                            text = "⚠️ Pre-Sale Price Hike Detected",
                            style = MaterialTheme.typography.labelSmall,
                            color = FakeWarningOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AsliGreenContainer
                    ) {
                        Text(
                            text = "✓ Asli Discount Verified",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsliGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (product.buyWaitSignal == BuyWaitSignal.BUY_NOW) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AsliGreenContainer
                    ) {
                        Text(
                            text = "Best Time to Buy",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsliGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Product Row
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .size(95.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceLightGrey),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextDark,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Rating Stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldStar,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${product.reviewCount.toIndianK()})",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmazonTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Price Section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currencyFormat.format(bestCalc.netEffectivePrice),
                            style = MaterialTheme.typography.titleLarge,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "M.R.P: ${currencyFormat.format(product.mrp)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMutedGrey,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "($discountPct% off)",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmazonRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    if (bestCalc.cardDiscount > 0) {
                        Text(
                            text = "Save extra ${currencyFormat.format(bestCalc.cardDiscount)} with your card",
                            style = MaterialTheme.typography.labelSmall,
                            color = AsliGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Amazon Style Side-by-Side Merchant Comparison Matrix
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceLightGrey,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        product.merchantPrices.take(3).forEach { m ->
                            val isLowest = (m.id == bestMerchant.id)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                MerchantLogoBadge(merchant = m.merchantName, size = 16.dp)
                                Column {
                                    Text(
                                        text = m.merchantName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isLowest) AmazonTeal else TextMutedGrey,
                                        fontSize = 10.sp,
                                        fontWeight = if (isLowest) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = currencyFormat.format(m.currentPrice),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isLowest) AsliGreen else TextDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Amazon Yellow Action Button
                    Button(
                        onClick = { onQuickBuy(bestMerchant) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmazonYellow,
                            contentColor = TextDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("quick_buy_button_${product.id}")
                    ) {
                        Text("Compare & Buy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun Int.toIndianK(): String {
    return if (this >= 1000) {
        String.format(Locale.US, "%.1fk", this / 1000.0)
    } else {
        this.toString()
    }
}
