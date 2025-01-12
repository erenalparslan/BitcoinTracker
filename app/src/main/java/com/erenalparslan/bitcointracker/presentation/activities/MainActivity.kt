package com.erenalparslan.bitcointracker.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}