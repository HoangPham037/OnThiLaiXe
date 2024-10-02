package com.example.onthilaixe.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.example.onthilaixe.base.BaseActivity
import com.example.onthilaixe.customview.CustomDialog
import com.example.onthilaixe.databinding.ActivityMainBinding
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.dotest.DoTestFragment
import com.example.onthilaixe.ui.home.HomeFragment
import com.example.onthilaixe.ui.randomtest.RandomExamFragment
import com.example.onthilaixe.ui.showresultexam.ShowResultExamFragment
import com.example.onthilaixe.ui.splash.SplashFragment
import com.example.onthilaixe.ui.test.ExamFragment
import com.example.onthilaixe.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    @Inject
    lateinit var preferences: Preferences

    private var retrievedBundle = Bundle()
    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun init() {
        openFragment(SplashFragment::class.java, null, false)
    }

    private fun getBundleFromPreferences(preferences: Preferences): Bundle {
        val gson = Gson()
        val jsonString = preferences.getString("ShareDataToOnBackPressed")
        val map = gson.fromJson<Map<String, Any>>(
            jsonString,
            object : TypeToken<Map<String, Any>>() {}.type
        )
        return mapToBundle(map)
    }

    private fun mapToBundle(map: Map<String, Any>): Bundle {
        val bundle = Bundle()
        for ((key, value) in map) {
            when (value) {
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is String -> bundle.putString(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Float -> bundle.putFloat(key, value)
                is Double -> bundle.putDouble(key, value)
            }
        }
        return bundle
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(android.R.id.content)) {
            is HomeFragment -> {
                val dialog = CustomDialog(
                    this,
                    "Bạn có chắc chắn muốn thoát?",
                    "",
                    "Ở lại",
                    "Thoát"
                ) {
                    finish()
                }
                dialog.show()
            }

            is RandomExamFragment -> {
                val dialog = CustomDialog(
                    this,
                    "Bạn có chắc chắn muốn nộp bài?",
                    "Hãy kiểm tra kỹ lại kỹ các câu hỏi của mình trước khi nộp bài",
                    "Làm tiếp",
                    "Kết thúc"
                ) {
                    retrievedBundle = getBundleFromPreferences(preferences)
                    openFragment(ShowResultExamFragment::class.java, retrievedBundle, true)
                }
                dialog.show()
            }

            is ShowResultExamFragment -> {
                retrievedBundle = getBundleFromPreferences(preferences)
                when (retrievedBundle.getString(Constants.KEY_TYPE_LIST_QUEST_FROM)) {
                    Constants.KEY_FROM_TEST -> {
                        supportFragmentManager.popBackStack()
                    }

                    Constants.KEY_FROM_RANDOM_TEST -> {
                        supportFragmentManager.popBackStack(
                            RandomExamFragment::class.java.simpleName,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }

                    Constants.KEY_FROM_DO_TEST -> {
                        supportFragmentManager.popBackStack(
                            DoTestFragment::class.java.simpleName,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }

                    else -> {
                        supportFragmentManager.popBackStack(
                            ShowResultExamFragment::class.java.simpleName,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                }
            }

            is DoTestFragment -> {
                val recheckQuestion = preferences.getString(Constants.KEY_CHECK_TYPE_DO_TEST)
                if (recheckQuestion == Constants.KEY_VALUE_TYPE_DO_TEST_ONE) {
                    supportFragmentManager.popBackStack(
                        DoTestFragment::class.java.simpleName,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    preferences.setString(
                        Constants.KEY_CHECK_TYPE_DO_TEST,
                        Constants.KEY_VALUE_TYPE_DO_TEST_ZERO
                    )
                    Constants.TYPECHECKTEST = 1
                } else {
                    val dialog = CustomDialog(
                        this,
                        "Bạn có chắc chắn muốn nộp bài?",
                        "Hãy kiểm tra kỹ lại kỹ các câu hỏi của mình trước khi nộp bài",
                        "Làm tiếp",
                        "Kết thúc"
                    ) {
                        retrievedBundle = getBundleFromPreferences(preferences)
                        openFragment(ShowResultExamFragment::class.java, retrievedBundle, true)
                    }
                    dialog.show()
                }
            }

            is ExamFragment -> {
                supportFragmentManager.popBackStack()
            }

            else -> super.onBackPressed()
        }
    }
}