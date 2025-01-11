package com.erenalparslan.bitcointracker.presentation.crypto.favorites

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmnetFavoritesBinding
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import com.erenalparslan.bitcointracker.presentation.crypto.CoinListAdapter
import com.erenalparslan.bitcointracker.presentation.crypto.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragmnet_favorites) {

    private val binding by viewBinding(FragmnetFavoritesBinding::bind)
    private val viewmodel by viewModels<FavoritesViewModel>()
    private val favoritesList = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            favoritesRw.adapter = CoinListAdapter(requireContext()) { coin ->
                findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesToDetailFragment(
                        coin.symbol!!
                    )
                )
            }

            viewmodel.favoritesCoins.observe(viewLifecycleOwner) {
                it.forEach { coin ->
                    favoritesList.add(coin.coinId)
                }
                filterCoins(favoritesList)
            }

            swipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
        }

    }


    private fun filterCoins(symbols: List<String>) {
        lifecycleScope.launch {
            viewmodel.allCoins.observe(viewLifecycleOwner) { coins ->
                val filteredList = coins.filter { coin ->
                    val symbol = coin.symbol ?: kotlin.run { "" }
                    symbols.contains(symbol)
                }.map { coin ->
                    CoinModel(
                        coin.id.toString(),
                        coin.name,
                        coin.quote?.uSD?.price,
                        coin.symbol,
                        coin.quote?.uSD?.percentChange24h.toString(),
                    )
                }
                (binding.favoritesRw.adapter as CoinListAdapter).submitList(filteredList)
            }
        }
    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                viewmodel.getFavoritesCoins()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to refresh data", Toast.LENGTH_SHORT)
                    .show()
            } finally {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}