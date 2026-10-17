package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

data class ParsedHinglishQuery(
    val category: String? = null,
    val maxBudget: Double? = null,
    val minRating: Double? = null,
    val brandPreference: String? = null,
    val prioritizedFeature: String? = null, // e.g. "Camera", "Battery", "Value", "Discount"
    val userSummaryAdvice: String
)

object GeminiHinglishAdvisor {
    private const val TAG = "GeminiAdvisor"

    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    /**
     * Parses natural language or Hinglish voice query (e.g. "50,000 ke andar best camera phone")
     * into structured search criteria + natural advice text.
     */
    suspend fun analyzeQuery(query: String): ParsedHinglishQuery = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        // If Gemini API Key is available, we query Gemini 2.5 Flash / Flash Lite for deep understanding
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are an Indian E-Commerce Price Comparison & Smart Shopping Advisor ("Sirf Sasta Nahi, Sabse Sahi").
                    Analyze this user shopping query: "$query".
                    Extract the following JSON strictly:
                    {
                      "category": "Smartphones" | "Laptops" | "Audio" | "Appliances" | "Wearables" | "Fashion" | null,
                      "maxBudget": <number or null>,
                      "minRating": <number or null>,
                      "brandPreference": <string or null>,
                      "prioritizedFeature": "Camera" | "Battery" | "Discount" | "Value" | null,
                      "userSummaryAdvice": "<Short helpful advice in Hinglish/English under 20 words>"
                    }
                    Respond with ONLY raw JSON.
                """.trimIndent()

                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
                val jsonPayload = JSONObject().apply {
                    val contents = org.json.JSONArray().apply {
                        val partObj = JSONObject().apply {
                            put("parts", org.json.JSONArray().apply {
                                put(JSONObject().put("text", prompt))
                            })
                        }
                        put(partObj)
                    }
                    put("contents", contents)
                }

                val request = Request.Builder()
                    .url(url)
                    .post(jsonPayload.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val respBody = response.body?.string() ?: ""
                    val rootJson = JSONObject(respBody)
                    val text = rootJson.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    // Clean markdown code fence if any
                    val cleanJson = text.replace("```json", "").replace("```", "").trim()
                    val parsed = JSONObject(cleanJson)

                    return@withContext ParsedHinglishQuery(
                        category = parsed.optString("category").takeIf { it.isNotBlank() && it != "null" },
                        maxBudget = if (parsed.has("maxBudget") && !parsed.isNull("maxBudget")) parsed.optDouble("maxBudget") else null,
                        minRating = if (parsed.has("minRating") && !parsed.isNull("minRating")) parsed.optDouble("minRating") else null,
                        brandPreference = parsed.optString("brandPreference").takeIf { it.isNotBlank() && it != "null" },
                        prioritizedFeature = parsed.optString("prioritizedFeature").takeIf { it.isNotBlank() && it != "null" },
                        userSummaryAdvice = parsed.optString("userSummaryAdvice", "Found top matches matching your criteria.")
                    )
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API call failed, falling back to local semantic parser: ${e.message}")
            }
        }

        // Fast local semantic & Hinglish rules engine (zero latency, offline capable)
        return@withContext parseLocally(query)
    }

    private fun parseLocally(query: String): ParsedHinglishQuery {
        val lower = query.lowercase().trim()

        // 1. Extract budget e.g., "50,000", "50k", "under 30000", "ke andar 40000", "below 20k"
        var budget: Double? = null
        val kMatcher = Pattern.compile("(\\d+)\\s*(?:k|hazar|thousand)", Pattern.CASE_INSENSITIVE).matcher(lower)
        if (kMatcher.find()) {
            budget = (kMatcher.group(1)?.toDoubleOrNull() ?: 0.0) * 1000.0
        } else {
            val numMatcher = Pattern.compile("(?:under|below|andar|kam|tak)?\\s*₹?\\s*([0-9]{1,3}(?:,[0-9]{3})+|[0-9]{4,7})").matcher(lower)
            if (numMatcher.find()) {
                val numStr = numMatcher.group(1)?.replace(",", "")
                budget = numStr?.toDoubleOrNull()
            }
        }

        // 2. Extract Category
        val category = when {
            lower.contains("phone") || lower.contains("mobile") || lower.contains("smartphone") || lower.contains("iphone") || lower.contains("galaxy") -> "Smartphones"
            lower.contains("laptop") || lower.contains("macbook") || lower.contains("notebook") || lower.contains("computer") -> "Laptops"
            lower.contains("headphone") || lower.contains("earphone") || lower.contains("earbud") || lower.contains("airpod") || lower.contains("audio") || lower.contains("speaker") -> "Audio"
            lower.contains("ac") || lower.contains("conditioner") || lower.contains("fridge") || lower.contains("refrigerator") || lower.contains("tv") || lower.contains("vacuum") -> "Appliances"
            lower.contains("watch") || lower.contains("smartwatch") || lower.contains("band") -> "Wearables"
            lower.contains("shoe") || lower.contains("sneaker") || lower.contains("jeans") || lower.contains("shirt") -> "Fashion"
            else -> null
        }

        // 3. Extract Brand
        val brand = when {
            lower.contains("apple") || lower.contains("iphone") || lower.contains("macbook") -> "Apple"
            lower.contains("samsung") -> "Samsung"
            lower.contains("oneplus") -> "OnePlus"
            lower.contains("sony") -> "Sony"
            lower.contains("redmi") || lower.contains("xiaomi") -> "Xiaomi"
            lower.contains("nothing") -> "Nothing"
            lower.contains("lg") -> "LG"
            lower.contains("nike") -> "Nike"
            lower.contains("dyson") -> "Dyson"
            else -> null
        }

        // 4. Feature prioritization
        val feature = when {
            lower.contains("camera") || lower.contains("photo") -> "Camera"
            lower.contains("battery") || lower.contains("backup") -> "Battery"
            lower.contains("sasta") || lower.contains("cheap") || lower.contains("discount") || lower.contains("offer") -> "Discount"
            lower.contains("best") || lower.contains("sabse sahi") || lower.contains("value") -> "Value"
            else -> null
        }

        val advice = buildString {
            if (category != null) append("$category ")
            if (budget != null) append("under ₹${budget.toInt()} ")
            if (feature != null) append("optimized for $feature. ")
            if (isEmpty()) append("Comparing genuine prices across 100+ stores.")
        }

        return ParsedHinglishQuery(
            category = category,
            maxBudget = budget,
            minRating = if (lower.contains("best") || lower.contains("top")) 4.2 else null,
            brandPreference = brand,
            prioritizedFeature = feature,
            userSummaryAdvice = advice.trim()
        )
    }
}
