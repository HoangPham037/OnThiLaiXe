package com.example.onthilaixe.ui.tips

import android.view.LayoutInflater
import android.view.View
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.databinding.FragmentTipsBinding

class TipsFragment : BaseFragmentWithBinding<FragmentTipsBinding>() {
    override fun getViewBinding(inflater: LayoutInflater): FragmentTipsBinding {
        return FragmentTipsBinding.inflate(layoutInflater)
    }

    override fun init() {
        setToolbar(binding.toolbarMain)
        binding.webViewTip.scrollBarSize = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webViewTip.settings.setSupportZoom(false)
        binding.webViewTip.loadUrl("file:///android_asset/tips_light.html")
        binding.webViewTip.settings.builtInZoomControls = false
    }

    override fun initData() {
        //do nothing
    }

    override fun initAction() {
        //do nothing
    }

}