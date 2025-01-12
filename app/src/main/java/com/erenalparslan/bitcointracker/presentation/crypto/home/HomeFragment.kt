package com.erenalparslan.bitcointracker.presentation.crypto.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.Extensions.navigateWithAnimation
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentHomeBinding
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
                findNavController().navigateWithAnimation(
                    HomeFragmentDirections.actionHomeToDetailFragment(
                        coin.symbol
                    )
                )
            }

            searchEt.addTextChangedListener { text ->
                val query = text.toString()
                filterCoins(query)
            }

            lifecycleScope.launch {
                viewModel.coins.observe(viewLifecycleOwner) {

                    (recyclerView.adapter as CoinListAdapter).submitList(it)
                }
            }
        }


    }

    private fun filterCoins(query: String) {
        lifecycleScope.launch {
            viewModel.coins.observe(viewLifecycleOwner) { coins ->
                val filteredList = coins.filter { coin ->
                    val name = coin.name
                    val symbol = coin.symbol
                    name.contains(query, ignoreCase = true) ||
                            symbol.contains(query, ignoreCase = true)

                }
                (binding.recyclerView.adapter as CoinListAdapter).submitList(filteredList)
            }
        }
    }
}