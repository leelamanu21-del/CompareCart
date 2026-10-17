package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.MerchantPrice
import com.example.data.model.PriceAlert
import com.example.data.model.Product
import com.example.data.model.UserCard
import com.example.data.remote.ConversionResult
import com.example.data.remote.CuelinksAffiliateManager
import com.example.data.remote.GeminiHinglishAdvisor
import com.example.data.repository.PriceAlertRepository
import com.example.data.repository.ProductRepository
import com.example.data.repository.UserCardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ConversionDialogState(
    val isConverting: Boolean,
    val result: ConversionResult? = null,
    val productTitle: String,
    val merchantName: String
)

class CompareCartViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val productRepo = ProductRepository()
    private val cardRepo = UserCardRepository(db.userCardDao())
    private val alertRepo = PriceAlertRepository(db.priceAlertDao())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _asliDiscountOnly = MutableStateFlow(false)
    val asliDiscountOnly = _asliDiscountOnly.asStateFlow()

    private val _buyNowOnly = MutableStateFlow(false)
    val buyNowOnly = _buyNowOnly.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    private val _aiAdviceMessage = MutableStateFlow<String?>(null)
    val aiAdviceMessage = _aiAdviceMessage.asStateFlow()

    private val _conversionDialogState = MutableStateFlow<ConversionDialogState?>(null)
    val conversionDialogState = _conversionDialogState.asStateFlow()

    // Interactive tester state for AffiliateInfoScreen
    private val _testConversionResult = MutableStateFlow<ConversionResult?>(null)
    val testConversionResult = _testConversionResult.asStateFlow()

    private val _isTestingApi = MutableStateFlow(false)
    val isTestingApi = _isTestingApi.asStateFlow()

    val userCards: StateFlow<List<UserCard>> = cardRepo.allCards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeBankNames: StateFlow<Set<String>> = cardRepo.activeCards
        .map { list -> list.map { it.bankName }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), setOf("HDFC", "SBI"))

    val priceAlerts: StateFlow<List<PriceAlert>> = alertRepo.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProducts: StateFlow<List<Product>> = combine(
        productRepo.products,
        _searchQuery,
        _selectedCategory,
        _asliDiscountOnly,
        _buyNowOnly
    ) { products, query, category, asliOnly, buyNow ->
        val q = query.trim().lowercase()
        products.filter { p ->
            val matchQuery = if (q.isBlank()) true else {
                p.title.lowercase().contains(q) ||
                p.brand.lowercase().contains(q) ||
                p.category.lowercase().contains(q) ||
                p.specs.any { it.lowercase().contains(q) }
            }
            val matchCat = if (category == "All") true else p.category.equals(category, ignoreCase = true)
            val matchAsli = if (asliOnly) p.fakeDiscountAnalysis.isAsliDiscount else true
            val matchBuy = if (buyNow) p.buyWaitSignal == com.example.data.model.BuyWaitSignal.BUY_NOW else true

            matchQuery && matchCat && matchAsli && matchBuy
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductRepository.getSeedProducts())

    init {
        viewModelScope.launch {
            cardRepo.prepopulateDefaultCardsIfEmpty()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _aiAdviceMessage.value = null
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun toggleAsliDiscountFilter() {
        _asliDiscountOnly.value = !_asliDiscountOnly.value
    }

    fun toggleBuyNowFilter() {
        _buyNowOnly.value = !_buyNowOnly.value
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun toggleCard(cardId: String, isEnabled: Boolean) {
        viewModelScope.launch {
            cardRepo.toggleCard(cardId, isEnabled)
        }
    }

    fun addCustomCard(bankName: String, cardType: String) {
        viewModelScope.launch {
            cardRepo.addCard(
                UserCard(
                    id = "card-${bankName.lowercase()}-${System.currentTimeMillis()}",
                    bankName = bankName,
                    cardType = cardType,
                    cardNetwork = "Visa",
                    isEnabled = true
                )
            )
        }
    }

    fun deleteCard(card: UserCard) {
        viewModelScope.launch {
            cardRepo.removeCard(card)
        }
    }

    fun createPriceAlert(product: Product, targetPrice: Double) {
        viewModelScope.launch {
            alertRepo.createAlert(
                productId = product.id,
                productTitle = product.title,
                productImage = product.imageUrl,
                merchantName = product.merchantPrices.firstOrNull()?.merchantName ?: "Online",
                targetPrice = targetPrice,
                currentLowestPrice = product.currentLowestPrice,
                historicalLow = product.historicalLow
            )
        }
    }

    fun removePriceAlert(alert: PriceAlert) {
        viewModelScope.launch {
            alertRepo.removeAlert(alert)
        }
    }

    fun processVoiceHinglishQuery(queryText: String) {
        _searchQuery.value = queryText
        viewModelScope.launch {
            val result = GeminiHinglishAdvisor.analyzeQuery(queryText)
            if (result.category != null) {
                _selectedCategory.value = result.category
            }
            _aiAdviceMessage.value = result.userSummaryAdvice
        }
    }

    /**
     * Executes single master aggregator link conversion via Cuelinks API v3
     */
    fun onBuyNowClicked(product: Product, merchantPrice: MerchantPrice) {
        _conversionDialogState.value = ConversionDialogState(
            isConverting = true,
            productTitle = product.title,
            merchantName = merchantPrice.merchantName
        )

        viewModelScope.launch {
            val result = CuelinksAffiliateManager.convertMerchantLink(
                originalUrl = merchantPrice.originalUrl,
                merchantName = merchantPrice.merchantName
            )
            _conversionDialogState.value = ConversionDialogState(
                isConverting = false,
                result = result,
                productTitle = product.title,
                merchantName = merchantPrice.merchantName
            )
        }
    }

    fun testConvertUrl(url: String, merchant: String = "Test Store") {
        _isTestingApi.value = true
        _testConversionResult.value = null
        viewModelScope.launch {
            val res = CuelinksAffiliateManager.convertMerchantLink(url, merchant)
            _testConversionResult.value = res
            _isTestingApi.value = false
        }
    }

    fun dismissConversionDialog() {
        _conversionDialogState.value = null
    }
}
