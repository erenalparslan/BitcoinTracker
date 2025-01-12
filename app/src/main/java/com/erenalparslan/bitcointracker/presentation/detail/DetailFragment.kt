package com.erenalparslan.bitcointracker.presentation.detail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.Constants.PERIOD_1D
import com.erenalparslan.bitcointracker.common.Constants.PERIOD_1M
import com.erenalparslan.bitcointracker.common.Constants.PERIOD_1W
import com.erenalparslan.bitcointracker.common.Constants.PERIOD_90D
import com.erenalparslan.bitcointracker.common.Extensions.loadImage
import com.erenalparslan.bitcointracker.common.Extensions.navigateWithAnimation
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentDetailBinding
import com.erenalparslan.bitcointracker.domain.model.CoinDetailModel
import com.erenalparslan.bitcointracker.domain.model.CoinQuotesModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewmodel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private var coinId = ""
    private var coinQuotes: CoinQuotesModel? = null
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
            it?.let {
                coinQuotes = parseDataQuotes(it)
            }

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
                        coin24hChangeButton.id -> PERIOD_1D
                        coin7dChangeButton.id -> PERIOD_1W
                        coin1mChangeButton.id -> PERIOD_1M
                        coin90dChangeButton.id -> PERIOD_90D
                        else -> ""
                    }
                    showPercentageChange(period)
                }
            }

            btnFavorite.setOnClickListener {
                btnFavorite.isSelected = !btnFavorite.isSelected
                viewmodel.addToFavorites(coinId)
            }

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            refreshPrice.setOnClickListener {
                val rotateAnimation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.anim_refresh_button)

                refreshPrice.startAnimation(rotateAnimation)
                viewmodel.getCoinQuotesDetail(coinId)

            }
        }
    }


    private fun parseData(coin: CoinDetailModel) {

        with(binding) {
            cryptoImage.loadImage(coin.logo)
            coinName.text = coin.name
            description.text = coin.description
        }

    }

    private fun parseDataQuotes(coinQuotes: CoinQuotesModel): CoinQuotesModel {


        with(binding) {
            price = coinQuotes.price
            priceResult = getString(R.string.price, price)
            coinPrice.text = priceResult

            if (coinQuotes.percentChange24h >= 0) {
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

            val percentChange = coinQuotes.percentChange24h
            val result = getString(R.string.percent_change, percentChange)
            coin24hChange.text = result
        }

        return coinQuotes
    }

    private fun showPercentageChange(period: String) {
        when (period) {
            PERIOD_1D -> {
                coinQuotes?.percentChange24h?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.percentChange24h ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            PERIOD_1W -> {
                coinQuotes?.percentChange7d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.percentChange7d ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            PERIOD_1M -> {
                coinQuotes?.percentChange30d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.percentChange30d ?: 0.0
                val result = getString(R.string.percent_change, percentChange)
                binding.coin24hChange.text = result
            }

            PERIOD_90D -> {
                coinQuotes?.percentChange90d?.let { percentChange ->
                    textColorSelector(percentChange)
                }
                val percentChange = coinQuotes?.percentChange90d ?: 0.0
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