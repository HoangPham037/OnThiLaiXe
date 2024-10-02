package com.example.onthilaixe.ui.practice

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.onthilaixe.R
import com.example.onthilaixe.base.extension.formatTotalQuestionPractice
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemForPracticeQuestionBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.viewmodel.QuestionViewModel


class PracticeQuestionAdapter(
    private val questionViewModel: QuestionViewModel,
    private val chooseLicense: String,
    private val lifecycleOwner: LifecycleOwner,
    private val event: (List<Question>, String) -> Unit
) : BaseRecyclerAdapter<ItemLicenseType, PracticeQuestionAdapter.PracticeQuestionViewHolder>() {

    inner class PracticeQuestionViewHolder(val binding: ViewDataBinding) :
        BaseViewHolder<ItemLicenseType>(binding) {
        override fun bind(itemData: ItemLicenseType?) {
            super.bind(itemData)
            if (binding is ItemForPracticeQuestionBinding) {
                binding.apply {
                    tvTitle.text = itemData?.title
                    itemData?.image?.let { imgDesc.setImageResource(it) }
                }
                itemData?.type?.let {
                    questionViewModel.getALlQuestionByType(
                        type = it,
                        chooseLicense
                    )
                }?.observe(lifecycleOwner) { listQuestion ->
                    val listLearned = listQuestion.filter { question -> question.learned == 1 }
                    binding.apply {
                        tvDesc.text = listQuestion.size.formatTotalQuestionPractice()
                        tvTotal.text = listQuestion.size.toString()
                        tvDone.text = listLearned.size.toString()
                        progressBar.max = listQuestion.size
                        progressBar.progress = listLearned.size
                    }
                    onItemClickListener {
                        event.invoke(listQuestion, itemData.title)
                    }
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_for_practice_question
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PracticeQuestionViewHolder {
        return PracticeQuestionViewHolder(getViewHolderDataBinding(parent, viewType))
    }
}