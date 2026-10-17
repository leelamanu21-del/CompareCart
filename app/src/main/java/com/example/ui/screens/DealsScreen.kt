package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.BuyWaitSignal
import com.example.data.model.MerchantPrice
import com.example.data.model.Product
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    viewModel: CompareCartViewModel,
    onProductClick: (Product) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val rawProducts by viewModel.filteredProducts.collectAsState()
    val activeBankNames by viewModel.activeBankNames.collectAsState()

    var filterType by remember { mutableStateOf("ALL_DEALS") } // "ALL_DEALS", "ASLI_ONLY", "BUY_NOW_ONLY"

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    val deals = remember(rawProducts, filterType) {
        val filtered = when (filterType) {
            "ASLI_ONLY" -> rawProducts.filter { it.fakeDiscountAnalysis.isAsliDiscount }
            "BUY_NOW_ONLY" -> rawProducts.filter { it.buyWaitSignal == BuyWaitSignal.BUY_NOW }
            else -> rawProducts
        }
        // Sort by biggest discount
        filtered.sortedByDescending { it.fakeDiscountAnalysis.claimedDiscountPct }
    }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            Column(modifier = Modifier.background(SurfaceWhite)) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Compare & Deals",
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                fontSize = 18.sp
                            )
                            Text(
                                "Verified price comparisons across Amazon & Flipkart",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMutedGrey,
                                fontSize = 11.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
                )

                // Filter Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = filterType == "ALL_DEALS",
                            onClick = { filterType = "ALL_DEALS" },
                            label = { Text("All Verified Deals", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmazonTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = filterType == "ASLI_ONLY",
                            onClick = { filterType = "ASLI_ONLY" },
                            leadingIcon = {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = AsliGreen, modifier = Modifier.size(14.dp))
                            },
                            label = { Text("Asli Discount Only", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AsliGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = filterType == "BUY_NOW_ONLY",
                            onClick = { filterType = "BUY_NOW_ONLY" },
                            leadingIcon = {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = AmazonOrange, modifier = Modifier.size(14.dp))
                            },
                            label = { Text("Buy Now Signals", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmazonOrange,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                HorizontalDivider(color = BorderLight, thickness = 1.dp)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
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
                        Icon(Icons.Default.TrendingDown, contentDescription = null, tint = AsliGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Prices checked across Amazon, Flipkart, Myntra & JioMart. Click Compare & Buy to lock in current rate.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextDark,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            items(deals, key = { it.id }) { product ->
                DealItemCard(
                    product = product,
                    activeBankNames = activeBankNames,
                    onClick = { onProductClick(product) },
                    onBuyNow = { merchant ->
                        viewModel.onBuyNowClicked(product, merchant)
                    }
                )
            }
        }
    }
}

@Composable
fun DealItemCard(
    product: Product,
    activeBankNames: Set<String>,
    onClick: () -> Unit,
    onBuyNow: (MerchantPrice) -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }
    }

    val bestMerchant = product.merchantPrices.minByOrNull { it.currentPrice } ?: product.merchantPrices.first()
    val discountPct = (((product.mrp - bestMerchant.currentPrice) / product.mrp) * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("deal_card_${product.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceLightGrey),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = GoldStar, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${product.rating} (${product.reviewCount})",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMutedGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currencyFormat.format(bestMerchant.currentPrice),
                            style = MaterialTheme.typography.titleMedium,
                            color = AmazonRed,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currencyFormat.format(product.mrp),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AsliGreenContainer
                        ) {
                            Text(
                                text = "$discountPct% off",
                                style = MaterialTheme.typography.labelSmall,
                                color = AsliGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Multi-Store Matrix Comparison
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceLightGrey,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        product.merchantPrices.take(3).forEach { m ->
                            val isLowest = m.id == bestMerchant.id
                            Column {
                                Text(
                                    text = m.merchantName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isLowest) AmazonTeal else TextMutedGrey,
                                    fontWeight = if (isLowest) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 10.sp
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

                    // Amazon Style Yellow Button
                    Button(
                        onClick = { onBuyNow(bestMerchant) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmazonYellow,
                            contentColor = TextDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("deal_buy_button_${product.id}")
                    ) {
                        Text("Compare & Buy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
