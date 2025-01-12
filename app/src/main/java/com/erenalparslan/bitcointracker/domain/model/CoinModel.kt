package com.erenalparslan.bitcointracker.domain.model

data class CoinModel(
    val id: String,
    val name: String,
    val price: Double,
    val symbol: String,
    val quote: String,
)
