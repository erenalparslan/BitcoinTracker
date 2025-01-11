package com.erenalparslan.bitcointracker.domain


import android.util.Log
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.api.ApiFactory
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.API_KEY
import com.erenalparslan.bitcointracker.data.api.ApiFactory.Companion.LIMIT
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.erenalparslan.bitcointracker.data.favorites.FavoritesCoinDto
import com.erenalparslan.bitcointracker.data.home.CryptoResponse
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BitcoinRepository @Inject constructor(
    private val apiFactory: ApiFactory,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

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