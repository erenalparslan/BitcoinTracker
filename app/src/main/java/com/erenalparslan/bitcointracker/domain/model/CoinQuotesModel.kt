package com.erenalparslan.bitcointracker.domain.model

data class CoinQuotesModel(
    val price: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val percentChange30d: Double,
    val percentChange90d: Double,
)
