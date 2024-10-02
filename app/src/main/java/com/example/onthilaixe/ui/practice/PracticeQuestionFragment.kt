package com.example.onthilaixe.ui.practice

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentPracticeQuestionBinding
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.practicedetail.PracticeQuestionDetailFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.viewmodel.PracticeViewModel
import com.example.onthilaixe.viewmodel.QuestionViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PracticeQuestionFragment : BaseFragmentWithBinding<FragmentPracticeQuestionBinding>() {

    private lateinit var practiceQuestionAdapter: PracticeQuestionAdapter
    private val questionViewModel: QuestionViewModel by viewModels()
    private val practiceViewModel: PracticeViewModel by viewModels()
    private lateinit var chooseLicense: String

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentPracticeQuestionBinding {
        return FragmentPracticeQuestionBinding.inflate(layoutInflater)
    }

    override fun init() {
        chooseLicense = preferences.getString(Constants.KEY_LICENSE_TYPE).toString()
        setupAdapter(chooseLicense)
    }


    override fun initData() {
        practiceViewModel.listItemHome.observe(viewLifecycleOwner) {
            practiceQuestionAdapter.submitList(it)
        }
        practiceViewModel.fetchListItemHome()
    }

    override fun initAction() {
        setToolbar(binding.toolbarMain)
        setupActionMenu(chooseLicense)
    }

    private fun setupAdapter(chooseLicense: String?) {
        binding.apply {
            practiceQuestionAdapter = PracticeQuestionAdapter(
                questionViewModel,
                chooseLicense!!,
                viewLifecycleOwner
            ) { listQuestion, title ->
                val gson = Gson()
                val jsonQuestionList = gson.toJson(listQuestion)
                val bundle = Bundle().apply {
                    putString(Constants.KEY_LIST_QUESTION, jsonQuestionList)
                    putString(Constants.KEY_TITLE_TOOLBAR, title)
                }
                openFragment(PracticeQuestionDetailFragment::class.java, bundle, true)
            }
            rcvPractice.adapter = practiceQuestionAdapter
        }
    }

    private fun setupActionMenu(chooseLicense: String?) {
        binding.toolbarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> {
                    val dialogConfirmDelete = CustomDialog(
                        requireContext(),
                        "Xóa toàn bộ câu trả lời",
                        "Toàn bộ kết quả trả lời các câu hỏi ôn tập sẽ bị xóa",
                        "Bỏ qua",
                        "Chắc chắn"
                    ) {
                        if (chooseLicense != null) {
                            questionViewModel.getALlQuestionWithLicense(chooseLicense)
                                .observe(viewLifecycleOwner) { listQuest ->
                                    val listId = listQuest.map { question -> question.pk }
                                    questionViewModel.deleteAllQuestionLearned(listId)
                                }
                        }
                    }
                    dialogConfirmDelete.show()
                }
            }
            true
        }
    }
}