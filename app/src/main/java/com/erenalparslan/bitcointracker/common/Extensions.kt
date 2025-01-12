package com.erenalparslan.bitcointracker.common

import android.content.Context
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.home.Data
import com.erenalparslan.bitcointracker.data.quotes.QuotesDetail
import com.erenalparslan.bitcointracker.domain.model.CoinDetailModel
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import com.erenalparslan.bitcointracker.domain.model.CoinQuotesModel


object Extensions {
    fun setImage(imageView: ImageView, coinImage: String) {
        val imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/$coinImage.png"
        imageView.loadImage(imageUrl)
    }

    fun ImageView.loadImage(url: String?) {
        val placeholder = createPlaceHolder(this.context)
        this.load(url) {
            crossfade(true)
            crossfade(500)
            placeholder(placeholder)
        }
    }

    private fun createPlaceHolder(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 12f
            centerRadius = 40f
            start()
        }
    }

    fun List<Data>.toCoinModel() = this.map {
        CoinModel(
            id = it.id.toString() ?: "",
            name = it.name ?: "",
            price = it.quote?.uSD?.price ?: 0.0,
            symbol = it.symbol ?: "",
            quote = it.quote?.uSD?.percentChange24h.toString() ?: ""
        )

    }

    fun CoinDetail.toCoinDetailModel() = CoinDetailModel(
        id = this.id,
        description = this.description,
        category = this.category,
        logo = this.logo,
        name = this.name,
        symbol = this.symbol
    )

    fun QuotesDetail.toCoinQuotesModel() = CoinQuotesModel(
        price = this.quote?.uSD?.price ?: 0.0,
        percentChange24h = this.quote?.uSD?.percentChange24h ?: 0.0,
        percentChange7d = this.quote?.uSD?.percentChange7d ?: 0.0,
        percentChange30d = this.quote?.uSD?.percentChange30d ?: 0.0,
        percentChange90d = this.quote?.uSD?.percentChange90d ?: 0.0
    )


    fun NavController.navigateWithAnimation(
        navDirections: NavDirections
    ) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.slide_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

        navigate(navDirections, navOptions)
    }

}

