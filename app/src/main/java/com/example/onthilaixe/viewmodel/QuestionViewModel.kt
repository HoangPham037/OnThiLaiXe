package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onthilaixe.models.local.room.entity.License
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun getALlQuestionByType(type: Int, chosenInclude: String): LiveData<List<Question>> =
        repository.getALlQuestionByType(type, chosenInclude).flowOn(Dispatchers.Main)
            .asLiveData(context = viewModelScope.coroutineContext)
    fun getALlQuestionWithLicense(chosenInclude: String): LiveData<List<Question>> =
        repository.getALlQuestionWithLicense(chosenInclude).flowOn(Dispatchers.Main)
            .asLiveData()
    fun getAllQuestionWrong(): LiveData<List<Question>> =
        repository.getAllQuestionWrong().flowOn(Dispatchers.Main).asLiveData()

    fun deleteAllQuestionLearned(ids: List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllQuestionLearned(ids)
        }
    }
}