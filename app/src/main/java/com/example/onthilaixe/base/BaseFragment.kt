package com.example.onthilaixe.base


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.onthilaixe.base.extension.hideKeyboard
import com.google.android.material.appbar.MaterialToolbar

/**
 * create at 06/05/2024 by phamhoang99
 * */
abstract class BaseFragment : Fragment() {
    private var activity : BaseActivity<*>? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { v, _ -> // Ẩn bàn phím nếu đang hiển thị
            v.hideKeyboard(requireActivity())
            v.clearFocus()
            true
        }
        activity = requireActivity() as BaseActivity<*>
        init()
        initData()
        initAction()
    }

    fun openFragment(fragmentClazz: Class<*>, args: Bundle?,
        addBackStack: Boolean
    ) {
        activity?.openFragment(fragmentClazz, args, addBackStack)
    }

    open fun setToolbar(view: MaterialToolbar, onclick: (() -> Unit)? = null) {
        view.setNavigationOnClickListener {
            activity?.onBackPressed()
            onclick?.invoke()
        }
    }

    fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
    fun onBackPressed() {
        activity?.onBackPressed()
    }

    abstract fun init()
    abstract fun initData()
    abstract fun initAction()

}