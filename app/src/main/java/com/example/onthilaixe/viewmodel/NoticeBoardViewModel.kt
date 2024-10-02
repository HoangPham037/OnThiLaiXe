package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
@HiltViewModel
class NoticeBoardViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    fun getALlNoticeBoard(type: Int): LiveData<List<NoticeBoards>> =
        repository.getALlNoticeBoard(type).flowOn(Dispatchers.Main)
            .asLiveData()
}