package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class CuelinksConvertRequest(
    @param:Json(name = "url") val url: String
)

@JsonClass(generateAdapter = true)
data class CuelinksConvertResponse(
    @param:Json(name = "affiliate_url") val affiliateUrl: String? = null,
    @param:Json(name = "url") val fallbackUrl: String? = null,
    @param:Json(name = "status") val status: String? = null,
    @param:Json(name = "message") val message: String? = null
)

interface CuelinksApi {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("pub_api/v3/links/convert")
    suspend fun convertLink(
        @Header("Authorization") authToken: String,
        @Body request: CuelinksConvertRequest
    ): Response<CuelinksConvertResponse>
}
