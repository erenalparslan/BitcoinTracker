package com.erenalparslan.bitcointracker.domain.model

data class CoinModel(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val symbol: String? = null,
    val quote: String? = null
)
