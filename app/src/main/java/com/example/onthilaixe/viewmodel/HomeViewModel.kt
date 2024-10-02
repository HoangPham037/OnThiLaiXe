package com.example.onthilaixe.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthilaixe.R
import com.example.onthilaixe.ui.home.ItemHome
import com.example.onthilaixe.ui.inapp.Constant
import com.example.onthilaixe.ui.inapp.PrefHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private lateinit var pref: PrefHelper
    private val _currentGold : MutableLiveData<Int> = MutableLiveData()
    val currentGold: LiveData<Int> get() = _currentGold
    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == Constant.KEY_TOTAL_COIN) {
            _currentGold.value = pref.getValueCoin()
        }
    }

    init {
        pref = PrefHelper.getInstance(context)!!
        pref.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
        _currentGold.value = pref.getValueCoin()
    }

    override fun onCleared() {
        super.onCleared()
        pref.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
    private val _listItemHome = MutableLiveData<List<ItemHome>>()
    val listItemHome: LiveData<List<ItemHome>> get() = _listItemHome

    fun fetchListItemHome() {
        _listItemHome.value = setListItemHome()
    }

    private fun setListItemHome(): List<ItemHome> {
        return listOf(
            ItemHome(
                R.drawable.ic_item_home_one,
                "Đề ngẫu nhiên",
                "Bạn sẽ được làm những đề thi được lựa chọn ngẫu nhiên"
            ),
            ItemHome(
                R.drawable.ic_item_home_two,
                "Thi theo bộ đề",
                "Bạn sẽ được làm những đề thi theo bộ có sẵn"
            ),
            ItemHome(
                R.drawable.ic_item_home_three,
                "Xem câu bị sai",
                "Bạn sẽ được xem lại những câu mình làm sai để rút kinh nghiệm"
            ),
            ItemHome(
                R.drawable.ic_item_home_four,
                "Ôn tập câu hỏi",
                "Bạn sẽ được ôn tập các câu hỏi được sắp xếp theo nhóm"
            ),
            ItemHome(
                R.drawable.ic_item_home_five,
                "Các biển báo",
                "Bạn có thể ôn tập các câu hỏi về biển báo tại đây!"
            ),
            ItemHome(
                R.drawable.ic_item_home_six,
                "Mẹo ghi nhớ",
                "Ở đây có những mẹo giúp bạn ghi nhớ tốt hơn!"
            ),
            ItemHome(
                R.drawable.ic_item_home_seven,
                "60 câu điểm liệt",
                "Hãy chú ý những câu điểm liệt, nếu làm sai bài thi của bạn sẽ liệt!"
            ),
            ItemHome(
                R.drawable.ic_item_home_eight,
                "Top 50 câu sai",
                "Đây là những câu nhiều người sai, hãy chú ý nhé!"
            )
        )
    }

}