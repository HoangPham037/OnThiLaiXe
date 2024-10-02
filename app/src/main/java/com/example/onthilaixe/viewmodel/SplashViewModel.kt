package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _navigateToNextScreen = MutableLiveData<Unit>()
    val navigateToNextScreen: LiveData<Unit> get() = _navigateToNextScreen

    private val _max = MutableLiveData<Int>()
    val max: LiveData<Int> get() = _max

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    init {
        _max.value = 100
        _progress.value = 0
    }

    fun startDelay() {
        viewModelScope.launch {
            val delayTime = 2000L
            val interval = 100L
            val steps = delayTime / interval
            for (i in 1..steps) {
                delay(interval)
                _progress.value = (i * 100 / steps).toInt()
            }
            _navigateToNextScreen.value = Unit
        }
    }
}