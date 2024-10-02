package com.example.onthilaixe.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView.Adapter

/**
 * create at 06/05/2024 by phamhoang99
 * */

@Retention(AnnotationRetention.SOURCE)
annotation class RecyclerType
abstract class BaseRecyclerAdapter<T : Any, VH : BaseViewHolder<T>>(

) : Adapter<VH>() {
    var listItem: List<T> = arrayListOf()

    @LayoutRes
    abstract fun getItemLayoutResource(@RecyclerType viewType: Int): Int

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(listItem[position])
    }

   open fun submitList(list: List<T>?) {
        listItem = list ?: arrayListOf()
       notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun getViewHolderDataBinding(parent: ViewGroup, viewType: Int): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getItemLayoutResource(viewType),
            parent,
            false
        )
}
