package com.example.onthilaixe.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * create at 06/05/2024 by phamhoang99
 * */
open class BaseViewHolder<T : Any>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    private var _itemData: T? = null
    val itemData get() = _itemData

    open fun bind(itemData: T?) {
        this._itemData = itemData
    }

    open fun onItemClickListener( callBack : ()-> Unit){
        itemView.setOnClickListener {
            callBack.invoke()
        }
    }
}
