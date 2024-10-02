package com.example.onthilaixe.ui.home

import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.base.extension.click
import com.example.onthilaixe.base.extension.formatTitleHome
import com.example.onthilaixe.customview.SpacesItemDecoration
import com.example.onthilaixe.databinding.FragmentHomeBinding
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.inapp.PurchaseActivity
import com.example.onthilaixe.ui.license.ChooseLicenseFragment
import com.example.onthilaixe.ui.noticeboardtype.NoticeBoardTypeFragment
import com.example.onthilaixe.ui.practice.PracticeQuestionFragment
import com.example.onthilaixe.ui.randomtest.RandomExamFragment
import com.example.onthilaixe.ui.test.ExamFragment
import com.example.onthilaixe.ui.tips.TipsFragment
import com.example.onthilaixe.ui.wrongsentences.WrongSentencesFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.utils.loadListFromPreferences
import com.example.onthilaixe.utils.saveListToPreferences
import com.example.onthilaixe.utils.updateListDefault
import com.example.onthilaixe.viewmodel.HomeViewModel
import com.example.onthilaixe.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragmentWithBinding<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter
    private val questionViewModel: QuestionViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun init() {
        setupTitle()
        setupAdapter()
    }

    override fun initData() {
        homeViewModel.currentGold.observe(viewLifecycleOwner) {
            binding.tvGold.text = it.toString()
        }
        homeViewModel.listItemHome.observe(viewLifecycleOwner) { listItemHome ->
            homeAdapter.submitList(listItemHome)
        }
        homeViewModel.fetchListItemHome()
    }

    override fun initAction() {
        setupActionMenu()
        binding.tvGold.click {
            startActivity(Intent(requireContext(), PurchaseActivity::class.java))
        }
    }

    private fun setupTitle() {
        val title = preferences.getString(Constants.KEY_LICENSE_TYPE)
        binding.tvTitle.text = title?.formatTitleHome()
    }

    private fun setupAdapter() {
        homeAdapter = HomeAdapter { position ->
            when (position) {
                0 -> {
                    openFragment(RandomExamFragment::class.java, null, true)
                }

                1 -> {
                    openFragment(ExamFragment::class.java, null, true)
                }

                2 -> {
                    saveListWrong()
                    openFragment(WrongSentencesFragment::class.java, null, true)
                }

                3 -> {
                    openFragment(PracticeQuestionFragment::class.java, null, true)
                }

                4 -> {
                    openFragment(NoticeBoardTypeFragment::class.java, null, true)
                }

                5 -> {
                    openFragment(TipsFragment::class.java, null, true)
                }

                6 -> {
                    toast("Tinh năng đang phát triển")
                }

                7 -> {
                    toast("Tinh năng đang phát triển")
                }
            }

        }
        val spacesItemDecoration = SpacesItemDecoration(20)
        binding.rcItemHome.addItemDecoration(spacesItemDecoration)
        binding.rcItemHome.adapter = homeAdapter
    }

    private fun setupActionMenu() {
        binding.imgSetting.click {
            openFragment(ChooseLicenseFragment::class.java, null, true)
        }
    }

    private fun saveListWrong() {
        questionViewModel.getAllQuestionWrong().observe(viewLifecycleOwner) { listQuestionWrong ->
            val listDefault = loadListFromPreferences(preferences, Constants.KEY_LIST_WRONG_DEFAULT)
            if (listDefault.isEmpty()) {
                val newListDefault =
                    listQuestionWrong.map { question -> question.copy(marked = 0) } as ArrayList<Question>
                saveListToPreferences(preferences, newListDefault, Constants.KEY_LIST_WRONG_DEFAULT)
            } else {
                val updatedListDefault = updateListDefault(listDefault, listQuestionWrong)
                if (listDefault != updatedListDefault) {
                    saveListToPreferences(
                        preferences,
                        updatedListDefault,
                        Constants.KEY_LIST_WRONG_DEFAULT
                    )
                }
            }
        }
    }
}