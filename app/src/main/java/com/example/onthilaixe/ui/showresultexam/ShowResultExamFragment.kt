package com.example.onthilaixe.ui.showresultexam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.formatTitleExamResult
import com.example.onthilaixe.base.extension.formatTitleShowResult
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentShowTestResultBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.dotest.DoTestFragment
import com.example.onthilaixe.ui.randomtest.RandomExamFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.utils.saveBundleToPreferences
import com.example.onthilaixe.viewmodel.RandomExamViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShowResultExamFragment : BaseFragmentWithBinding<FragmentShowTestResultBinding>(),
    ShowResultExamAdapter.ItemClickCallback {

    lateinit var mAdapter: ShowResultExamAdapter
    private val viewModelRandomTest: RandomExamViewModel by viewModels()
    private var list: List<Question> = listOf()
    private val listTrueQuestion = arrayListOf<Question>()
    private lateinit var listQuestions: ArrayList<Question>
    private lateinit var examName: String
    private lateinit var testId: String
    private lateinit var jsonListQuestion: String
    private lateinit var getTypeListQuestFrom: String
    private var numberOfCorrectQuestion: Int? = 0

    @Inject
    lateinit var preferences: Preferences

    override fun getViewBinding(inflater: LayoutInflater): FragmentShowTestResultBinding {
        return FragmentShowTestResultBinding.inflate(layoutInflater)
    }

    override fun init() {
        val timePass = preferences.getString(Constants.KEY_TIME_RESULT_EXAM)
        binding.txtTimedotest.text = timePass
        setupAdapter()
    }

    override fun initData() {
        val gson = Gson()
        examName = arguments?.getString(Constants.KEY_EXAM_NAME).toString()
        testId = arguments?.getString(Constants.KEY_TEST_ID).toString()
        val license = preferences.getString(Constants.KEY_LICENSE_TYPE)
        numberOfCorrectQuestion = preferences.getInt(Constants.KEY_NUMBER_OF_CORRECT_QUEST)
        binding.toolbarMain.title = examName.formatTitleExamResult(license.toString())
        binding.txtCurrentTest.text = examName.formatTitleShowResult()

        jsonListQuestion = arguments?.getString(Constants.KEY_JSON_LIST_QUESTION_SEND)
            .toString()
        listQuestions = gson.fromJson(
            jsonListQuestion,
            object : TypeToken<ArrayList<Question>>() {}.type
        )

        getTypeListQuestFrom = arguments?.getString(Constants.KEY_TYPE_LIST_QUEST_FROM).toString()
        if (getTypeListQuestFrom == Constants.KEY_FROM_RANDOM_TEST) {
            handleRandomTest(listQuestions)
        } else {
            handleNonRandomTest(listQuestions)
        }
        saveBundleToPreferences(createBundleToCheckOnBackPressed(), preferences)
    }

    override fun initAction() {
        binding.toolbarMain.setNavigationOnClickListener {
            when (getTypeListQuestFrom) {
                Constants.KEY_FROM_TEST -> {
                    activity?.supportFragmentManager?.popBackStack()
                }

                Constants.KEY_FROM_RANDOM_TEST -> {
                    activity?.supportFragmentManager?.popBackStack(
                        RandomExamFragment::class.java.simpleName,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }

                Constants.KEY_FROM_DO_TEST -> {
                    activity?.supportFragmentManager?.popBackStack(
                        DoTestFragment::class.java.simpleName,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }

                else -> {
                    activity?.supportFragmentManager?.popBackStack(
                        ShowResultExamFragment::class.java.simpleName,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
            }
        }
        setupActionMenu()
    }

    private fun setupAdapter() {
        mAdapter = ShowResultExamAdapter(this)
        binding.rcvTestResult.adapter = mAdapter
    }

    private fun createBundleToCheckOnBackPressed(): Bundle {
        val bundle = Bundle().apply {
            putString(
                Constants.KEY_TYPE_LIST_QUEST_FROM,
                getTypeListQuestFrom
            ).toString()
        }
        return bundle
    }

    private fun handleRandomTest(listQuestion: ArrayList<Question>) {
        list = listQuestion
        val listTest = arrayListOf<Question>()
        for (i in listQuestion.indices) {
            list[i].nameTarget = i + 1
            if (listQuestion[i].marked != 0) {
                listTest.add(listQuestion[i])
            }
        }
        val listQuestionTrue =
            list.filter { question -> question.marked == question.answers }
        numberOfCorrectQuestion?.let { checkIfUserPass(it, listQuestionTrue) }
        updateQuestionWrong()
        updateUI(listTest, listQuestion, list)
    }

    private fun handleNonRandomTest(listQuestions: ArrayList<Question>) {
        val listQuestionId = listQuestions.map { it.pk }
        viewModelRandomTest.listQuestion.observe(viewLifecycleOwner) { listQuestion ->
            list = listQuestion
            val listTest = arrayListOf<Question>()
            for (i in listQuestion.indices) {
                list[i].nameTarget = i + 1
                if (listQuestion[i].marked != 0) {
                    listTest.add(listQuestion[i])
                }
            }
            val listQuestionTrue =
                list.filter { question -> question.marked == question.answers && question.marked != 0 }
            numberOfCorrectQuestion?.let { checkIfUserPass(it, listQuestionTrue) }
            updateQuestionWrong()
            updateUI(listTest, listQuestions, list)
        }
        viewModelRandomTest.getQuestionsByQuestionIdTest(listQuestionId)
    }

    private fun updateUI(
        listTest: ArrayList<Question>,
        listQuestion: ArrayList<Question>,
        list: List<Question>
    ) {
        binding.txtDoneQuestion.text = String.format("%d/%d", listTest.size, listQuestion.size)
        mAdapter.submitList(list)
        setupTabAndAction()
    }

    private fun updateQuestionWrong() {
        val listQuestionWrong = list.filter { question ->
            question.marked != question.answers && question.marked != 0
        }.map {
            it.pk
        }
        viewModelRandomTest.updateWrongInQuestion(listQuestionWrong)
    }

    private fun navigateDoTest() {
        val listQuestionId = listQuestions.map { question -> question.pk }
        viewModelRandomTest.resetMarkedQuestById(listQuestionId)
        viewModelRandomTest.resetTestDefaultById(testId.toInt())
        activity?.supportFragmentManager?.popBackStack(
            ShowResultExamFragment::class.java.simpleName,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        openFragment(DoTestFragment::class.java, sendBundle(), true)
    }

    private fun navigateRandomTest() {
        activity?.supportFragmentManager?.popBackStack(
            ShowResultExamFragment::class.java.simpleName,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        openFragment(RandomExamFragment::class.java, null, true)
    }

    private fun setupTabAndAction() {
        Constants.TYPE_RCV = 1
        binding.apply {
            val imagesList: List<Int> = listOf(
                R.drawable.ic_total_test_result,
                R.drawable.ic_total_test_result,
                R.drawable.ic_true_result,
                R.drawable.ic_false_result,
                R.drawable.ic_not_done_result
            )

            val listWrongQuestion = arrayListOf<Question>()
            val listNotDoneQuestion = arrayListOf<Question>()

            for (i in list) {
                if (i.marked == i.answers) {
                    listTrueQuestion.add(i)
                }
                if (i.marked != 0 && i.marked != i.answers) {
                    listWrongQuestion.add(i)
                }
                if (i.marked == 0) {
                    listNotDoneQuestion.add(i)
                }
            }

            val txtList: List<String> = listOf(
                " ${list.size}",
                " ${list.size}",
                " ${listTrueQuestion.size}",
                " ${listWrongQuestion.size}",
                " ${listNotDoneQuestion.size}",
            )

            tablayoutTestResult.tabMode = TabLayout.MODE_FIXED
            tablayoutTestResult.tabGravity = TabLayout.GRAVITY_FILL
            for (i in 1..4) {
                val tab = tablayoutTestResult.newTab()
                val tabItemView = LayoutInflater.from(tablayoutTestResult.context)
                    .inflate(
                        R.layout.item_custom_for_tablayout_result_test,
                        tablayoutTestResult,
                        false
                    )
                val tabTxt = tabItemView.findViewById<TextView>(R.id.txtTablayoutTestResult)
                val tabImg = tabItemView.findViewById<ImageView>(R.id.img_tablayout)
                tabImg.setImageResource(imagesList[i])
                tabTxt.text = txtList[i]
                tab.customView = tabItemView
                tablayoutTestResult.addTab(tab)
            }

            tablayoutTestResult.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        when (it.position) {
                            0 -> {
                                Constants.TYPE_RCV = 1
                                mAdapter.submitList(list)
                            }

                            1 -> {
                                Constants.TYPE_RCV = 2
                                val filteredList =
                                    list.filter { question -> question.marked == question.answers }
                                mAdapter.submitList(filteredList)
                            }

                            2 -> {
                                Constants.TYPE_RCV = 3
                                val filteredList =
                                    list.filter { question -> question.marked != question.answers && question.marked != 0 }
                                mAdapter.submitList(filteredList)
                            }

                            3 -> {
                                Constants.TYPE_RCV = 4
                                val filteredList = list.filter { question -> question.marked == 0 }
                                mAdapter.submitList(filteredList)
                            }
                        }
                        mAdapter.notifyDataSetChanged()
                        val selectedTabView = it.customView
                        val selectedTabBackground =
                            selectedTabView?.findViewById<View>(R.id.bg_circle)
                        selectedTabBackground?.setBackgroundResource(R.drawable.bg_circle_selected)

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val unselectedTabView = it.customView
                        val unselectedTabBackground =
                            unselectedTabView?.findViewById<View>(R.id.bg_circle)
                        unselectedTabBackground?.setBackgroundResource(R.drawable.bg_circle)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

        }

    }

    private fun checkIfUserPass(numberOfCorrectQuestion: Int, listQuestionTrue: List<Question>) {
        if (listQuestionTrue.size < numberOfCorrectQuestion) {
            binding.tvResult.text = resources.getText(R.string.text_result_false)
            binding.tvResult.setBackgroundResource(R.drawable.bgr_text_result_false)
            viewModelRandomTest.updateTestIsFailed(testId = testId.toInt())
        } else {
            binding.tvResult.text = resources.getText(R.string.text_result_true)
            binding.tvResult.setBackgroundResource(R.drawable.bgr_text_result_true)
            viewModelRandomTest.updateTestIsFinish(testId = testId.toInt())
        }
    }

    override fun onItemClick(test: Question) {
        preferences.setString(
            Constants.KEY_CHECK_TYPE_DO_TEST,
            Constants.KEY_VALUE_TYPE_DO_TEST_ONE
        )
        val bundle = Bundle().apply {
            putString(Constants.KEY_CHECK_TYPE_DO_TEST, Constants.KEY_VALUE_TYPE_DO_TEST_ONE)
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonListQuestion)
            putString(Constants.KEY_EXAM_NAME, examName)
            putString(Constants.KEY_TEST_ID, testId)
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, getTypeListQuestFrom)

            test.nameTarget?.let {
                putInt(Constants.KEY_POSITION_QUESTION, it)
            }
        }
        openFragment(DoTestFragment::class.java, bundle, true)
    }

    private fun sendBundle(): Bundle {
        val bundle = Bundle().apply {
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonListQuestion)
            putString(Constants.KEY_EXAM_NAME, examName)
            putString(Constants.KEY_TEST_ID, testId)
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, Constants.KEY_FROM_DO_TEST)
            putString(Constants.KEY_CHECK_TYPE_DO_TEST, Constants.KEY_VALUE_TYPE_DO_TEST_ZERO)
        }
        return bundle
    }

    private fun setupActionMenu() {
        binding.toolbarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> {
                    val dialogConfirmDelete = CustomDialog(
                        requireContext(),
                        "Bạn muốn làm lại bài thi?",
                        "Những câu trả lời trước đó của bạn sẽ được xóa!",
                        "Bỏ qua",
                        "Chắc chắn"
                    ) {
                        if (getTypeListQuestFrom == Constants.KEY_FROM_RANDOM_TEST) {
                            navigateRandomTest()
                        } else {
                            navigateDoTest()
                        }
                    }
                    dialogConfirmDelete.show()
                }
            }
            true
        }
    }
}


