package com.example.onthilaixe.ui.showresultexam

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.onthilaixe.R
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemForViewpagerTestResultBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.utils.Constants

class ShowResultExamAdapter(private val itemClickCallback: ItemClickCallback) : BaseRecyclerAdapter<Question,BaseViewHolder<Question>>() {

    interface ItemClickCallback {
        fun onItemClick(test: Question)
    }
    companion object {
        private const val TYPE_TOTAL_QUESTION = 0
        private const val TYPE_CORRECT_QUESTION = 1
        private const val TYPE_WRONG_QUESTION = 2
        private const val TYPE_NOT_DONE_QUESTION = 3
    }
    override fun getItemLayoutResource(viewType: Int): Int {
        return when(viewType){
            TYPE_TOTAL_QUESTION -> R.layout.item_for_viewpager_test_result
            TYPE_CORRECT_QUESTION -> R.layout.item_for_viewpager_test_result
            TYPE_WRONG_QUESTION -> R.layout.item_for_viewpager_test_result
            TYPE_NOT_DONE_QUESTION -> R.layout.item_for_viewpager_test_result
            else -> R.layout.item_for_viewpager_test_result
        }
    }

    inner class TotalInfoQuestionViewHolder(val binding: ViewDataBinding) : BaseViewHolder<Question>(binding){
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            if(binding is ItemForViewpagerTestResultBinding) {
                binding.apply {
                    val itemIndex = listItem.indexOf(itemData) + 1
                        txtIdAnswear.text = " Câu  ${itemData?.nameTarget.toString()}" ?: "Câu + $itemIndex"
                    if(itemData?.marked ==0){
                        imgTrueFalse.setImageResource(R.drawable.ic_not_done_result)
                    } else if(itemData?.marked != itemData?.answers){
                        imgTrueFalse.setImageResource(R.drawable.ic_false_result)
                    } else{
                        imgTrueFalse.setImageResource(R.drawable.ic_true_result)
                    }
                    binding.gotoQuestion.click {
                        itemData?.let {
                            itemClickCallback.onItemClick(it)
                        }
                    }
                }
            }

        }
    }
    inner class QuestionCorrectViewHolder(val binding: ViewDataBinding) : BaseViewHolder<Question>(binding){
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            if(binding is ItemForViewpagerTestResultBinding){
                val itemIndex = listItem.indexOf(itemData) + 1
                if (itemData?.marked == itemData?.answers) {
                    binding.txtIdAnswear.text = " Câu ${itemData?.nameTarget.toString()}" ?: "Câu + $itemIndex"
                    binding.imgTrueFalse.setImageResource(R.drawable.ic_true_result)
                }
                binding.gotoQuestion.click {
                    itemData?.let {
                        itemClickCallback.onItemClick(it)
                    }
                }
            }
        }
    }
    inner class QuestionWrongViewHolder(val binding: ViewDataBinding) : BaseViewHolder<Question>(binding){
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            if(binding is ItemForViewpagerTestResultBinding){
                val itemIndex = listItem.indexOf(itemData) + 1
                binding.apply {
                    txtIdAnswear.text = " Câu ${itemData?.nameTarget.toString()}" ?: "Câu + $itemIndex"
                    if(itemData?.marked != itemData?.answers && itemData?.marked != 0){
                        imgTrueFalse.setImageResource(R.drawable.ic_false_result)
                    }
                }
                binding.gotoQuestion.click {
                    itemData?.let {
                        itemClickCallback.onItemClick(it)
                    }
                }
            }
        }
    }
    inner class QuestionNotDoneViewHolder(val binding: ViewDataBinding) : BaseViewHolder<Question>(binding){
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            if(binding is ItemForViewpagerTestResultBinding){
                val itemIndex = listItem.indexOf(itemData) + 1
                binding.apply {
                    binding.txtIdAnswear.text = " Câu ${itemData?.nameTarget.toString()}" ?: "Câu + $itemIndex"
                    if(itemData?.marked == 0){
                        imgTrueFalse.setImageResource(R.drawable.ic_not_done_result)
                    }
                }
                binding.gotoQuestion.click {
                    itemData?.let {
                        itemClickCallback.onItemClick(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Question> {
       return when(viewType) {
           TYPE_TOTAL_QUESTION -> TotalInfoQuestionViewHolder(getViewHolderDataBinding(parent, viewType))
           TYPE_CORRECT_QUESTION -> QuestionCorrectViewHolder(getViewHolderDataBinding(parent, viewType))
           TYPE_WRONG_QUESTION -> QuestionWrongViewHolder(getViewHolderDataBinding(parent, viewType))
           TYPE_NOT_DONE_QUESTION -> QuestionNotDoneViewHolder(getViewHolderDataBinding(parent, viewType))
           else -> {
               throw Exception("Error reading holder type")
           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        return  when (Constants.TYPE_RCV){
            1 -> TYPE_TOTAL_QUESTION
            2 -> TYPE_CORRECT_QUESTION
            3 -> TYPE_WRONG_QUESTION
            4 -> TYPE_NOT_DONE_QUESTION
            else -> TYPE_TOTAL_QUESTION
        }
    }
}