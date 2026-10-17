package com.example.data.repository

import com.example.data.model.Product
import com.example.data.remote.RealTimeProductSearchEngine
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
     * Executes real-time dynamic search across ALL product categories using Cuelinks-wrapped
     * merchant engines and live multi-store price comparisons.
     */
    suspend fun searchLiveAndCached(query: String, category: String?): List<Product> {
        val q = query.trim()
        val cat = if (category == "All") null else category

        if (q.length >= 2) {
            val liveResults = RealTimeProductSearchEngine.searchLiveEcommerce(q, cat)
            if (liveResults.isNotEmpty()) {
                _products.update { currentList ->
                    val newItems = liveResults.filter { liveItem ->
                        currentList.none { it.id == liveItem.id || it.title.equals(liveItem.title, ignoreCase = true) }
                    }
                    (newItems + currentList).take(60)
                }
            }
        }

        return searchLocal(q, cat)
    }

    fun searchLocal(query: String, category: String?): List<Product> {
        val q = query.trim().lowercase()
        val tokens = q.split("\\s+".toRegex()).filter { it.length >= 2 }

        return _products.value.filter { product ->
            val titleLower = product.title.lowercase()
            val brandLower = product.brand.lowercase()
            val catLower = product.category.lowercase()
            val specsLower = product.specs.joinToString(" ").lowercase()

            val matchesQuery = if (q.isBlank()) true else {
                titleLower.contains(q) ||
                brandLower.contains(q) ||
                catLower.contains(q) ||
                specsLower.contains(q) ||
                (tokens.isNotEmpty() && tokens.any { token ->
                    titleLower.contains(token) || brandLower.contains(token) || catLower.contains(token)
                })
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
