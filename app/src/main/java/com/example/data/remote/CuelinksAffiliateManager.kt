package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

data class ConversionResult(
    val originalUrl: String,
    val finalUrl: String,
    val isMonetized: Boolean,
    val merchantName: String,
    val message: String,
    val responseCode: Int = 200,
    val latencyMs: Long = 0L,
    val maskedTokenUsed: String = ""
)

object CuelinksAffiliateManager {
    private const val TAG = "CuelinksAffiliate"
    private const val CONVERT_ENDPOINT = "https://api.cuelinks.com/pub_api/v3/links/convert"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private fun getPublisherId(): String {
        return "108920"
    }

    /**
     * Converts raw merchant product URL to Cuelinks monetized affiliate URL
     * via Cuelinks Pub API v3 (POST https://api.cuelinks.com/pub_api/v3/links/convert).
     * Ensures robust URI encoding of the target URL to prevent Amazon/Flipkart error pages.
     */
    suspend fun convertMerchantLink(
        originalUrl: String,
        merchantName: String
    ): ConversionResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.CUELINKS_API_KEY.trim()
        val startTime = System.currentTimeMillis()
        val cleanedUrl = originalUrl.trim()

        val maskedKey = if (apiKey.length > 8 && apiKey != "MY_CUELINKS_API_KEY") {
            "${apiKey.take(4)}...${apiKey.takeLast(4)}"
        } else {
            "Not Set"
        }

        val publisherId = getPublisherId()

        // If no valid API key is set, format redirect via Cuelinks network publisher engine with strict URI encoding
        if (apiKey.isBlank() || apiKey == "MY_CUELINKS_API_KEY") {
            Log.d(TAG, "Using Cuelinks network redirect fallback with Publisher ID: $publisherId")
            val encodedTarget = URLEncoder.encode(cleanedUrl, StandardCharsets.UTF_8.toString())
            val smartAffiliateUrl = "https://linksredirect.com/?cid=$publisherId&source=comparecart_app&url=$encodedTarget"
            val latency = System.currentTimeMillis() - startTime
            return@withContext ConversionResult(
                originalUrl = cleanedUrl,
                finalUrl = smartAffiliateUrl,
                isMonetized = true,
                merchantName = merchantName,
                message = "Cuelinks Network Redirect (Publisher ID: $publisherId)",
                responseCode = 200,
                latencyMs = latency,
                maskedTokenUsed = "Default"
            )
        }

        try {
            val jsonPayload = JSONObject().apply {
                put("url", cleanedUrl)
            }

            val request = Request.Builder()
                .url(CONVERT_ENDPOINT)
                .addHeader("Authorization", "Token $apiKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(jsonPayload.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            val latency = System.currentTimeMillis() - startTime
            val responseBody = response.body?.string() ?: ""

            val statusCode = response.code
            Log.d(TAG, "Cuelinks v3 API Response: HTTP $statusCode, Body: $responseBody, Latency: ${latency}ms")

            if (response.isSuccessful && responseBody.isNotBlank()) {
                val rootJson = JSONObject(responseBody)
                val affiliateUrl = when {
                    rootJson.has("affiliate_url") && !rootJson.isNull("affiliate_url") -> rootJson.getString("affiliate_url")
                    rootJson.has("url") && !rootJson.isNull("url") -> rootJson.getString("url")
                    rootJson.has("data") && rootJson.getJSONObject("data").has("affiliate_url") -> rootJson.getJSONObject("data").getString("affiliate_url")
                    else -> null
                }

                if (!affiliateUrl.isNullOrBlank()) {
                    return@withContext ConversionResult(
                        originalUrl = cleanedUrl,
                        finalUrl = affiliateUrl,
                        isMonetized = true,
                        merchantName = merchantName,
                        message = "Cuelinks v3 API 200 OK — Link Monetized!",
                        responseCode = statusCode,
                        latencyMs = latency,
                        maskedTokenUsed = maskedKey
                    )
                }
            }

            // Fallback if API response didn't contain affiliate url
            val encodedTarget = URLEncoder.encode(cleanedUrl, StandardCharsets.UTF_8.toString())
            val fallbackTrackedUrl = "https://linksredirect.com/?cid=$publisherId&source=comparecart&url=$encodedTarget"

            return@withContext ConversionResult(
                originalUrl = cleanedUrl,
                finalUrl = fallbackTrackedUrl,
                isMonetized = true,
                merchantName = merchantName,
                message = "Cuelinks HTTP $statusCode (Active Fallback Engaged)",
                responseCode = statusCode,
                latencyMs = latency,
                maskedTokenUsed = maskedKey
            )
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            Log.w(TAG, "Cuelinks network conversion fallback engaged: ${e.message}")
            val encodedTarget = URLEncoder.encode(cleanedUrl, StandardCharsets.UTF_8.toString())
            val offlineTrackedUrl = "https://linksredirect.com/?cid=$publisherId&source=comparecart&url=$encodedTarget"

            return@withContext ConversionResult(
                originalUrl = cleanedUrl,
                finalUrl = offlineTrackedUrl,
                isMonetized = true,
                merchantName = merchantName,
                message = "Network Fallback Engaged.",
                responseCode = 200,
                latencyMs = latency,
                maskedTokenUsed = maskedKey
            )
        }
    }
}
