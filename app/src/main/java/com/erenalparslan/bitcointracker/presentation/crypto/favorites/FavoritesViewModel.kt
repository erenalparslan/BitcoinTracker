package com.erenalparslan.bitcointracker.presentation.crypto.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erenalparslan.bitcointracker.common.NetworkResult
import com.erenalparslan.bitcointracker.data.favorites.FavoritesCoinDto
import com.erenalparslan.bitcointracker.data.home.Data
import com.erenalparslan.bitcointracker.domain.BitcoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: BitcoinRepository) :
    ViewModel() {

    private val _favoritesCoins = MutableLiveData<List<FavoritesCoinDto>>()
    val favoritesCoins: LiveData<List<FavoritesCoinDto>> get() = _favoritesCoins
    private val _allCoins = MutableLiveData<List<Data>>()
    val allCoins: LiveData<List<Data>> get() = _allCoins

    init {
        getFavoritesCoins()
        getCoinList()
    }

     fun getFavoritesCoins() {
        viewModelScope.launch {
            repository.getFavoritesCoins().collect {
                when (it) {
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        it.data?.let { data ->
                            _favoritesCoins.postValue(data)
                        }

                    }
                }
            }
        }
    }


    private fun getCoinList() {
        viewModelScope.launch {
            repository.getBitcoinData().collect {
                when (it) {
                    is NetworkResult.Error -> _allCoins.postValue(emptyList())
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> it.data?.data?.let { it1 -> _allCoins.postValue(it1) }
                }
            }
        }
    }
}