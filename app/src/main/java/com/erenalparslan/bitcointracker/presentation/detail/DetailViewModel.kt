package com.erenalparslan.bitcointracker.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.domain.BitcoinRepository
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: BitcoinRepository) : ViewModel() {

    private val _coinDetail = MutableLiveData<DetailResponse>()
    val coinDetail: MutableLiveData<DetailResponse>
        get() = _coinDetail

    private val _quotesDetail = MutableLiveData<QutotesResponse>()
    val quotesDetail: MutableLiveData<QutotesResponse>
        get() = _quotesDetail


    fun getCoinDetail(id: String) {
        viewModelScope.launch {
            repository.getCryptoDetail(id).collect {
                when(it){
                    is NetworkResult.Error -> { Log.d("Erens", "error: ")}
                    is NetworkResult.Loading -> {
                        Log.d("Erens", "loading: ")}
                    is NetworkResult.Success ->{
                        Log.d("Erens", "succes ${it.data}: ")
                        it.data?.let { it1 -> _coinDetail.postValue(it1) }
                    }

                }

            }
        }
    }

    fun getCoinQuotesDetail(id: String) {
        viewModelScope.launch {
            repository.getQuotesDetail(id).collect {
                it.data?.let { quotesDetail ->
                    _quotesDetail.postValue(quotesDetail)
                }

            }
        }
    }

    fun addToFavorites(coinId: String) {

        repository.addFavoritesCoin(coinId)


    }
}