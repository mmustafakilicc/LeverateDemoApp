package com.mklc.leveratedemoapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mklc.leveratedemoapp.R
import com.mklc.leveratedemoapp.databinding.ItemCarouselTickerListBinding

class MainCarouselAdapter: ListAdapter<TickerView, MainCarouselAdapter.TickerVH>(TickerDiffUtilBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerVH {
        val view = DataBindingUtil.inflate<ItemCarouselTickerListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_carousel_ticker_list,
            parent,
            false
        )
        return TickerVH(view)
    }

    override fun onBindViewHolder(holder: TickerVH, position: Int) {
        holder.binding.ticker = getItem(position)
    }

    class TickerVH(val binding: ItemCarouselTickerListBinding) : RecyclerView.ViewHolder(binding.root)

    private class TickerDiffUtilBack : DiffUtil.ItemCallback<TickerView>() {
        override fun areItemsTheSame(oldItem: TickerView, newItem: TickerView): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: TickerView, newItem: TickerView): Boolean =
            oldItem.price == newItem.price
    }
}