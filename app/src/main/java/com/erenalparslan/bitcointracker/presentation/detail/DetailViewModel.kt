package com.erenalparslan.bitcointracker.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.domain.BitcoinRepository
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.erenalparslan.bitcointracker.domain.model.CoinDetailModel
import com.erenalparslan.bitcointracker.domain.model.CoinQuotesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: BitcoinRepository) : ViewModel() {

    private val _coinDetail = MutableLiveData<CoinDetailModel>()
    val coinDetail: MutableLiveData<CoinDetailModel>
        get() = _coinDetail

    private val _quotesDetail = MutableLiveData<CoinQuotesModel>()
    val quotesDetail: MutableLiveData<CoinQuotesModel>
        get() = _quotesDetail


    fun getCoinDetail(id: String) {
        viewModelScope.launch {
            repository.getCryptoDetail(id).collect {
                when (it) {
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        it.data?.let { it1 -> _coinDetail.postValue(it1) }
                    }
                }

            }
        }
    }

    fun getCoinQuotesDetail(id: String) {
        viewModelScope.launch {
            repository.getQuotesDetail(id).collect {
                when(it){
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        it.data?.let { it1 -> _quotesDetail.postValue(it1) }
                    }
                }
            }
        }
    }

    fun addToFavorites(coinId: String) {

        repository.addFavoritesCoin(coinId)


    }
}