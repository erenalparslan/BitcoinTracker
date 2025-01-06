package com.erenalparslan.bitcointracker.presentation.crypto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erenalparslan.bitcointracker.databinding.ItemCoinBinding
import com.erenalparslan.bitcointracker.domain.model.CoinModel

class CoinListAdapter(private val onItemClick: (CoinModel) -> Unit) :
    ListAdapter<CoinModel, CoinListAdapter.CoinViewHolder>(CoinDiff) {
    class CoinViewHolder(private val binding: ItemCoinBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: CoinModel) {
            binding.coinName.text = coin.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
    return CoinViewHolder(ItemCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
