package com.erenalparslan.bitcointracker.domain


import android.util.Log
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.api.ApiFactory
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.API_KEY
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.LIMIT
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.data.detail.Coin
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.erenalparslan.bitcointracker.data.home.CryptoResponse
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

    suspend fun getCryptoDetail(id: String): Flow<NetworkResult<DetailResponse>> = flow {
        try {
            emit(NetworkResult.Loading())
            Log.d("Erens", "getCryptoDetail: $id ")
            val result = apiFactory.getDetail(API_KEY, id)
            if (result.data == null) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                emit(NetworkResult.Success(result))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }

    suspend fun getQuotesDetail(id: String): Flow<NetworkResult<QutotesResponse>> = flow {
        try {
            emit(NetworkResult.Loading())
            val result = apiFactory.getDetailQuotes(API_KEY, id)
            if (result.data == null) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                emit(NetworkResult.Success(result))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }
}