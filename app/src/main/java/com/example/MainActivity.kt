package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ProductRepository
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

enum class Screen {
    WELCOME,
    HOME,
    EXPLORE,
    DEALS,
    SAVED_ALERTS,
    ACCOUNT,
    PRODUCT_DETAIL,
    MY_CARDS,
    PRICE_ALERTS,
    AFFILIATE_INFO
}

class MainActivity : ComponentActivity() {

    private val viewModel: CompareCartViewModel by viewModels()
    private lateinit var sessionManager: com.example.data.local.UserSessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sessionManager = com.example.data.local.UserSessionManager.getInstance(this)

        setContent {
            CompareCartTheme {
                val context = androidx.compose.ui.platform.LocalContext.current
                LaunchedEffect(Unit) {
                    viewModel.openUrlEvent.collect { url ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.util.Log.e("MainActivity", "Failed to open URL: $url", e)
                        }
                    }
                }

                var currentScreen by remember {
                    mutableStateOf(if (sessionManager.hasCompletedWelcome()) Screen.HOME else Screen.WELCOME)
                }
                val selectedProduct by viewModel.selectedProduct.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundWhite,
                    bottomBar = {
                        // Show Amazon-style 5 standard navigation tabs on main screens
                        val isMainScreen = currentScreen in listOf(
                            Screen.HOME,
                            Screen.EXPLORE,
                            Screen.DEALS,
                            Screen.SAVED_ALERTS,
                            Screen.ACCOUNT
                        )

                        if (isMainScreen) {
                            NavigationBar(
                                containerColor = SurfaceWhite,
                                contentColor = TextDark,
                                tonalElevation = 8.dp,
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_bottom_nav")
                            ) {
                                // 1. Home Tab
                                NavigationBarItem(
                                    selected = currentScreen == Screen.HOME,
                                    onClick = { currentScreen = Screen.HOME },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AmazonTeal,
                                        selectedTextColor = AmazonTeal,
                                        indicatorColor = AsliGreenContainer,
                                        unselectedIconColor = TextMutedGrey,
                                        unselectedTextColor = TextMutedGrey
                                    )
                                )

                                // 2. Explore Tab (Categories)
                                NavigationBarItem(
                                    selected = currentScreen == Screen.EXPLORE,
                                    onClick = { currentScreen = Screen.EXPLORE },
                                    icon = { Icon(Icons.Default.Category, contentDescription = "Explore") },
                                    label = { Text("Explore", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AmazonTeal,
                                        selectedTextColor = AmazonTeal,
                                        indicatorColor = AsliGreenContainer,
                                        unselectedIconColor = TextMutedGrey,
                                        unselectedTextColor = TextMutedGrey
                                    )
                                )

                                // 3. Compare / Deals Tab
                                NavigationBarItem(
                                    selected = currentScreen == Screen.DEALS,
                                    onClick = { currentScreen = Screen.DEALS },
                                    icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Compare & Deals") },
                                    label = { Text("Deals", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AmazonTeal,
                                        selectedTextColor = AmazonTeal,
                                        indicatorColor = AsliGreenContainer,
                                        unselectedIconColor = TextMutedGrey,
                                        unselectedTextColor = TextMutedGrey
                                    )
                                )

                                // 4. Saved / Alerts Tab
                                NavigationBarItem(
                                    selected = currentScreen == Screen.SAVED_ALERTS,
                                    onClick = { currentScreen = Screen.SAVED_ALERTS },
                                    icon = { Icon(Icons.Default.NotificationsNone, contentDescription = "Alerts") },
                                    label = { Text("Alerts", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AmazonTeal,
                                        selectedTextColor = AmazonTeal,
                                        indicatorColor = AsliGreenContainer,
                                        unselectedIconColor = TextMutedGrey,
                                        unselectedTextColor = TextMutedGrey
                                    )
                                )

                                // 5. Account Tab
                                NavigationBarItem(
                                    selected = currentScreen == Screen.ACCOUNT,
                                    onClick = { currentScreen = Screen.ACCOUNT },
                                    icon = { Icon(Icons.Default.PersonOutline, contentDescription = "Account") },
                                    label = { Text("Account", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AmazonTeal,
                                        selectedTextColor = AmazonTeal,
                                        indicatorColor = AsliGreenContainer,
                                        unselectedIconColor = TextMutedGrey,
                                        unselectedTextColor = TextMutedGrey
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.WELCOME -> {
                                WelcomeScreen(
                                    sessionManager = sessionManager,
                                    onContinueToHome = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onProductClick = { product ->
                                        viewModel.selectProduct(product)
                                        currentScreen = Screen.PRODUCT_DETAIL
                                    },
                                    onManageCardsClick = { currentScreen = Screen.MY_CARDS },
                                    onAlertsClick = { currentScreen = Screen.SAVED_ALERTS },
                                    onAffiliateInfoClick = { currentScreen = Screen.AFFILIATE_INFO }
                                )
                            }

                            Screen.EXPLORE -> {
                                ExploreScreen(
                                    viewModel = viewModel,
                                    onCategorySelected = { currentScreen = Screen.HOME },
                                    onKeywordSelected = { currentScreen = Screen.HOME },
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.DEALS -> {
                                DealsScreen(
                                    viewModel = viewModel,
                                    onProductClick = { product ->
                                        viewModel.selectProduct(product)
                                        currentScreen = Screen.PRODUCT_DETAIL
                                    },
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.SAVED_ALERTS, Screen.PRICE_ALERTS -> {
                                PriceAlertsScreen(
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.HOME }
                                )
                            }

                            Screen.ACCOUNT -> {
                                AccountScreen(
                                    viewModel = viewModel,
                                    sessionManager = sessionManager,
                                    onManageCardsClick = { currentScreen = Screen.MY_CARDS },
                                    onAlertsClick = { currentScreen = Screen.SAVED_ALERTS },
                                    onAffiliateInfoClick = { currentScreen = Screen.AFFILIATE_INFO },
                                    onLoginClick = { currentScreen = Screen.WELCOME },
                                    onBack = { currentScreen = Screen.HOME }
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
                                    onBack = { currentScreen = Screen.ACCOUNT }
                                )
                            }

                            Screen.AFFILIATE_INFO -> {
                                AffiliateInfoScreen(
                                    viewModel = viewModel,
                                    onBack = { currentScreen = Screen.ACCOUNT }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
