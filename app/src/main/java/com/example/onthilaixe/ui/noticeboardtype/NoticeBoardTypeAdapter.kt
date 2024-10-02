package com.example.onthilaixe.ui.noticeboardtype

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.onthilaixe.R
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemNoticeBoardTypeBinding
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.viewmodel.NoticeBoardViewModel

class NoticeBoardTypeAdapter(
    private val noticeBoardViewModel: NoticeBoardViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val event: (List<NoticeBoards>, String) -> Unit
) : BaseRecyclerAdapter<ItemNoticeBoardType, NoticeBoardTypeAdapter.ProhibitionHolder>() {
    inner class ProhibitionHolder(private val binding: ViewDataBinding) :
        BaseViewHolder<ItemNoticeBoardType>(binding) {
        override fun bind(itemData: ItemNoticeBoardType?) {
            super.bind(itemData)
            if (binding is ItemNoticeBoardTypeBinding) {
                binding.tvType.text = itemData?.title
                itemData?.img?.let { binding.imgType.setImageResource(it) }
            }
            onItemClickListener {
                itemData?.type?.let {
                    noticeBoardViewModel.getALlNoticeBoard(it)
                        .observe(lifecycleOwner) { listNoticeBoard ->
                            event.invoke(listNoticeBoard, itemData.title)
                        }
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_notice_board_type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProhibitionHolder {
        return ProhibitionHolder(getViewHolderDataBinding(parent, viewType))
    }
}