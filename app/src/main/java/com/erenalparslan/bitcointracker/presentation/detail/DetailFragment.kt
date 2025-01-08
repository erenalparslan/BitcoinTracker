package com.erenalparslan.bitcointracker.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.Extensions.loadImage
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.data.quotes.QuotesDetail
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.databinding.FragmentDetailBinding
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewmodel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private var coinId = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.symbol.let {
            coinId = it
        }
        viewmodel.getCoinDetail(coinId)
        viewmodel.getCoinQuotesDetail(coinId)
        with(binding) {
            viewmodel.coinDetail.observe(viewLifecycleOwner) {
                Log.d("Erens", "onViewCreated: ${it.data}")
                parseData(it)
            }
            viewmodel.quotesDetail.observe(viewLifecycleOwner) {
                parseDataQuotes(it)
            }
        }
    }

    private fun parseData(it: DetailResponse?) {
        val gson = Gson()
        val json = gson.toJson(it?.data)
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject[args.symbol] as JSONArray

        val coin = gson.fromJson(jsonArray.getJSONObject(0).toString(), CoinDetail::class.java)

        coin?.let {
            with(binding) {
                cryptoImage.loadImage(it.logo)
                coinName.text = it.name
                description.text = it.description
            }
        }
    }

    private fun parseDataQuotes(it: QutotesResponse?) {
        val gson = Gson()
        val json = gson.toJson(it?.data)
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject[args.symbol] as JSONArray

        val coin = gson.fromJson(jsonArray.getJSONObject(0).toString(), QuotesDetail::class.java)

        coin?.let {
            with(binding) {
                coinPrice.text = coin.quote?.uSD?.price.toString()
                coin24hChange.text = coin.quote?.uSD?.percentChange24h.toString()
            }
        }
    }
}