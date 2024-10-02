package com.example.onthilaixe.ui.noticeboard

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.databinding.FragmentNoticeBoardBinding
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.utils.Constants
import com.google.gson.Gson

class NoticeBoardFragment : BaseFragmentWithBinding<FragmentNoticeBoardBinding>() {
    private lateinit var noticeBoardList: ArrayList<NoticeBoards>
    private lateinit var noticeBoardAdapter: NoticeBoardAdapter

    override fun getViewBinding(inflater: LayoutInflater): FragmentNoticeBoardBinding {
        return FragmentNoticeBoardBinding.inflate(layoutInflater)
    }

    override fun init() {
        noticeBoardAdapter = NoticeBoardAdapter()
        binding.rcvNotices.adapter = noticeBoardAdapter
    }

    override fun initData() {
        val gson = Gson()
        val jsonQuestionList: String? = arguments?.getString(Constants.KEY_LIST_NOTICE_BOARD)
        noticeBoardList = gson.fromJson(
            jsonQuestionList,
            object : com.google.gson.reflect.TypeToken<ArrayList<NoticeBoards>>() {}.type
        )
        val title = arguments?.getString(Constants.KEY_TITLE_TOOLBAR_NOTICE)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNoticeBoard)
        (activity as AppCompatActivity).supportActionBar?.title = title
        noticeBoardAdapter.submitList(noticeBoardList)
    }

    override fun initAction() {
        setToolbar(binding.toolbarNoticeBoard)
    }
}