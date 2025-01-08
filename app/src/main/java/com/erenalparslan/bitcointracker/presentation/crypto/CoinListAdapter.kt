package com.erenalparslan.bitcointracker.presentation.crypto

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.Extensions.setImage
import com.erenalparslan.bitcointracker.databinding.ItemCoinBinding
import com.erenalparslan.bitcointracker.domain.model.CoinModel
import java.lang.Double
import kotlin.Boolean
import kotlin.Int
import kotlin.Unit
import kotlin.let
import kotlin.toString

class CoinListAdapter(private val ctx: Context, private val onItemClick: (CoinModel) -> Unit) :
    ListAdapter<CoinModel, CoinListAdapter.CoinViewHolder>(CoinDiff) {
    inner class CoinViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: CoinModel) {
            val quote = coin.quote
            binding.coinName.text = coin.name
            binding.coinSymbol.text = "${coin.symbol}/USDT"
            binding.coinPriceChange.text = quote
            quote?.let {
                if (Double.parseDouble(it) >= 0) {
                    binding.coinPriceChange.setTextColor(ContextCompat.getColor(ctx, R.color.green))
                } else {
                    binding.coinPriceChange.setTextColor(ContextCompat.getColor(ctx, R.color.red))
                }
            }
            binding.coinValue.text = coin.price
            setImage(binding.imageView, coin.id.toString())
            binding.root.setOnClickListener {
                coin.symbol?.let { it1 -> Log.e("Erens", "bind: $it1 ") }
                onItemClick(coin)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(
            ItemCoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

object CoinDiff : DiffUtil.ItemCallback<CoinModel>() {

    override fun areItemsTheSame(oldItem: CoinModel, newItem: CoinModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CoinModel, newItem: CoinModel): Boolean {
        return oldItem == newItem
    }
}
