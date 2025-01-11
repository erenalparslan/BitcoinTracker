package com.erenalparslan.bitcointracker.data.quotes


import com.google.gson.annotations.SerializedName

data class QutotesResponse(
    @SerializedName("data")
    var `data`: Any? = null,
    @SerializedName("status")
    var status: Status? = null
)

data class Data(
    @SerializedName("BTC")
    var coin: List<QuotesDetail?>? = null
)

data class QuotesDetail(
    @SerializedName("circulating_supply")
    var circulatingSupply: Double? = null,
    @SerializedName("cmc_rank")
    var cmcRank: Double? = null,
    @SerializedName("date_added")
    var dateAdded: String? = null,
    @SerializedName("id")
    var id: Double? = null,
    @SerializedName("infinite_supply")
    var infiniteSupply: Boolean? = null,
    @SerializedName("is_active")
    var isActive: Double? = null,
    @SerializedName("is_fiat")
    var isFiat: Double? = null,
    @SerializedName("last_updated")
    var lastUpdated: String? = null,
    @SerializedName("max_supply")
    var maxSupply: Double? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("num_market_pairs")
    var numMarketPairs: Double? = null,
    @SerializedName("platform")
    var platform: Platform? = null,
    @SerializedName("quote")
    var quote: Quote? = null,
    @SerializedName("self_reported_circulating_supply")
    var selfReportedCirculatingSupply: Double? = null,
    @SerializedName("self_reported_market_cap")
    var selfReportedMarketCap: Double? = null,
    @SerializedName("slug")
    var slug: String? = null,
    @SerializedName("symbol")
    var symbol: String? = null,
    @SerializedName("tags")
    var tags: List<Tag?>? = null,
    @SerializedName("total_supply")
    var totalSupply: Double? = null,
    @SerializedName("tvl_ratio")
    var tvlRatio: Any? = null
)

data class Platform(
    @SerializedName("id")
    var id: Double? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("slug")
    var slug: String? = null,
    @SerializedName("symbol")
    var symbol: String? = null,
    @SerializedName("token_address")
    var tokenAddress: String? = null
)

data class Quote(
    @SerializedName("USD")
    var uSD: USD? = null
)

data class USD(
    @SerializedName("fully_diluted_market_cap")
    var fullyDilutedMarketCap: Double? = null,
    @SerializedName("last_updated")
    var lastUpdated: String? = null,
    @SerializedName("market_cap")
    var marketCap: Double? = null,
    @SerializedName("market_cap_dominance")
    var marketCapDominance: Double? = null,
    @SerializedName("percent_change_1h")
    var percentChange1h: Double? = null,
    @SerializedName("percent_change_24h")
    var percentChange24h: Double? = null,
    @SerializedName("percent_change_30d")
    var percentChange30d: Double? = null,
    @SerializedName("percent_change_60d")
    var percentChange60d: Double? = null,
    @SerializedName("percent_change_7d")
    var percentChange7d: Double? = null,
    @SerializedName("percent_change_90d")
    var percentChange90d: Double? = null,
    @SerializedName("price")
    var price: Double? = null,
    @SerializedName("tvl")
    var tvl: Any? = null,
    @SerializedName("volume_24h")
    var volume24h: Double? = null,
    @SerializedName("volume_change_24h")
    var volumeChange24h: Double? = null
)


data class Tag(
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("slug")
    var slug: String? = null
)


data class Status(
    @SerializedName("credit_count")
    var creditCount: Double? = null,
    @SerializedName("elapsed")
    var elapsed: Double? = null,
    @SerializedName("error_code")
    var errorCode: Double? = null,
    @SerializedName("error_message")
    var errorMessage: Any? = null,
    @SerializedName("notice")
    var notice: Any? = null,
    @SerializedName("timestamp")
    var timestamp: String? = null
)
