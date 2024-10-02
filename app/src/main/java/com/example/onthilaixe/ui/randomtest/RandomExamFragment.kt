package com.example.onthilaixe.ui.randomtest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentRandomTestBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.showresultexam.ShowResultExamFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.utils.saveBundleToPreferences
import com.example.onthilaixe.viewmodel.RandomExamViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RandomExamFragment : BaseFragmentWithBinding<FragmentRandomTestBinding>() {

    private lateinit var adapterRandomTest: RandomExamAdapter
    private lateinit var adapterSquare: SquareAdapter
    private val viewModelRandomTest: RandomExamViewModel by viewModels()
    lateinit var bundle: Bundle
    private var listDefault = arrayListOf<Question>()
    private lateinit var countdownTimer: CountDownTimer
    private var isTimerRunning = false
    private var randomTestId: Int = 0

    @Inject
    lateinit var preferences: Preferences

    override fun getViewBinding(inflater: LayoutInflater): FragmentRandomTestBinding {
        return FragmentRandomTestBinding.inflate(layoutInflater)
    }

    override fun init() {
        setupAdapter()
    }

    override fun initData() {
        val numberOfQuestion = preferences.getInt(Constants.KEY_NUMBER_OF_QUEST)
        numberOfQuestion?.let {
            setupTabAndAction(it)
        }
        fetchRandomTest()
    }

    override fun initAction() {
        binding.imgBack.click {
            showDialogConfirm()
            countdownTimer.cancel()
        }
        binding.randomtestSubmit.click {
            showDialogConfirm()
            countdownTimer.cancel()
        }
    }

    private fun setupAdapter() {
        adapterRandomTest = RandomExamAdapter { item, pos ->
            val listDefaultSquare = listDefault.toList().toMutableList()
            listDefaultSquare[pos] = listDefault[pos]
            listDefault = listDefaultSquare as ArrayList<Question>
            val listMarked = listDefault.filter { question -> question.marked != 0 }
            binding.tvTotalQuestClicked.text =
                String.format("%d/%d", listMarked.size, listDefault.size)
            navigateToShowExamResult(false)
            adapterSquare.notifyDataSetChanged()

            if (item != null) {
                viewModelRandomTest.saveQuestion(item)
            }
            adapterRandomTest.notifyDataSetChanged()

        }
        binding.rcvRandomTest.adapter = adapterRandomTest
        adapterSquare = SquareAdapter()
        binding.rcvSquare.setHasFixedSize(true)
        binding.rcvSquare.adapter = adapterSquare

    }

    private fun showDialogConfirm() {
        val dialog = CustomDialog(
            requireContext(),
            "Bạn có chắc chắn muốn nộp bài?",
            "Hãy kiểm tra kỹ lại kỹ các câu hỏi của mình trước khi nộp bài",
            "Làm tiếp",
            "Kết thúc"
        ) {
            navigateToShowExamResult(true)
        }
        dialog.show()
    }

    private fun navigateToShowExamResult(goWhere: Boolean) {
        val gson = Gson()
        val jsonListQuestion = gson.toJson(listDefault)
        val bundle = Bundle().apply {
            putString(Constants.KEY_JSON_LIST_QUESTION_SEND, jsonListQuestion)
            putString(Constants.KEY_EXAM_NAME, "random")
            putString(Constants.KEY_TYPE_LIST_QUEST_FROM, Constants.KEY_FROM_RANDOM_TEST)
            putString(Constants.KEY_TEST_ID, randomTestId.toString())
        }
        if (goWhere) openFragment(ShowResultExamFragment::class.java, bundle, true)
        saveBundleToPreferences(bundle, preferences)
    }


    private fun countDownTime(int: Float) {
        countdownTimer = object : CountDownTimer(int.toLong() * 1000 * 60, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val millisPassed = (int.toLong() * 1000 * 60) - millisUntilFinished
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
                binding.txtCountdown.text = "0"
                preferences.setString(Constants.KEY_TIME_RESULT_EXAM, "00:00")
                navigateToShowExamResult(true)
            }
        }
        countdownTimer.start()
        isTimerRunning = true
    }

    private fun setupTabAndAction(totalQuestion: Int) {
        binding.apply {
            for (i in 1..totalQuestion) {
                val tab = tabLayout.newTab()
                val tabItemView = LayoutInflater.from(tabLayout.context)
                    .inflate(R.layout.item_custom_for_tablayout, tabLayout, false)
                val tabTxt = tabItemView.findViewById<TextView>(R.id.txtTablayout)
                tabTxt.text = "$i"
                tab.customView = tabItemView
                tabLayout.addTab(tab)
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

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        rcvRandomTest.setCurrentItem(it.position, true)
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

            rcvRandomTest.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                    updateButtonStates(position)
                }
            })
            btnNext.setOnClickListener {
                val currentItem = rcvRandomTest.currentItem
                if (currentItem < totalQuestion - 1) {
                    rcvRandomTest.currentItem = currentItem + 1
                    updateButtonStates(rcvRandomTest.currentItem)
                }
            }


            btnPrevious.setOnClickListener {
                val currentItem = rcvRandomTest.currentItem
                if (currentItem > 0) {
                    rcvRandomTest.currentItem = currentItem - 1
                    updateButtonStates(rcvRandomTest.currentItem)
                }
            }
            updateButtonStates(rcvRandomTest.currentItem)
        }
        scrollToItemPosition(2)
        scrollToItemPosition(1)

    }

    private fun scrollToItemPosition(itemPosition: Int) {
        binding.apply {
            rcvRandomTest.post {
                rcvRandomTest.setCurrentItem(itemPosition - 1, true)
                tabLayout.selectTab(tabLayout.getTabAt(itemPosition - 1))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isTimerRunning) {
            countdownTimer.cancel()
            isTimerRunning = false
        }
    }

    private fun fetchRandomTest() {
        val license = preferences.getString(Constants.KEY_LICENSE_TYPE)
        binding.tvTitle.text = String.format("%s", "Đề ngẫu nhiên - $license")
        val durations = preferences.getLong(Constants.KEY_DURATION)
        durations?.let { duration -> countDownTime(duration) }
        if (license != null) {
            viewModelRandomTest.listTestId.observe(viewLifecycleOwner) { listTestId ->
                val randomTestId = listTestId.random()
                viewModelRandomTest.listQuestionId.observe(viewLifecycleOwner) { listQuestionId ->
                    viewModelRandomTest.listQuestion.observe(viewLifecycleOwner) { listQuestion ->
                        listDefault = listQuestion.map { question ->
                            question.copy(learned = 0, marked = 0)
                        } as ArrayList<Question>
                        adapterRandomTest.submitList(listDefault)
                        adapterSquare.submitList(listDefault)
                        navigateToShowExamResult(false)
                    }
                    viewModelRandomTest.getQuestionsByQuestionIdTest(listQuestionId)
                }
                viewModelRandomTest.getAllTestQuest(randomTestId)
            }
            viewModelRandomTest.getAllTestIdByClassLicense(license)
        }
    }
}