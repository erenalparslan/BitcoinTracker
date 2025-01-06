package com.erenalparslan.bitcointracker.presentation.crypto.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.domain.BitcoinRepository
import com.example.coinmarket.model.home.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BitcoinRepository) : ViewModel() {

    private val _coins = MutableLiveData<List<Data>>()
    val coins: LiveData<List<Data>> get() = _coins

    init {
        getCoinList()
    }

    private fun getCoinList() {
        viewModelScope.launch {
            repository.getBitcoinData().collect{
                when(it){
                    is NetworkResult.Error -> _coins.postValue(emptyList())
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> it.data?.data?.let { it1 -> _coins.postValue(it1) }
                }
            }
        }
    }

}