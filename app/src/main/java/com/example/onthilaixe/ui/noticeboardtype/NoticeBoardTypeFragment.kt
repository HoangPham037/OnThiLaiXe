package com.example.onthilaixe.ui.noticeboardtype

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.databinding.FragmentNoticeBoardTypeBinding
import com.example.onthilaixe.ui.noticeboard.NoticeBoardFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.viewmodel.NoticeBoardTTypeViewModel
import com.example.onthilaixe.viewmodel.NoticeBoardViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeBoardTypeFragment : BaseFragmentWithBinding<FragmentNoticeBoardTypeBinding>() {
    private lateinit var noticeBoardTypeAdapter: NoticeBoardTypeAdapter
    private val noticeBoardViewModel: NoticeBoardViewModel by viewModels()
    private val noticeBoardTypeViewModel: NoticeBoardTTypeViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater): FragmentNoticeBoardTypeBinding {
        return FragmentNoticeBoardTypeBinding.inflate(layoutInflater)
    }

    override fun init() {
        setupAdapter()
    }

    override fun initData() {
        noticeBoardTypeViewModel.listItemHome.observe(viewLifecycleOwner) {
            noticeBoardTypeAdapter.submitList(it)
        }
        noticeBoardTypeViewModel.fetchListItemHome()
    }

    private fun setupAdapter() {
        noticeBoardTypeAdapter = NoticeBoardTypeAdapter(
            noticeBoardViewModel,
            viewLifecycleOwner
        ) { listNoticeBoard, title ->
            val gson = Gson()
            val jsonQuestionList = gson.toJson(listNoticeBoard)
            val bundle = Bundle().apply {
                putString(Constants.KEY_LIST_NOTICE_BOARD, jsonQuestionList)
                putString(Constants.KEY_TITLE_TOOLBAR_NOTICE, title)
            }
            openFragment(NoticeBoardFragment::class.java, bundle, true)
        }
        binding.rcvBoardType.adapter = noticeBoardTypeAdapter
    }

    override fun initAction() {
        setToolbar(binding.toolbarNoticeBoard)
    }

}