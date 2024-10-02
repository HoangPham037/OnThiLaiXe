package com.example.onthilaixe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.onthilaixe.models.local.room.entity.License
import com.example.onthilaixe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
@HiltViewModel
class LicenseViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun getALlLicense(): LiveData<List<License>> =
        repository.getALlLicense().flowOn(Dispatchers.Main)
            .asLiveData()
}