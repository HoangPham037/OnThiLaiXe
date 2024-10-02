package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthilaixe.R
import com.example.onthilaixe.ui.noticeboardtype.ItemNoticeBoardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeBoardTTypeViewModel @Inject constructor() : ViewModel() {

    private val _listItemHome = MutableLiveData<List<ItemNoticeBoardType>>()
    val listItemHome: LiveData<List<ItemNoticeBoardType>> get() = _listItemHome

    fun fetchListItemHome() {
        _listItemHome.value = setListItemHome()
    }

    private fun setListItemHome(): List<ItemNoticeBoardType> {
        return listOf(
            ItemNoticeBoardType(R.drawable.ic_pratice_type_1, "Biển báo cấm", 1),
            ItemNoticeBoardType(R.drawable.ic_pratice_type_2, "Biển hiệu lệnh", 2),
            ItemNoticeBoardType(R.drawable.ic_pratice_type_3, "Biển chỉ dẫn", 3),
            ItemNoticeBoardType(R.drawable.ic_pratice_type_4, "Biển báo nguy hiểm và cảnh báo", 4),
            ItemNoticeBoardType(R.drawable.ic_pratice_type_5, "Biển báo phụ", 5)
        )
    }
}