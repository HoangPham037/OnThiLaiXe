package com.example.onthilaixe.ui.practicedetail

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.onthilaixe.R
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.base.extension.gone
import com.example.onthilaixe.base.extension.goneIf
import com.example.onthilaixe.base.extension.visible
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemForRandomTestBinding
import com.example.onthilaixe.models.local.room.entity.Question
import java.io.IOException

class PracticeQuestionDetailAdapter(private val event: (Question?, Int) -> Unit) :
    BaseRecyclerAdapter<Question, PracticeQuestionDetailAdapter.ViewHolderRandomTest>() {

    inner class ViewHolderRandomTest(private val binding: ItemForRandomTestBinding) :
        BaseViewHolder<Question>(binding) {
        override fun bind(itemData: Question?) {
            super.bind(itemData)
            binding.apply {
                rdtestQuestion.text = itemData?.questionContent
                txtAnswear1.text = itemData?.option1
                txtAnswear2.text = itemData?.option2
                txtAnswear3.text = itemData?.option3
                txtAnswear4.text = itemData?.option4

                val answerViews = listOf(answear1, answear2, answear3, answear4)
                val listTvAnswer = listOf(txtAnswear1, txtAnswear2, txtAnswear3, txtAnswear4)
                val answerCircles =
                    listOf(answearCicler1, answearCicler2, answearCicler3, answearCicler4)

                val posSelected = itemData?.marked

                answerViews.forEach {
                    it.isEnabled = true
                }
                answerCircles.forEach { circle -> circle.setBackgroundResource(R.drawable.bg_circle) }
                listTvAnswer.forEach { answer ->
                    answer.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green_01
                        )
                    )
                }
                answerCircles.forEachIndexed { index, textView ->
                    textView.text = index.plus(1).toString()
                }
                tvCheckQuestion.gone()
                layoutShowAnswer.gone()

                if (posSelected != 0) {
                    answerCircles[(itemData?.marked
                        ?: 1) - 1].setBackgroundResource(R.drawable.bg_circle_selected)
                    if (itemData?.learned == 1) {
                        layoutShowAnswer.visible()
                        tvDescAnswer.text = itemData.answerDesc
                        tvCheckQuestion.gone()
                        answerViews.forEach {
                            it.isEnabled = false
                        }

                        if (posSelected == itemData.answers) {
                            listTvAnswer[posSelected!! - 1].setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.green_04
                                )
                            )
                            answerCircles[posSelected - 1].apply {
                                setBackgroundResource(R.drawable.ic_aw_true)
                                text = ""
                            }
                        } else {
                            listTvAnswer[itemData.answers!! - 1].setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.green_04
                                )
                            )
                            answerCircles[itemData.answers - 1].apply {
                                setBackgroundResource(R.drawable.ic_aw_true)
                                text = ""
                            }
                            listTvAnswer[posSelected!! - 1].setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.red
                                )
                            )
                            answerCircles[posSelected - 1].apply {
                                setBackgroundResource(R.drawable.ic_aw_wrong)
                                text = ""
                            }
                        }

                    } else {
                        tvCheckQuestion.apply {
                            visible()
                            click {
                                layoutShowAnswer.visible()
                                itemData?.learned = 1

                                itemData?.marked?.let { it1 -> event.invoke(itemData, it1) }
                                tvDescAnswer.text = itemData?.answerDesc
                                gone()
                            }
                        }
                    }


                }

                answerViews.forEachIndexed { index, view ->
                    val answerText = when (index) {
                        0 -> itemData?.option1
                        1 -> itemData?.option2
                        2 -> itemData?.option3
                        3 -> itemData?.option4
                        else -> null
                    }
                    view.apply {
                        goneIf(answerText == null)
                        click {
                            itemData?.marked = index + 1
                            event.invoke(itemData, index + 1)
                        }
                    }
                    if (itemData?.image?.isEmpty() == true) {
                        imgQuestionPic.gone()
                    } else {
                        imgQuestionPic.visible()
                        try {
                            val stream =
                                itemData?.image?.let { itemView.context.assets.open("imgWebp/$it") }
                            val drawable = Drawable.createFromStream(stream, null)
                            imgQuestionPic.setImageDrawable(drawable)
                        } catch (e: IOException) {
                            return
                        }
                    }
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_for_random_test
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRandomTest {
        return ViewHolderRandomTest(
            getViewHolderDataBinding(
                parent,
                viewType
            ) as ItemForRandomTestBinding
        )
    }
}