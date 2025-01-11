package com.erenalparslan.bitcointracker.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.Extensions.loadImage
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.data.detail.CoinDetail
import com.erenalparslan.bitcointracker.data.detail.DetailResponse
import com.erenalparslan.bitcointracker.data.quotes.QuotesDetail
import com.erenalparslan.bitcointracker.data.quotes.QutotesResponse
import com.erenalparslan.bitcointracker.databinding.FragmentDetailBinding
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
    private var coinQuotes: QuotesDetail? = null
    private var price = 0.0
    private var priceResult = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.symbol.let {
            coinId = it
        }
        viewmodel.getCoinDetail(coinId)
        viewmodel.getCoinQuotesDetail(coinId)

        viewmodel.coinDetail.observe(viewLifecycleOwner) {
            parseData(it)
        }
        viewmodel.quotesDetail.observe(viewLifecycleOwner) {
            coinQuotes = parseDataQuotes(it)
        }
        with(binding) {

            val buttons = listOf(
                coin24hChangeButton,
                coin7dChangeButton,
                coin1mChangeButton,
                coin90dChangeButton
            )

            coin24hChangeButton.isSelected = true

            buttons.forEach { button ->
                button.setOnClickListener {
                    buttons.forEach { it.isSelected = false }

                    button.isSelected = true

                    val period = when (button.id) {
                        coin24hChangeButton.id -> "1D"
                        coin7dChangeButton.id -> "1W"
                        coin1mChangeButton.id -> "1M"
                        coin90dChangeButton.id -> "90D"
                        else -> ""
                    }
                    showPercentageChange(period)
                }
            }

            btnFavorite.setOnClickListener {
                viewmodel.addToFavorites(coinId)
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

    private fun parseDataQuotes(it: QutotesResponse?): QuotesDetail {
        val gson = Gson()
        val json = gson.toJson(it?.data)
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject[args.symbol] as JSONArray

        val coin = gson.fromJson(jsonArray.getJSONObject(0).toString(), QuotesDetail::class.java)

        coin?.let {
            with(binding) {
                price = it.quote?.uSD?.price ?: 0.0
                priceResult = getString(R.string.price, price)
                coinPrice.text = priceResult
                it.quote?.uSD?.percentChange24h?.let { percentChange24h ->
                    if (percentChange24h >= 0) {
                        binding.coin24hChange.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else {
                        binding.coin24hChange.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                    }
                }
                val percentChange = it.quote?.uSD?.percentChange24h ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                coin24hChange.text = result
            }
        }
        return coin
    }

    private fun showPercentageChange(period: String) {
        when (period) {
            "1D" -> {
                coinQuotes?.quote?.uSD?.percentChange24h?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.quote?.uSD?.percentChange24h ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            "1W" -> {
                coinQuotes?.quote?.uSD?.percentChange7d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.quote?.uSD?.percentChange7d ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            "1M" -> {
                coinQuotes?.quote?.uSD?.percentChange30d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.quote?.uSD?.percentChange30d ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            "90D" -> {
                coinQuotes?.quote?.uSD?.percentChange90d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.quote?.uSD?.percentChange90d ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }
        }

    }

    private fun textColorSelector(percentChange: Double) {
        if (percentChange >= 0) {
            binding.coin24hChange.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        } else {
            binding.coin24hChange.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }
    }
}