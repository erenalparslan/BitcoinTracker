package com.erenalparslan.bitcointracker.domain


import com.erenalparslan.bitcointracker.common.Extensions.toCoinDetailModel
import com.erenalparslan.bitcointracker.common.Extensions.toCoinModel
import com.erenalparslan.bitcointracker.common.Extensions.toCoinQuotesModel
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.api.ApiFactory
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.API_KEY
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.LIMIT
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.favorites.FavoritesCoinDto
import com.erenalparslan.bitcointracker.data.quotes.QuotesDetail
import com.erenalparslan.bitcointracker.domain.model.CoinDetailModel
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import com.erenalparslan.bitcointracker.domain.model.CoinQuotesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class BitcoinRepository @Inject constructor(
    private val apiFactory: ApiFactory,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun getBitcoinData(): Flow<NetworkResult<List<CoinModel>>> = flow {
        try {
            emit(NetworkResult.Loading())
            val result = apiFactory.getData(API_KEY, LIMIT)
            if (result.data.isNullOrEmpty()) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                emit(NetworkResult.Success(result.data.toCoinModel()))
            }

        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }

    suspend fun getCryptoDetail(id: String): Flow<NetworkResult<CoinDetailModel>> = flow {
        try {
            emit(NetworkResult.Loading())
            val result = apiFactory.getDetail(API_KEY, id)
            if (result.data == null) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                val gson = Gson()
                val json = gson.toJson(result?.data)
                val jsonObject = JSONObject(json)
                val jsonArray = jsonObject[id] as JSONArray

                val coin =
                    gson.fromJson(jsonArray.getJSONObject(0).toString(), CoinDetail::class.java)

                emit(NetworkResult.Success(coin.toCoinDetailModel()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }

    suspend fun getQuotesDetail(id: String): Flow<NetworkResult<CoinQuotesModel>> = flow {
        try {
            emit(NetworkResult.Loading())
            val result = apiFactory.getDetailQuotes(API_KEY, id)
            if (result.data == null) {
                emit(NetworkResult.Error(true, "No Data!"))
            } else {
                val gson = Gson()
                val json = gson.toJson(result?.data)
                val jsonObject = JSONObject(json)
                val jsonArray = jsonObject[id] as JSONArray

                val coin =
                    gson.fromJson(jsonArray.getJSONObject(0).toString(), QuotesDetail::class.java)

                emit(NetworkResult.Success(coin.toCoinQuotesModel()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(true, e.localizedMessage))
        }
    }

    fun addFavoritesCoin(symbol: String) {
        firebaseAuth.currentUser?.uid?.let {
            firebaseFireStore.collection("users").document(it).collection("favorite_coins")
                .add(FavoritesCoinDto(symbol))
        }
    }

    suspend fun getFavoritesCoins(): Flow<NetworkResult<List<FavoritesCoinDto>>> = flow {
        try {
            firebaseAuth.currentUser?.uid?.let { userId ->
                val querySnapshot = firebaseFireStore
                    .collection("users")
                    .document(userId)
                    .collection("favorite_coins")
                    .get()
                    .await()

                val favoriteCoins = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(FavoritesCoinDto::class.java)
                }

                emit(NetworkResult.Success(favoriteCoins))
            } ?: emit(NetworkResult.Error(false, "User not logged in"))
        } catch (e: Exception) {
            emit(
                NetworkResult.Error(
                    true,
                    e.localizedMessage
                )
            )
        }
    }


}