package com.example.data.repository

import com.example.data.model.Product
import com.example.data.remote.LiveEcommerceSearchEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(UniversalProductCatalog.getAllUniversalProducts())
    val products: Flow<List<Product>> = _products.asStateFlow()

    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    /**
     * Executes real-time dynamic search across ALL product categories using Cuelinks-ready
     * merchant engines and AI live aggregator.
     */
    suspend fun searchLiveAndCached(query: String, category: String?): List<Product> {
        val q = query.trim()
        val cat = if (category == "All") null else category

        // 1. Check local universal catalog first
        val localMatches = searchLocal(q, cat)

        // 2. If query is non-empty, call live e-commerce search engine to fetch real-time items
        if (q.length >= 2) {
            val liveResults = LiveEcommerceSearchEngine.searchLiveProducts(q, cat)
            if (liveResults.isNotEmpty()) {
                _products.update { currentList ->
                    val newItems = liveResults.filter { liveItem -> currentList.none { it.title.equals(liveItem.title, ignoreCase = true) } }
                    newItems + currentList
                }
                return searchLocal(q, cat)
            }
        }

        return localMatches
    }

    private fun searchLocal(query: String, category: String?): List<Product> {
        val q = query.trim().lowercase()
        return _products.value.filter { product ->
            val matchesQuery = if (q.isBlank()) true else {
                product.title.lowercase().contains(q) ||
                product.brand.lowercase().contains(q) ||
                product.category.lowercase().contains(q) ||
                product.specs.any { it.lowercase().contains(q) }
            }

            val matchesCategory = if (category == null || category == "All") true else {
                product.category.equals(category, ignoreCase = true)
            }

            matchesQuery && matchesCategory
        }
    }

    companion object {
        fun getSeedProducts(): List<Product> = UniversalProductCatalog.getAllUniversalProducts()
    }
}
