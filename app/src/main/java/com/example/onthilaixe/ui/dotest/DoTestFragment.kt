package com.example.onthilaixe.ui.dotest

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.base.extension.gone
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentDoTestBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.randomtest.RandomExamAdapter
import com.example.onthilaixe.ui.randomtest.SquareAdapter
import com.example.onthilaixe.ui.showresultexam.ShowResultExamFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.utils.saveBundleToPreferences
import com.example.onthilaixe.viewmodel.RandomExamViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoTestFragment : BaseFragmentWithBinding<FragmentDoTestBinding>() {

    private lateinit var adapterRandomTest: RandomExamAdapter
    private lateinit var adapterSquare: SquareAdapter
    private val viewModelRandomTest: RandomExamViewModel by viewModels()
    private lateinit var listQuestion: ArrayList<Question>
    private lateinit var examName: String
    private lateinit var nameLicense: String
    private lateinit var testId: String
    private lateinit var recheckQuestion: String
    private var isTimerRunning = false
    private lateinit var jsonQuestionList: String
    private lateinit var listDefault: ArrayList<Question>

    private lateinit var countdownTimer: CountDownTimer

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentDoTestBinding {
        return FragmentDoTestBinding.inflate(layoutInflater)
    }


    override fun init() {
        setupAdapter()
    }

    override fun initData() {
        recheckQuestion = arguments?.getString(Constants.KEY_CHECK_TYPE_DO_TEST).toString()
        testId = arguments?.getString(Constants.KEY_TEST_ID).toString()
        examName = arguments?.getString(Constants.KEY_EXAM_NAME).toString()
        nameLicense = preferences.getString(Constants.KEY_LICENSE_TYPE).toString()
        binding.tvTitle.text = String.format("%s", "Đề thi số $examName - $nameLicense")
        val gson = Gson()
        jsonQuestionList = arguments?.getString(Constants.KEY_JSON_LIST_QUESTION_SEND).toString()

        val numberOfQuestion = preferences.getInt(Constants.KEY_NUMBER_OF_QUEST)
        numberOfQuestion?.let { totalQuestion ->
            setupTabAndAction(totalQuestion)
        }
        setTypeOfDoTest()
        val durations = preferences.getLong(Constants.KEY_DURATION)
        durations?.let { duration -> countDownTimeExam(duration) }

        val typeListQuestionFrom = arguments?.getString(Constants.KEY_TYPE_LIST_QUEST_FROM)
        listQuestion = gson.fromJson(
            jsonQuestionList,
            object : com.google.gson.reflect.TypeToken<ArrayList<Question>>() {}.type
        )
        if (typeListQuestionFrom == Constants.KEY_FROM_DO_TEST) {
            val listQuestionId = listQuestion.map { question -> question.pk }
            viewModelRandomTest.listQuestion.observe(viewLifecycleOwner) { listQuestion ->
                listDefault = listQuestion.map { question ->
                    question.copy(learned = 0, marked = 0)
                } as ArrayList<Question>
                adapterRandomTest.submitList(listQuestion)
                adapterSquare.submitList(listQuestion)
                val listMarked = listDefault.filter { question -> question.marked != 0 }
                binding.questionDone2.text =
                    String.format("%d/%d", listMarked.size, listDefault.size)
            }
            viewModelRandomTest.getQuestionsByQuestionIdTest(listQuestionId)
        } else {
            listDefault = listQuestion
            adapterRandomTest.submitList(listDefault)
            adapterSquare.submitList(listDefault)
        }

    }

    override fun initAction() {
        binding.imgBack.click {
            handleBtnBack()
        }

        binding.randomtestSubmit.click {
            showDialogConfirm()
            countdownTimer.cancel()
        }
    }

    private fun handleBtnBack() {
        if (recheckQuestion != Constants.KEY_VALUE_TYPE_DO_TEST_ONE) {
            showDialogConfirm()
            countdownTimer.cancel()
        } else {
            activity?.supportFragmentManager?.popBackStack(
                DoTestFragment::class.java.simpleName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            preferences.setString(
                Constants.KEY_CHECK_TYPE_DO_TEST,
                Constants.KEY_VALUE_TYPE_DO_TEST_ZERO
            )
            Constants.TYPECHECKTEST = 1
            countdownTimer.cancel()
        }
    }

    private fun setupAdapter() {
        adapterRandomTest = RandomExamAdapter { item, pos ->
            val listDefaultSquare = listDefault.toList().toMutableList()
            listDefaultSquare[pos] = listDefault[pos]
            listDefault = listDefaultSquare as ArrayList<Question>
            val listMarked = listDefault.filter { question -> question.marked != 0 }
            binding.questionDone2.text =
                String.format("%d/%d", listMarked.size, listDefault.size)
            adapterSquare.notifyDataSetChanged()
            if (item != null) {
                viewModelRandomTest.saveQuestion(item)
            }
            adapterRandomTest.notifyDataSetChanged()
        }
        binding.rcvTest.adapter = adapterRandomTest
        adapterSquare = SquareAdapter()
        binding.rcvSquare.setHasFixedSize(true)
        binding.rcvSquare.adapter = adapterSquare
    }

    private fun setTypeOfDoTest() {
        /**
         * check recheckQuestion
         * If recheckQuestion is of type 1, set the test review
         * else set the do test
         * */
        if (recheckQuestion == Constants.KEY_VALUE_TYPE_DO_TEST_ONE) {
            preferences.setString(
                Constants.KEY_CHECK_TYPE_DO_TEST,
                Constants.KEY_VALUE_TYPE_DO_TEST_ONE
            )
            Constants.TYPECHECKTEST = 2
            when (arguments?.getString(Constants.KEY_TYPE_LIST_QUEST_FROM)) {
                Constants.KEY_FROM_TEST -> saveBundle(Constants.KEY_FROM_TEST)
                Constants.KEY_FROM_RANDOM_TEST -> saveBundle(Constants.KEY_FROM_RANDOM_TEST)
            }
            setupUiIfCheckDoneQuestion()
        } else {
            saveBundle(Constants.KEY_FROM_DO_TEST)
            preferences.setString(
                Constants.KEY_CHECK_TYPE_DO_TEST,
                Constants.KEY_VALUE_TYPE_DO_TEST_ZERO
            )
            Constants.TYPECHECKTEST = 1
        }
    }

    private fun setupUiIfCheckDoneQuestion() {
        countDownTimeExam(null)
        binding.apply {
            layoutCountdowntime.gone()
            randomtestSubmit.gone()
            rcvSquare.gone()
            questionDone2.gone()
        }
    }

    private fun showDialogConfirm() {
        val dialog = CustomDialog(
            requireContext(),
            "Bạn có chắc chắn muốn nộp bài?",
            "Check kỹ lại các câu hỏi của mình trước khi nộp nhé",
            "Làm tiếp",
            "Ok"
        ) {
            navigateToShowExamResult()
        }
        dialog.show()
    }

    private fun navigateToShowExamResult() {
        val bundle = Bundle().apply {
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonQuestionList)
            putString(Constants.KEY_EXAM_NAME, examName)
            putString(Constants.KEY_TEST_ID, testId)
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, Constants.KEY_FROM_DO_TEST)
        }
        saveBundleToPreferences(bundle, preferences)
        openFragment(ShowResultExamFragment::class.java, bundle, true)
    }

    private fun saveBundle(listQuestFrom: String) {
        val bundle = Bundle().apply {
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonQuestionList)
            putString(Constants.KEY_EXAM_NAME, examName)
            putString(Constants.KEY_TEST_ID, testId)
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, listQuestFrom)
        }
        saveBundleToPreferences(bundle, preferences)
    }

    private fun countDownTimeExam(totalTimes: Float?) {
        if (totalTimes != null) {
            countdownTimer = object : CountDownTimer(totalTimes.toLong() * 1000 * 60, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val millisPassed = (totalTimes.toLong() * 1000 * 60) - millisUntilFinished
                    val secondsPassedTotal = millisPassed / 1000
                    val minutesPassed = secondsPassedTotal / 60
                    val secondsPassed = secondsPassedTotal % 60

                    val secondsRemaining = millisUntilFinished / 1000
                    val minutes = secondsRemaining / 60
                    val seconds = secondsRemaining % 60
                    val countdownText = String.format("%02d:%02d", minutes, seconds)
                    val timePass = String.format("%02d:%02d", minutesPassed, secondsPassed)
                    preferences.setString(Constants.KEY_TIME_RESULT_EXAM, timePass)
                    binding.txtCountdown.text = countdownText
                }

                override fun onFinish() {
                    preferences.setString(Constants.KEY_TIME_RESULT_EXAM, "00:00")
                    navigateToShowExamResult()
                }
            }
            countdownTimer.start()
            isTimerRunning = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (isTimerRunning) {
            countdownTimer.cancel()
            isTimerRunning = false
        }
    }

    private fun setupTabAndAction(totalQuestion: Int) {
        binding.apply {
            for (i in 1..totalQuestion) {
                val tab = tabLayoutTest.newTab()
                val tabItemView = LayoutInflater.from(tabLayoutTest.context)
                    .inflate(R.layout.item_custom_for_tablayout, tabLayoutTest, false)
                val tabTxt = tabItemView.findViewById<TextView>(R.id.txtTablayout)
                tabTxt.text = "$i"
                tab.customView = tabItemView
                tabLayoutTest.addTab(tab)
            }

            fun updateButtonStates(currentItem: Int) {
                binding.apply {
                    when (currentItem) {
                        0 -> {
                            btnPrevious.setBackgroundResource(R.drawable.bg_circle)
                            btnNext.setBackgroundResource(R.drawable.bgr_btn_circle)
                        }

                        totalQuestion - 1 -> {
                            btnNext.setBackgroundResource(R.drawable.bg_circle)
                            btnPrevious.setBackgroundResource(R.drawable.bgr_btn_circle)
                        }

                        else -> {
                            btnPrevious.setBackgroundResource(R.drawable.bgr_btn_circle)
                            btnNext.setBackgroundResource(R.drawable.bgr_btn_circle)
                        }
                    }
                }

            }

            tabLayoutTest.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        rcvTest.setCurrentItem(it.position, true)
                        val selectedTabView = it.customView
                        val selectedTabBackground =
                            selectedTabView?.findViewById<View>(R.id.bg_circle)
                        selectedTabBackground?.setBackgroundResource(R.drawable.bg_circle_selected)
                        updateButtonStates(it.position)
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

            rcvTest.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayoutTest.selectTab(tabLayoutTest.getTabAt(position))
                    updateButtonStates(position)
                }
            })

            btnNext.setOnClickListener {
                val currentItem = rcvTest.currentItem
                if (currentItem < totalQuestion - 1) {
                    rcvTest.currentItem = currentItem + 1
                    updateButtonStates(rcvTest.currentItem)
                }
            }

            btnPrevious.setOnClickListener {
                val currentItem = rcvTest.currentItem
                if (currentItem > 0) {
                    rcvTest.currentItem = currentItem - 1
                    updateButtonStates(rcvTest.currentItem)
                }
            }
            updateButtonStates(rcvTest.currentItem)

            if (recheckQuestion == Constants.KEY_VALUE_TYPE_DO_TEST_ONE) {
                tabLayoutTest.post {
                    rollCurrentTabToPosition()
                }
            } else {
                Log.d("hien", "setupTabAndAction: ")
                rollCurrentTabToPosition(2)
                rollCurrentTabToPosition(1)
            }
        }
    }

    private fun rollCurrentTabToPosition(number: Int? = null) {
        var position = arguments?.getInt(Constants.KEY_POSITION_QUESTION)
        if (position == 0) {
            position = number
        }
        scrollToItemPosition(position!!)
    }

    private fun scrollToItemPosition(itemPosition: Int) {
        binding.apply {
            rcvTest.post {
                rcvTest.setCurrentItem(itemPosition - 1, true)
                tabLayoutTest.selectTab(tabLayoutTest.getTabAt(itemPosition - 1))
            }
        }
    }
}