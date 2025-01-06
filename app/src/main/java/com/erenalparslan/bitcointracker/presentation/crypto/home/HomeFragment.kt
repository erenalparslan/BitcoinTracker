package com.erenalparslan.bitcointracker.presentation.crypto.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentHomeBinding
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import com.erenalparslan.bitcointracker.presentation.crypto.CoinListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {


    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            recyclerView.adapter = CoinListAdapter({})

            lifecycleScope.launch {
            viewModel.coins.observe(viewLifecycleOwner) {
                val list=it.map {
                  CoinModel(it.id.toString(),it.name,it.slug,it.symbol)
                }
                (recyclerView.adapter as CoinListAdapter).submitList(list)
            }
            }
        }


    }
}