package com.erenalparslan.bitcointracker.presentation.crypto.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentHomeBinding
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import com.erenalparslan.bitcointracker.presentation.crypto.CoinListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            recyclerView.adapter = CoinListAdapter(requireContext()) { coin ->
                findNavController().navigate(HomeFragmentDirections.actionHomeToDetailFragment(coin.symbol!!))
            }

            searchEt.addTextChangedListener { text ->
                val query = text.toString()
                filterCoins(query)
            }

            lifecycleScope.launch {
                viewModel.coins.observe(viewLifecycleOwner) {
                    val list = it.map { coin ->
                        CoinModel(
                            coin.id.toString(),
                            coin.name,
                            coin.quote?.uSD?.price,
                            coin.symbol,
                            coin.quote?.uSD?.percentChange24h.toString(),
                        )
                    }
                    (recyclerView.adapter as CoinListAdapter).submitList(list)
                }
            }
        }


    }

    private fun filterCoins(query: String) {
        lifecycleScope.launch {
            viewModel.coins.observe(viewLifecycleOwner) { coins ->
                val filteredList = coins.filter { coin ->
                    val name = coin.name ?: kotlin.run { "" }
                    val symbol = coin.symbol ?: kotlin.run { "" }
                    name.contains(query, ignoreCase = true) ||
                            symbol.contains(query, ignoreCase = true)

                }.map { coin ->
                    CoinModel(
                        coin.id.toString(),
                        coin.name,
                        coin.quote?.uSD?.price,
                        coin.symbol,
                        coin.quote?.uSD?.percentChange24h.toString(),
                    )
                }
                (binding.recyclerView.adapter as CoinListAdapter).submitList(filteredList)
            }
        }
    }
}