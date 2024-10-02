package com.example.onthilaixe.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.onthilaixe.base.extension.gone
import com.example.onthilaixe.databinding.CustomDialogBinding

class CustomDialog(
    context: Context,
    val title: String,
    val content: String?,
    private val btnNegative: String,
    private val btnPositive: String,
    private val callBack: () -> Unit
) : Dialog(context) {
    lateinit var binding: CustomDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(null)
        initViews()
    }

    private fun initViews() {
        binding.tvPositive.text = btnPositive
        binding.tvTitle.text = title
        if (content?.isNotEmpty() == true) {
            binding.tvContent.text = content
        }else {
            binding.tvContent.gone()
        }

        binding.btnNegative.text = btnNegative
        binding.btnNegative.setOnClickListener {
            dismiss()
        }
        binding.tvPositive.setOnClickListener {
            callBack.invoke()
            dismiss()
        }
    }
}