package com.erenalparslan.bitcointracker.presentation.crypto.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmnetFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :Fragment(R.layout.fragmnet_favorites){

    private val binding by viewBinding(FragmnetFavoritesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}