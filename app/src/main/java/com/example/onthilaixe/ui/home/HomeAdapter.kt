package com.example.onthilaixe.ui.home

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.onthilaixe.R
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemHomeBinding

class HomeAdapter(val event: (Int) -> Unit) :
    BaseRecyclerAdapter<ItemHome, HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(private val binding: ViewDataBinding) :
        BaseViewHolder<ItemHome>(binding) {
        override fun bind(itemData: ItemHome?) {
            super.bind(itemData)
            if (binding is ItemHomeBinding) {
                itemData?.img?.let { binding.imgTitle.setImageResource(it) }
                binding.tvTitle.text = itemData?.title
                binding.tvDesc.text = itemData?.desc
                onItemClickListener {
                    event.invoke(position)
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_home
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(getViewHolderDataBinding(parent, viewType))
    }
}