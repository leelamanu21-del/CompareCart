package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.Product
import com.example.data.repository.ProductRepository
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

enum class Screen {
    HOME,
    PRODUCT_DETAIL,
    MY_CARDS,
    PRICE_ALERTS,
    AFFILIATE_INFO
}

class MainActivity : ComponentActivity() {

    private val viewModel: CompareCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompareCartTheme {
                var currentScreen by remember { mutableStateOf(Screen.HOME) }
                val selectedProduct by viewModel.selectedProduct.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark,
                    bottomBar = {
                        // Show bottom navigation on primary screens
                        if (currentScreen != Screen.PRODUCT_DETAIL) {
                            NavigationBar(
                                containerColor = SurfaceDark,
                                contentColor = TextPrimary,
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_bottom_nav")
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == Screen.HOME,
                                    onClick = { currentScreen = Screen.HOME },
                                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Deals") },
                                    label = { Text("Compare") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BackgroundDark,
                                        selectedTextColor = BrandCyan,
                                        indicatorColor = BrandCyan,
                                        unselectedIconColor = TextSecondary,
                                        unselectedTextColor = TextSecondary
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentScreen == Screen.MY_CARDS,
                                    onClick = { currentScreen = Screen.MY_CARDS },
                                    icon = { Icon(Icons.Default.CreditCard, contentDescription = "My Cards") },
                                    label = { Text("My Cards") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BackgroundDark,
                                        selectedTextColor = BrandCyan,
                                        indicatorColor = BrandCyan,
                                        unselectedIconColor = TextSecondary,
                                        unselectedTextColor = TextSecondary
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentScreen == Screen.PRICE_ALERTS,
                                    onClick = { currentScreen = Screen.PRICE_ALERTS },
                                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
                                    label = { Text("Alerts") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BackgroundDark,
                                        selectedTextColor = BrandCyan,
                                        indicatorColor = BrandCyan,
                                        unselectedIconColor = TextSecondary,
                                        unselectedTextColor = TextSecondary
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentScreen == Screen.AFFILIATE_INFO,
                                    onClick = { currentScreen = Screen.AFFILIATE_INFO },
                                    icon = { Icon(Icons.Default.Hub, contentDescription = "Cuelinks") },
                                    label = { Text("Aggregator") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BackgroundDark,
                                        selectedTextColor = BrandCyan,
                                        indicatorColor = BrandCyan,
                                        unselectedIconColor = TextSecondary,
                                        unselectedTextColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onProductClick = { product ->
                                        viewModel.selectProduct(product)
                                        currentScreen = Screen.PRODUCT_DETAIL
                                    },
                                    onManageCardsClick = { currentScreen = Screen.MY_CARDS },
                                    onAlertsClick = { currentScreen = Screen.PRICE_ALERTS },
                                    onAffiliateInfoClick = { currentScreen = Screen.AFFILIATE_INFO }
                                )
                            }

                            Screen.PRODUCT_DETAIL -> {
                                val product = selectedProduct ?: ProductRepository.getSeedProducts().first()
                                ProductDetailScreen(
                                    product = product,
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.MY_CARDS -> {
                                MyCardsScreen(
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.PRICE_ALERTS -> {
                                PriceAlertsScreen(
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.AFFILIATE_INFO -> {
                                AffiliateInfoScreen(
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }
                        }

                        val conversionDialogState by viewModel.conversionDialogState.collectAsState()
                        if (conversionDialogState != null) {
                            com.example.ui.components.CuelinksRedirectDialog(
                                state = conversionDialogState!!,
                                onDismiss = { viewModel.dismissConversionDialog() }
                            )
                        }
                    }
                }
            }
        }
    }
}
