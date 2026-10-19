package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffiliateInfoScreen(
    viewModel: CompareCartViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val testResult by viewModel.testConversionResult.collectAsState()
    val isTesting by viewModel.isTestingApi.collectAsState()
    val backendStatus by viewModel.backendStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchBackendStatus()
    }

    val rawApiKey = BuildConfig.CUELINKS_API_KEY.trim()
    val hasLocalApiKey = rawApiKey.isNotBlank() && rawApiKey != "MY_CUELINKS_API_KEY"
    val hasBackendApiKey = backendStatus?.hasApiKey == true || backendStatus?.isLiveApi == true
    val hasApiKey = hasLocalApiKey || hasBackendApiKey

    val maskedKey = when {
        hasLocalApiKey && rawApiKey.length > 8 -> "${rawApiKey.take(4)}...${rawApiKey.takeLast(4)}"
        hasBackendApiKey && !backendStatus?.maskedKey.isNullOrBlank() && backendStatus?.maskedKey != "Not Set" -> backendStatus!!.maskedKey
        hasBackendApiKey -> "Live on Render Backend"
        hasLocalApiKey -> "Configured (${rawApiKey.length} chars)"
        else -> "Not Set"
    }

    var testUrlInput by remember {
        mutableStateOf("https://www.amazon.in/dp/B0DGH7XZK6")
    }

    val supportedMerchants = listOf(
        "Amazon India" to "Electronics, Mobiles, Books & Appliances",
        "Flipkart" to "Smartphones, Fashion, Smart TVs",
        "Croma Retail" to "Laptops, Home Audio, ACs & Refrigerator",
        "Reliance Digital" to "Consumer Tech & In-store pickup",
        "Tata CLiQ" to "Luxury electronics, Watches & Footwear",
        "Myntra" to "Fashion, Sneakers & Sportswear",
        "Ajio" to "Streetwear & Premium Apparel"
    )

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Master Aggregator (Cuelinks v3)",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("affiliate_back_button")) {
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
            // Cuelinks API v3 Master Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(BrandCyan, BrandBlue))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        brush = Brush.linearGradient(listOf(BrandBlue, BrandCyan)),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hub,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Single Aggregator Architecture",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cuelinks Pub API v3 Link Monetization",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandCyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "CompareCart uses Cuelinks API v3 as the SINGLE MASTER NETWORK AGGREGATOR. When you tap 'Buy Now' on any product, original store URLs are dynamically converted to tracked affiliate URLs via:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceDark,
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderSubtle)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "POST https://api.cuelinks.com/pub_api/v3/links/convert\nHeaders: Authorization: Token \$CUELINKS_API_KEY",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandCyan,
                                modifier = Modifier.padding(10.dp),
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Status Chip
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (hasApiKey) AsliGreenContainer else SurfaceVariantDark,
                            modifier = Modifier.border(
                                1.dp,
                                if (hasApiKey) AsliGreen else BorderSubtle,
                                RoundedCornerShape(6.dp)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (hasApiKey) AsliGreen else FakeWarningOrange, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (hasApiKey) "Active Cuelinks Token Injected: $maskedKey" else "Fallback Mode Active (No Key in Secrets)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (hasApiKey) AsliGreen else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Live API v3 Test Tool
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(BorderSubtle, BrandCyan.copy(alpha = 0.5f)))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = BrandCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Live Cuelinks v3 API Conversion Test",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Test converting any merchant product link using your active CUELINKS_API_KEY:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = testUrlInput,
                            onValueChange = { testUrlInput = it },
                            label = { Text("Product URL") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = BrandCyan,
                                unfocusedBorderColor = BorderSubtle,
                                focusedLabelColor = BrandCyan
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (testUrlInput.isNotBlank()) {
                                    viewModel.testConvertUrl(testUrlInput.trim(), "Amazon")
                                }
                            },
                            enabled = !isTesting,
                            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("test_convert_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (isTesting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Calling Cuelinks API v3...", color = Color.White)
                            } else {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Execute Convert API Call", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Test Results Card
                        if (testResult != null) {
                            val res = testResult!!
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SurfaceDark,
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(if (res.responseCode == 200) AsliGreen else FakeWarningOrange)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "HTTP ${res.responseCode}: ${res.message}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (res.responseCode == 200) AsliGreen else FakeWarningOrange,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${res.latencyMs} ms",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BrandCyan,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Converted Affiliate URL:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = res.finalUrl,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BrandCyan,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 11.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.finalUrl))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Opening link...", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Open", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("Cuelinks Link", res.finalUrl))
                                                Toast.makeText(context, "Copied converted link!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Copy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Supported Network Merchants
            item {
                Text(
                    text = "Supported Indian Merchant Networks (100+)",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            items(supportedMerchants) { (merchant, description) ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderSubtle)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(SurfaceVariantDark, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = BrandCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = merchant,
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
