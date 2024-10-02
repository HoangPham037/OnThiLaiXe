package com.example.onthilaixe.ui.noticeboard

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.onthilaixe.R
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemNoticeBoardBinding
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import java.io.IOException

class NoticeBoardAdapter :
    BaseRecyclerAdapter<NoticeBoards, NoticeBoardAdapter.ProhibitionHolder>() {
    inner class ProhibitionHolder(private val binding: ViewDataBinding) :
        BaseViewHolder<NoticeBoards>(binding) {
        override fun bind(itemData: NoticeBoards?) {
            super.bind(itemData)
            if (binding is ItemNoticeBoardBinding) {
                binding.tvTitle.text = itemData?.name
                binding.tvDesc.text = itemData?.detail
                try {
                    val stream =
                        itemData?.icon?.let { itemView.context.assets.open("imgnoiceboard/$it") }
                    val drawable = Drawable.createFromStream(stream, null)
                    binding.imgNoticeBroad.setImageDrawable(drawable)
                } catch (e: IOException) {
                    return
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_notice_board
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProhibitionHolder {
        return ProhibitionHolder(getViewHolderDataBinding(parent, viewType))
    }
}