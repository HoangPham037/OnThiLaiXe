package com.example.onthilaixe.ui.test

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.onthilaixe.R
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.base.extension.gone
import com.example.onthilaixe.base.extension.visible
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemForTestBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.room.entity.Test
import com.example.onthilaixe.viewmodel.RandomExamViewModel

class AdapterTest(
    val viewModelQuestion: RandomExamViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val event : (List<Question>, Test, TypeClicks) -> Unit
) : BaseRecyclerAdapter<Test, AdapterTest.TestViewHolder>() {

    inner class TestViewHolder(val binding: ViewDataBinding) : BaseViewHolder<Test>(binding) {
        override fun bind(itemData: Test?) {
            super.bind(itemData)
            if (binding is ItemForTestBinding) {
                itemData?.idTest?.let { idTest ->
                    viewModelQuestion.getQuestionIdFromTestIds(idTest).observe(lifecycleOwner) { listQuestionId ->
                        viewModelQuestion.getQuestionsByQuestionIds(listQuestionId)?.observe(lifecycleOwner) { listQuestion ->
                            val listWrong = listQuestion.filter { question ->
                                question.marked != 0 && question.answers != question.marked
                            }
                            val listTrue = listQuestion.filter { question ->
                                question.marked == question.answers
                            }
                            if (itemData.isFinish == 1) {
                                binding.apply {
                                    gotoTest.setBackgroundResource(R.color.green_04)
                                    layoutTotalAnswer.visible()
                                    testTrueQuestion.text = listTrue.size.toString()
                                    testWrongQuestion.text = listWrong.size.toString()
                                }
                            }
                            else if (itemData.isFailed == 1 ) {
                                binding.apply {
                                    gotoTest.setBackgroundResource(R.color.pink)
                                    layoutTotalAnswer.visible()
                                    testTrueQuestion.text = listTrue.size.toString()
                                    testWrongQuestion.text = listWrong.size.toString()
                                }
                            }else {
                                binding.apply {
                                    gotoTest.setBackgroundResource(R.color.gray_02)
                                    layoutTotalAnswer.gone()
                                }
                            }
                            if (itemData.isFailed == 1 || itemData.isFinish == 1) {
                                itemView.click {
                                    event.invoke( listQuestion, itemData, TypeClicks.DO_TEST_AGAIN)
                                }
                            } else {
                                itemView.click {
                                    event.invoke(listQuestion, itemData, TypeClicks.DO_TEST_FIRST)
                                }
                            }
                        }
                    }
                }

                binding.numberofTest.text = itemData?.nameTest.toString()
            }
        }
    }


    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_for_test
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(getViewHolderDataBinding(parent, viewType))
    }
}

enum class TypeClicks {
    DO_TEST_FIRST,
    DO_TEST_AGAIN
}
