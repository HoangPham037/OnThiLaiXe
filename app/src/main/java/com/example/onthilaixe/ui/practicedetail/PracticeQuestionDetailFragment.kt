package com.example.onthilaixe.ui.practicedetail

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.onthilaixe.R
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.formatTotalQuestion
import com.example.onthilaixe.databinding.FragmentPractivesQuestionBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.viewmodel.PracticeViewModel
import com.example.onthilaixe.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PracticeQuestionDetailFragment : BaseFragmentWithBinding<FragmentPractivesQuestionBinding>() {
    private lateinit var practiceQuestionAdapter: PracticeQuestionDetailAdapter
    private lateinit var questionList: ArrayList<Question>
    private val practiceViewModel: PracticeViewModel by viewModels()
    override fun getViewBinding(inflater: LayoutInflater): FragmentPractivesQuestionBinding {
        return FragmentPractivesQuestionBinding.inflate(layoutInflater)
    }

    override fun init() {
        val gson = Gson()
        val jsonQuestionList: String? = arguments?.getString(Constants.KEY_LIST_QUESTION)
        questionList =
            gson.fromJson(jsonQuestionList, object : TypeToken<ArrayList<Question>>() {}.type)
        val title = arguments?.getString(Constants.KEY_TITLE_TOOLBAR)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMain)
        (activity as AppCompatActivity).supportActionBar?.title = title
        setupAdapter()
        setupTabAndAction()
    }

    override fun initData() {
    }

    override fun initAction() {
            setToolbar(binding.toolbarMain)
    }

    private fun setupAdapter() {
        practiceQuestionAdapter = PracticeQuestionDetailAdapter { item, _ ->
            if (item != null) {
                lifecycleScope.launch {
                    practiceViewModel.saveQuestion(item)
                }
            }
            practiceQuestionAdapter.notifyDataSetChanged()
        }
        binding.rcvRandomTest.adapter = practiceQuestionAdapter
        practiceQuestionAdapter.submitList(questionList)
    }

    private fun setupTabAndAction() {
        binding.apply {
            val listSize = practiceQuestionAdapter.listItem.size
            binding.tvTotalQuestion.text = listSize.toString().formatTotalQuestion()
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
                if (currentItem < listSize - 1) {
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
    }

}