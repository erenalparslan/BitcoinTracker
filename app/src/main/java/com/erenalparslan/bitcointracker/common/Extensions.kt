package com.erenalparslan.bitcointracker.common

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load


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

    fun createPlaceHolder(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 12f
            centerRadius = 40f
            start()
        }
    }
}

