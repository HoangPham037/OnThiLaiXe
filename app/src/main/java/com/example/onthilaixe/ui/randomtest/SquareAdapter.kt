package com.example.onthilaixe.ui.randomtest

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.example.onthilaixe.R
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemForRcvSquareBinding
import com.example.onthilaixe.models.local.room.entity.Question

class SquareAdapter() : BaseRecyclerAdapter<Question, SquareAdapter.ViewHolderSquare>() {

    inner class ViewHolderSquare(val binding: ViewDataBinding) : BaseViewHolder<Question>(binding) {
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            if (binding is ItemForRcvSquareBinding) {
                val colorResId = if (itemData?.marked != 0) {
                    R.color.green_02
                } else {
                    R.color.gray_02
                }
                binding.squareView.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        colorResId
                    )
                )
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_for_rcv_square
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSquare {
        return ViewHolderSquare(getViewHolderDataBinding(parent, viewType))
    }
}