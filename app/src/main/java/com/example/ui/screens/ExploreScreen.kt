package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class CategoryMeta(
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val popularKeywords: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: CompareCartViewModel,
    onCategorySelected: (String) -> Unit,
    onKeywordSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val categories = listOf(
        CategoryMeta(
            name = "Fashion",
            subtitle = "Sneakers, Jeans, T-Shirts, Watches",
            icon = Icons.Default.Checkroom,
            iconBg = Color(0xFFEFF6FF),
            iconTint = Color(0xFF2563EB),
            popularKeywords = listOf("Sneakers", "Polo T-Shirt", "Slim Jeans", "Fossil Watch")
        ),
        CategoryMeta(
            name = "Electronics",
            subtitle = "Mobiles, Audio, Cases, Smartwatches",
            icon = Icons.Default.Devices,
            iconBg = Color(0xFFF0FDF4),
            iconTint = Color(0xFF16A34A),
            popularKeywords = listOf("iPhone 16", "boAt Earbuds", "Phone Case", "S24 Ultra")
        ),
        CategoryMeta(
            name = "Home & Kitchen",
            subtitle = "Pressure Cookers, Air Fryers, Pans",
            icon = Icons.Default.Kitchen,
            iconBg = Color(0xFFFFF7ED),
            iconTint = Color(0xFFEA580C),
            popularKeywords = listOf("Prestige Cooker", "Air Fryer", "Non-Stick Pan", "Mixer")
        ),
        CategoryMeta(
            name = "Groceries",
            subtitle = "Basmati Rice, Almonds, Sunflower Oil",
            icon = Icons.Default.ShoppingBasket,
            iconBg = Color(0xFFFEF2F2),
            iconTint = Color(0xFFDC2626),
            popularKeywords = listOf("Daawat Rice", "Almonds", "Fortune Oil", "Atta")
        ),
        CategoryMeta(
            name = "Beauty",
            subtitle = "Face Serums, Sunscreens, Skincare",
            icon = Icons.Default.Spa,
            iconBg = Color(0xFFFAF5FF),
            iconTint = Color(0xFF9333EA),
            popularKeywords = listOf("Vitamin C", "Salicylic Acid", "Sunscreen", "Moisturizer")
        ),
        CategoryMeta(
            name = "Books",
            subtitle = "Bestsellers, Finance, Self-Help",
            icon = Icons.Default.MenuBook,
            iconBg = Color(0xFFFDF4FF),
            iconTint = Color(0xFFC026D3),
            popularKeywords = listOf("Atomic Habits", "Psychology of Money", "Ikigai", "Paperback")
        ),
        CategoryMeta(
            name = "Daily Essentials",
            subtitle = "Handwash, Detergents, Hygiene",
            icon = Icons.Default.LocalHospital,
            iconBg = Color(0xFFF0FDFA),
            iconTint = Color(0xFF0D9488),
            popularKeywords = listOf("Dettol Refill", "Surf Excel", "Toothpaste", "Sanitizer")
        )
    )

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Explore Categories",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(categories, key = { it.name }) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onCategorySelected(cat.name)
                            onCategorySelected(cat.name)
                        }
                        .testTag("explore_cat_${cat.name.lowercase().replace(" ", "_")}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(cat.iconBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.name,
                                    tint = cat.iconTint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cat.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = cat.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMutedGrey,
                                    fontSize = 11.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextLightGrey,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Keyword Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            cat.popularKeywords.take(3).forEach { kw ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SurfaceLightGrey,
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                                    ),
                                    modifier = Modifier.clickable {
                                        viewModel.onCategorySelected(cat.name)
                                        viewModel.onSearchQueryChanged(kw)
                                        onKeywordSelected(kw)
                                    }
                                ) {
                                    Text(
                                        text = kw,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AmazonTeal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
