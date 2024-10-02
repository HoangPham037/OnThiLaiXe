package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onthilaixe.models.local.room.entity.Test
import com.example.onthilaixe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExamViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    fun getAllTestWithClassLicense(license: String): LiveData<List<Test>> =
        repository.getAllTestWithClassLicense(license).flowOn(Dispatchers.Main)
            .asLiveData(context = viewModelScope.coroutineContext)

    fun resetAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetAll()
        }
    }

    fun deleteAllTest() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTest()
        }
    }
}