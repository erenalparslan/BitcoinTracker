package com.erenalparslan.bitcointracker.domain


import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.api.ApiFactory
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.API_KEY
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.LIMIT
import com.example.coinmarket.model.detail.Coin
import com.example.coinmarket.model.home.CryptoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BitcoinRepository @Inject constructor(private val apiFactory: ApiFactory) {

    suspend fun getBitcoinData(): Flow<NetworkResult<CryptoResponse>> = flow {
        try {
            emit(NetworkResult.Loading())
            val result = apiFactory.getData(API_KEY, LIMIT)
            if (result.data.isNullOrEmpty()) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                emit(NetworkResult.Success(result))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }
}