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
import com.example.data.repository.UniversalProductCatalog
import com.example.data.repository.UserCardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CompareCartViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val productRepo = ProductRepository()
    private val cardRepo = UserCardRepository(db.userCardDao())
    private val alertRepo = PriceAlertRepository(db.priceAlertDao())

    private var searchJob: Job? = null
    private var liveFetchJob: Job? = null
    private val searchCache = java.util.concurrent.ConcurrentHashMap<String, Long>()

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

    private val _isLiveSearching = MutableStateFlow(false)
    val isLiveSearching = _isLiveSearching.asStateFlow()

    private val _liveSearchStatus = MutableStateFlow<String?>(null)
    val liveSearchStatus = _liveSearchStatus.asStateFlow()

    private val _openUrlEvent = MutableSharedFlow<String>()
    val openUrlEvent = _openUrlEvent.asSharedFlow()

    // Interactive tester state for AffiliateInfoScreen
    private val _testConversionResult = MutableStateFlow<ConversionResult?>(null)
    val testConversionResult = _testConversionResult.asStateFlow()

    private val _isTestingApi = MutableStateFlow(false)
    val isTestingApi = _isTestingApi.asStateFlow()

    private val _backendStatus = MutableStateFlow<com.example.data.remote.BackendStatus?>(null)
    val backendStatus = _backendStatus.asStateFlow()

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
        val tokens = q.split("\\s+".toRegex()).filter { it.length >= 2 }
        products.filter { p ->
            val titleLower = p.title.lowercase()
            val brandLower = p.brand.lowercase()
            val catLower = p.category.lowercase()
            val specsLower = p.specs.joinToString(" ").lowercase()

            val matchQuery = if (q.isBlank()) true else {
                titleLower.contains(q) ||
                brandLower.contains(q) ||
                catLower.contains(q) ||
                specsLower.contains(q) ||
                (tokens.isNotEmpty() && tokens.any { token ->
                    titleLower.contains(token) || brandLower.contains(token) || catLower.contains(token)
                })
            }
            val matchCat = if (category == "All") true else p.category.equals(category, ignoreCase = true)
            val matchAsli = if (asliOnly) p.fakeDiscountAnalysis.isAsliDiscount else true
            val matchBuy = if (buyNow) p.buyWaitSignal == com.example.data.model.BuyWaitSignal.BUY_NOW else true

            matchQuery && matchCat && matchAsli && matchBuy
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UniversalProductCatalog.getAllUniversalProducts())

    init {
        viewModelScope.launch {
            cardRepo.prepopulateDefaultCardsIfEmpty()
        }
        fetchBackendStatus()
    }

    fun fetchBackendStatus() {
        viewModelScope.launch {
            val status = CuelinksAffiliateManager.fetchBackendStatus()
            _backendStatus.value = status
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        liveFetchJob?.cancel()

        if (query.isBlank()) {
            _aiAdviceMessage.value = null
            _liveSearchStatus.value = null
            _isLiveSearching.value = false
            return
        }

        // 500ms debounce to prevent continuous network requests while typing
        searchJob = viewModelScope.launch {
            delay(500)
            val trimmed = query.trim()
            if (trimmed.length >= 2) {
                performDynamicLiveFetch(trimmed, _selectedCategory.value)
            }
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        if (_searchQuery.value.isNotBlank()) {
            searchJob?.cancel()
            performDynamicLiveFetch(_searchQuery.value.trim(), category)
        }
    }

    fun triggerExplicitLiveSearch() {
        val q = _searchQuery.value.trim()
        if (q.isNotBlank()) {
            searchJob?.cancel()
            val cacheKey = "${q.lowercase()}_${_selectedCategory.value}"
            searchCache.remove(cacheKey)
            performDynamicLiveFetch(q, _selectedCategory.value)
        }
    }

    private fun performDynamicLiveFetch(query: String, category: String) {
        liveFetchJob?.cancel()
        liveFetchJob = viewModelScope.launch {
            val cacheKey = "${query.lowercase()}_$category"
            val lastFetched = searchCache[cacheKey] ?: 0L
            val now = System.currentTimeMillis()

            // Skip re-fetching if requested within the last 45 seconds to prevent spam
            if (now - lastFetched < 45_000L) {
                _liveSearchStatus.value = "Showing verified deals for \"$query\""
                return@launch
            }

            _isLiveSearching.value = true
            _liveSearchStatus.value = "Fetching live comparisons across Amazon, Flipkart, JioMart & Myntra..."
            try {
                // Safeguard with 3000ms timeout so network slow-downs never hang or freeze UI
                val liveItems = kotlinx.coroutines.withTimeoutOrNull(3000L) {
                    productRepo.searchLiveAndCached(query, category)
                } ?: emptyList()

                searchCache[cacheKey] = now
                if (liveItems.isNotEmpty()) {
                    _liveSearchStatus.value = "Live prices synced from 5+ merchants for \"$query\""
                } else {
                    _liveSearchStatus.value = null
                }
            } catch (e: Exception) {
                android.util.Log.w("CompareCartVM", "Search fetch interrupted: ${e.message}")
                _liveSearchStatus.value = null
            } finally {
                _isLiveSearching.value = false
            }
        }
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
            performDynamicLiveFetch(queryText, result.category ?: _selectedCategory.value)
        }
    }

    fun onBuyNowClicked(product: Product, merchantPrice: MerchantPrice) {
        viewModelScope.launch {
            val result = CuelinksAffiliateManager.convertMerchantLink(
                originalUrl = merchantPrice.originalUrl,
                merchantName = merchantPrice.merchantName
            )
            _openUrlEvent.emit(result.finalUrl)
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
}
