package com.example.onthilaixe.ui.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentTestBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.room.entity.Test
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.dotest.DoTestFragment
import com.example.onthilaixe.ui.inapp.PrefHelper
import com.example.onthilaixe.ui.inapp.PurchaseActivity
import com.example.onthilaixe.ui.showresultexam.ShowResultExamFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.viewmodel.ExamViewModel
import com.example.onthilaixe.viewmodel.HomeViewModel
import com.example.onthilaixe.viewmodel.RandomExamViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ExamFragment : BaseFragmentWithBinding<FragmentTestBinding>() {

    private lateinit var adapterTest: AdapterTest
    private val viewModelTest: ExamViewModel by viewModels()
    private val viewModelRandomTest: RandomExamViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var pref: PrefHelper

    @Inject
    lateinit var preferences: Preferences

    override fun getViewBinding(inflater: LayoutInflater): FragmentTestBinding {
        return FragmentTestBinding.inflate(layoutInflater)
    }

    override fun init() {
        pref = PrefHelper.getInstance(requireContext())!!
        setupAdapter()
    }

    override fun initData() {
        val license = preferences.getString(Constants.KEY_LICENSE_TYPE)
        if (license != null) {
            viewModelTest.getAllTestWithClassLicense(license)
                .observe(viewLifecycleOwner) { listTest ->
                    adapterTest.submitList(listTest)
                    adapterTest.notifyDataSetChanged()
                }
        }
        homeViewModel.currentGold.observe(viewLifecycleOwner) {
            binding.tvGold.text = it.toString()
        }
    }

    override fun initAction() {
        binding.imgBack.click {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.imgDelete.click {
            val dialogConfirmDelete = CustomDialog(
                requireContext(),
                "Bạn muốn xoá hết kết quả thi?",
                "Lịch sử thi của bạn sẽ được xoá hết!",
                "Bỏ qua",
                "Chắc chắn"
            ) {
                viewModelTest.resetAll()
                viewModelTest.deleteAllTest()
            }
            dialogConfirmDelete.show()
        }
        binding.tvGold.click {
            startActivity(Intent(requireContext(), PurchaseActivity::class.java))
        }
    }

    private fun setupAdapter() {
        adapterTest = AdapterTest(
            viewModelRandomTest,
            viewLifecycleOwner
        ) { listQuestion, test, typeClick ->
            when (typeClick) {
                TypeClicks.DO_TEST_FIRST -> {
                    showDialogConfirm(listQuestion, test)
                }

                TypeClicks.DO_TEST_AGAIN -> {
                    val gson = Gson()
                    val jsonListQuestion = gson.toJson(listQuestion)
                    val bundle = Bundle().apply {
                        putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonListQuestion)
                        putString(Constants.KEY_EXAM_NAME, test.idTest.toString())
                        putString(Constants.KEY_TEST_ID, test.idTest.toString())
                        putString(Constants.KEY_TYPE_LIST_QUEST_FROM, Constants.KEY_FROM_TEST)
                    }
                    openFragment(ShowResultExamFragment::class.java, bundle, true)
                }
            }
        }
        binding.rcvTest.adapter = adapterTest
    }

    private fun showDialogConfirm(
        listQuestion: List<Question>,
        test: Test
    ) {
        val dialog = CustomDialog(
            requireContext(),
            "Bạn có chắc chắn vào làm bài thi?",
            "Bạn cần 5 vàng để thi",
            "Bỏ qua",
            "Đồng ý"
        ) {
            if (pref.getValueCoin() >= 5) {
                pref.setValueCoin(pref.getValueCoin() - 5)
                navigateDoTest(listQuestion, test)
            } else {
                toast("Hiện tại vàng của bạn không đủ, vui lòng nạp thêm!")
            }
        }
        dialog.show()
    }

    private fun navigateDoTest(
        listQuestion: List<Question>,
        test: Test
    ) {
        val gson = Gson()
        val jsonQuestionList = gson.toJson(listQuestion)
        val bundle = Bundle().apply {
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonQuestionList)
            putString(Constants.KEY_EXAM_NAME, test.nameTest?.toString())
            putString(Constants.KEY_TEST_ID, test.idTest.toString())
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, Constants.KEY_FROM_TEST)
        }
        openFragment(DoTestFragment::class.java, bundle, true)
    }

}