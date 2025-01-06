package com.erenalparslan.bitcointracker.data.api

import com.example.coinmarket.model.detail.CoinDetail
import com.example.coinmarket.model.home.CryptoResponse
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
    ): CoinDetail

    companion object {
        const val BASE_URL = "https://pro-api.coinmarketcap.com/"
        const val API_KEY = ""
        const val LIMIT = "100"
    }
}
