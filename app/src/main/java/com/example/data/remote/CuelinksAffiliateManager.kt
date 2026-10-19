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

data class BackendStatus(
    val hasApiKey: Boolean = false,
    val isLiveApi: Boolean = false,
    val publisherId: String = "108920",
    val maskedKey: String = ""
)

object CuelinksAffiliateManager {
    private const val TAG = "CuelinksAffiliate"
    const val BACKEND_URL = "https://comparecartbeckend.onrender.com"
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

    suspend fun fetchBackendStatus(): BackendStatus = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url("$BACKEND_URL/api/status")
                .header("Accept", "application/json")
                .get()
                .build()
            val resp = okHttpClient.newCall(req).execute()
            if (resp.isSuccessful) {
                val body = resp.body?.string() ?: ""
                if (body.isNotBlank()) {
                    val root = JSONObject(body)
                    val hasKey = root.optBoolean("hasApiKey", false) ||
                                 root.optBoolean("hasCuelinksApiKey", false) ||
                                 root.optBoolean("liveApiMode", false) ||
                                 root.optBoolean("cuelinksApiKeySet", false)
                    val pubId = root.optString("publisherId", "108920")
                    val masked = root.optString("maskedKey", "Live on Render Backend")
                    return@withContext BackendStatus(
                        hasApiKey = hasKey,
                        isLiveApi = hasKey,
                        publisherId = pubId,
                        maskedKey = masked
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Backend status check failed: ${e.message}")
        }
        BackendStatus()
    }

    /**
     * Converts raw merchant product URL to Cuelinks monetized affiliate URL
     * via live production backend server (https://comparecartbeckend.onrender.com)
     * and Cuelinks Pub API v3.
     */
    suspend fun convertMerchantLink(
        originalUrl: String,
        merchantName: String
    ): ConversionResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val cleanedUrl = originalUrl.trim()
        val publisherId = getPublisherId()

        // 1. Try Live Production Backend Server (https://comparecartbeckend.onrender.com/api/cuelinks/convert)
        try {
            val backendJson = JSONObject().apply {
                put("url", cleanedUrl)
                put("merchant", merchantName)
            }
            val backendRequest = Request.Builder()
                .url("$BACKEND_URL/api/cuelinks/convert")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(backendJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val backendResponse = okHttpClient.newCall(backendRequest).execute()
            val latency = System.currentTimeMillis() - startTime
            val responseBody = backendResponse.body?.string() ?: ""

            if (backendResponse.isSuccessful && responseBody.isNotBlank()) {
                val rootJson = JSONObject(responseBody)
                val affiliateUrl = rootJson.optString("affiliateUrl", "").ifBlank {
                    rootJson.optString("finalUrl", "")
                }
                if (affiliateUrl.isNotBlank()) {
                    Log.d(TAG, "Successfully converted via live backend $BACKEND_URL: $affiliateUrl")
                    return@withContext ConversionResult(
                        originalUrl = cleanedUrl,
                        finalUrl = affiliateUrl,
                        isMonetized = true,
                        merchantName = merchantName,
                        message = "Live Backend ($BACKEND_URL) 200 OK — Monetized!",
                        responseCode = backendResponse.code,
                        latencyMs = latency,
                        maskedTokenUsed = "Render Backend"
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Live backend conversion error, falling back to direct conversion: ${e.message}")
        }

        val apiKey = BuildConfig.CUELINKS_API_KEY.trim()
        val maskedKey = if (apiKey.length > 8 && apiKey != "MY_CUELINKS_API_KEY") {
            "${apiKey.take(4)}...${apiKey.takeLast(4)}"
        } else {
            "Not Set"
        }

        // If no valid API key is set, format redirect via Cuelinks network publisher engine with strict URI encoding
        if (apiKey.isBlank() || apiKey == "MY_CUELINKS_API_KEY") {
            Log.d(TAG, "Using Cuelinks network redirect fallback with Publisher ID: $publisherId")
            val encodedTarget = URLEncoder.encode(cleanedUrl, StandardCharsets.UTF_8.toString())
            val smartAffiliateUrl = "https://linksredirect.com/?pub_id=$publisherId&cid=$publisherId&source=comparecart_app&url=$encodedTarget"
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
                put("channel_id", "328934")
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
                // Verify response does not contain error or unapproved status
                val hasError = rootJson.has("error") || rootJson.has("errors")
                if (!hasError) {
                    val affiliateUrl = when {
                        rootJson.has("affiliate_url") && !rootJson.isNull("affiliate_url") -> rootJson.getString("affiliate_url")
                        rootJson.has("url") && !rootJson.isNull("url") -> rootJson.getString("url")
                        rootJson.has("data") && rootJson.getJSONObject("data").has("affiliate_url") -> rootJson.getJSONObject("data").getString("affiliate_url")
                        else -> null
                    }

                    if (!affiliateUrl.isNullOrBlank() && 
                        !affiliateUrl.contains("unapproved", ignoreCase = true) && 
                        !affiliateUrl.contains("error", ignoreCase = true)) {
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
            }

            // CRUCIAL: Graceful Direct Redirect Fallback
            // If Cuelinks returns any error, unapproved channel message, or failed status,
            // DO NOT return the broken linksredirect.com URL.
            // Instead, fallback immediately and return the ORIGINAL clean product URL so the 'Buy Now' button ALWAYS opens a valid product page.
            Log.w(TAG, "Cuelinks returned error/unapproved, falling back to original clean product URL: $cleanedUrl")
            return@withContext ConversionResult(
                originalUrl = cleanedUrl,
                finalUrl = cleanedUrl,
                isMonetized = false,
                merchantName = merchantName,
                message = "Direct Clean URL (Graceful Fallback)",
                responseCode = statusCode,
                latencyMs = latency,
                maskedTokenUsed = maskedKey
            )
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            Log.w(TAG, "Cuelinks network conversion failed, falling back to clean original URL: ${e.message}")
            return@withContext ConversionResult(
                originalUrl = cleanedUrl,
                finalUrl = cleanedUrl,
                isMonetized = false,
                merchantName = merchantName,
                message = "Direct Clean URL (Offline Fallback)",
                responseCode = 200,
                latencyMs = latency,
                maskedTokenUsed = maskedKey
            )
        }
    }
}
