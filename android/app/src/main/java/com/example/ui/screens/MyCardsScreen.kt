package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.CompareCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCardsScreen(
    viewModel: CompareCartViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val userCards by viewModel.userCards.collectAsState()
    var showAddCardDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Bank Cards",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("cards_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddCardDialog = true },
                        modifier = Modifier.testTag("add_card_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCard,
                            contentDescription = "Add Card",
                            tint = BrandCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
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
            // Explanation Banner
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
                                    .size(36.dp)
                                    .background(BrandBlue.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = BrandCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Automated Effective Price Engine",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Zero manual math. Net prices calculate instantly.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Toggle your owned cards below. When comparing deals across Amazon, Flipkart, Tata CLiQ, and Croma, CompareCart subtracts all instant card discounts and bank promotions automatically.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Cards List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Configured Cards (${userCards.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = { showAddCardDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Card", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(userCards, key = { it.id }) { card ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (card.isEnabled) SurfaceCard else SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (card.isEnabled) Brush.linearGradient(listOf(BrandBlue, BrandCyan)) else androidx.compose.ui.graphics.SolidColor(BorderSubtle)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (card.isEnabled) BrandBlue.copy(alpha = 0.3f) else SurfaceVariantDark,
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = if (card.isEnabled) BrandCyan else TextMuted,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${card.bankName} Bank",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${card.cardType} • ${card.cardNetwork}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = card.isEnabled,
                                onCheckedChange = { active -> viewModel.toggleCard(card.id, active) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = BrandCyan,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = SurfaceVariantDark
                                )
                            )

                            IconButton(onClick = { viewModel.deleteCard(card) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Remove Card",
                                    tint = TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Live Bank Offers Today
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Live Bank Offers Available Across Stores",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                OfferHighlightCard(
                    bankName = "HDFC Bank",
                    offer = "Flat ₹4,000 to ₹10,000 Instant Discount on Apple & Samsung flagship devices across Amazon & Flipkart"
                )
            }
            item {
                OfferHighlightCard(
                    bankName = "SBI Card",
                    offer = "10% Instant Discount up to ₹2,000 on Electronics & Home Appliances across Amazon Great Indian Festival"
                )
            }
            item {
                OfferHighlightCard(
                    bankName = "ICICI Bank",
                    offer = "Flat ₹3,500 Instant Discount on Laptops and Tablets with zero-cost EMI options"
                )
            }
        }
    }

    if (showAddCardDialog) {
        AddCardDialog(
            onDismiss = { showAddCardDialog = false },
            onAddCard = { bank, type ->
                viewModel.addCustomCard(bank, type)
                showAddCardDialog = false
            }
        )
    }
}

@Composable
fun OfferHighlightCard(bankName: String, offer: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCard,
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderSubtle)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(top = 6.dp)
                    .background(AsliGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = bankName,
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandCyan,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = offer,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onAddCard: (String, String) -> Unit
) {
    val banks = listOf("HDFC", "SBI", "ICICI", "Axis", "Kotak", "OneCard", "Amex", "Bank of Baroda", "Federal")
    var selectedBank by remember { mutableStateOf(banks.first()) }
    var selectedCardType by remember { mutableStateOf("Credit Card") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Text(
                text = "Add Bank Card",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Select your bank to activate instant discount calculations:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text("Bank Name", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(modifier = Modifier.height(150.dp)) {
                    items(banks) { bank ->
                        val isSelected = (selectedBank == bank)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) BrandBlue else SurfaceVariantDark,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .border(1.dp, if (isSelected) BrandCyan else BorderSubtle, RoundedCornerShape(8.dp))
                        ) {
                            TextButton(
                                onClick = { selectedBank = bank },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "$bank Bank",
                                    color = if (isSelected) Color.White else TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Card Type", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                Row(modifier = Modifier.padding(top = 6.dp)) {
                    FilterChip(
                        selected = selectedCardType == "Credit Card",
                        onClick = { selectedCardType = "Credit Card" },
                        label = { Text("Credit Card") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = selectedCardType == "Debit Card",
                        onClick = { selectedCardType = "Debit Card" },
                        label = { Text("Debit Card") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onAddCard(selectedBank, selectedCardType) },
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text("Save Card", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
