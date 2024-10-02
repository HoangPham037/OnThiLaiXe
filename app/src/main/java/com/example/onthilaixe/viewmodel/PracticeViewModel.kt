package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onthilaixe.R
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.repository.Repository
import com.example.onthilaixe.ui.practice.ItemLicenseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _listItemHome = MutableLiveData<List<ItemLicenseType>>()
    val listItemHome: LiveData<List<ItemLicenseType>> get() = _listItemHome

    fun fetchListItemHome() {
        _listItemHome.value = setListItemHome()
    }

    private fun setListItemHome(): List<ItemLicenseType> {
        return listOf(
            ItemLicenseType(R.drawable.category_0, "Khái niệm và quy tắc", 1),
            ItemLicenseType(R.drawable.category_1, "Văn hóa và đạo đức lái xe", 3),
            ItemLicenseType(R.drawable.category_3, "Kỹ thuật lái xe", 4),
            ItemLicenseType(R.drawable.category_4, "Cấu tạo và sửa chữa", 5),
            ItemLicenseType(R.drawable.category_5, "Biển báo đường bộ", 6),
            ItemLicenseType(R.drawable.category_6, "Sa hình", 7)
        )
    }

    suspend fun saveQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveQuestion(question)
        }
    }
}