package com.example.onthilaixe.ui.wrongsentences

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.gone
import com.example.onthilaixe.base.extension.visible
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.FragmentWrongSentencesBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.utils.loadListFromPreferences
import com.example.onthilaixe.utils.saveListToPreferences
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WrongSentencesFragment : BaseFragmentWithBinding<FragmentWrongSentencesBinding>(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var adapterRandomTest: WrongQuestionAdapter
    private lateinit var currentListFromPreferences: List<Question>

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentWrongSentencesBinding {
        return FragmentWrongSentencesBinding.inflate(layoutInflater)
    }

    override fun init() {
        setupAdapter()
    }

    override fun initData() {
        currentListFromPreferences = loadListFromPreferences(
            preferences,
            Constants.KEY_LIST_WRONG_DEFAULT
        )

        if (currentListFromPreferences.isEmpty()) {
            binding.layoutNoData.visible()
        } else {
            binding.layoutNoData.gone()
            adapterRandomTest.submitList(currentListFromPreferences)
            binding.tvTotalQuestion.text = currentListFromPreferences.size.toString()
            setupTabAndAction(currentListFromPreferences as ArrayList<Question>)
        }
    }

    override fun initAction() {
        setToolbar(binding.toolbarMain)
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
                        val listResetQuest = loadListFromPreferences(preferences,Constants.KEY_LIST_WRONG_DEFAULT).map { question ->
                            question.copy(marked = 0, learned = 0)
                        }
                        saveListToPreferences(preferences, listResetQuest, Constants.KEY_LIST_WRONG_DEFAULT)
                        onBackPressed()
                    }
                    dialogConfirmDelete.show()
                }
            }
            true
        }
    }

    private fun setupAdapter() {
        adapterRandomTest = WrongQuestionAdapter { item, pos ->
            if (item != null) {
                (currentListFromPreferences as ArrayList<Question>)[pos] = item
                saveListToPreferences(
                    preferences,
                    currentListFromPreferences,
                    Constants.KEY_LIST_WRONG_DEFAULT
                )
            }
            adapterRandomTest.notifyDataSetChanged()
        }
        binding.rcvWrongQuestion.adapter = adapterRandomTest
    }

    private fun setupTabAndAction(listQuestWrong: ArrayList<Question>) {
        binding.apply {
            val listSize = listQuestWrong.size
            for (i in 1..listSize) {
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

                        listSize - 1 -> {
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
                        rcvWrongQuestion.setCurrentItem(it.position, true)
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

            rcvWrongQuestion.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                    updateButtonStates(position)
                }
            })
            btnNext.setOnClickListener {
                val currentItem = rcvWrongQuestion.currentItem
                if (currentItem < listSize - 1) {
                    rcvWrongQuestion.currentItem = currentItem + 1
                    updateButtonStates(rcvWrongQuestion.currentItem)
                }
            }


            btnPrevious.setOnClickListener {
                val currentItem = rcvWrongQuestion.currentItem
                if (currentItem > 0) {
                    rcvWrongQuestion.currentItem = currentItem - 1
                    updateButtonStates(rcvWrongQuestion.currentItem)
                }
            }
            updateButtonStates(rcvWrongQuestion.currentItem)
            scrollToItemPosition(2)
            scrollToItemPosition(1)
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, key: String?) {
        when (key) {
            Constants.KEY_LIST_WRONG_DEFAULT -> {
                currentListFromPreferences =
                    loadListFromPreferences(
                        preferences,
                        Constants.KEY_LIST_WRONG_DEFAULT
                    ) as ArrayList<Question>
            }
        }
    }

    private fun scrollToItemPosition(itemPosition: Int) {
        binding.apply {
            rcvWrongQuestion.post {
                rcvWrongQuestion.setCurrentItem(itemPosition - 1, true)
                tabLayout.selectTab(tabLayout.getTabAt(itemPosition - 1))
            }
        }
    }
}