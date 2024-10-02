package com.example.onthilaixe.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
/**
 * create at 06/05/2024 by phamhoang99
 * */
abstract class BaseFragmentWithBinding<VB : ViewBinding> : BaseFragment() {


    private lateinit var _binding : VB
    val binding get() = _binding

    abstract fun getViewBinding(inflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater)
        return binding.root
    }
}
