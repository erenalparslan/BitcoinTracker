package com.erenalparslan.bitcointracker.data.api

import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.erenalparslan.bitcointracker.data.home.CryptoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiFactory {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getData(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("limit") limit: String
    ): CryptoResponse

    @GET("v2/cryptocurrency/info")
    suspend fun getDetail(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbol: String
    ): DetailResponse


    @GET("/v2/cryptocurrency/quotes/latest")
    suspend fun getDetailQuotes(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbol: String
    ): QutotesResponse

    companion object {
        const val BASE_URL = "https://pro-api.coinmarketcap.com/"
        const val API_KEY = "8c62a913-a3c0-4952-a8e8-6f9330ce0605"
        const val LIMIT = "100"
    }
}
