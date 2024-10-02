package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RandomExamViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _listQuestionId : MutableLiveData<List<Int>> = MutableLiveData()
    val listQuestionId : LiveData<List<Int>> get() = _listQuestionId
    fun getAllTestQuest(type: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _listQuestionId.value = repository.getAllTestQuest(type)
        }
    }

    fun getQuestionIdFromTestIds(testId: Int): LiveData<List<Int>> =
        repository.getQuestionIdFromTestIds(testId).flowOn(Dispatchers.Main)
            .asLiveData(context = viewModelScope.coroutineContext)


    private val _listTestId: MutableLiveData<List<Int>> = MutableLiveData()
    val listTestId : LiveData<List<Int>> get() = _listTestId
    fun getAllTestIdByClassLicense(license: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _listTestId.value = repository.getAllTestIdByClassLicense(license)
        }
    }

    fun getQuestionsByQuestionIds(questionIds: List<Int>): LiveData<List<Question>>? =
        repository.getQuestionsByQuestionIds(questionIds)?.flowOn(Dispatchers.Main)
            ?.asLiveData(context = viewModelScope.coroutineContext)

    private val _listQuestion : MutableLiveData<List<Question>> = MutableLiveData()
    val listQuestion : LiveData<List<Question>> get() = _listQuestion
    fun getQuestionsByQuestionIdTest(questionIds: List<Int>) {
        viewModelScope.launch() {
            _listQuestion.value = repository.getQuestionsByQuestionIdTest(questionIds)
        }
    }

    fun saveQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveQuestion(question)
        }
    }

    fun resetMarkedQuestById(list: List<Int>) {
        viewModelScope.launch {
            repository.resetMarkedQuestById(list)
        }
    }

    fun updateTestIsFinish(testId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTestIsFinish(testId)
        }
    }

    fun updateTestIsFailed(testId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTestIsFailed(testId)
        }
    }

    fun updateWrongInQuestion(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWrongInQuestion(ids)
        }
    }

    fun resetTestDefaultById(idTest: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetTestDefaultById(idTest)
        }
    }
}