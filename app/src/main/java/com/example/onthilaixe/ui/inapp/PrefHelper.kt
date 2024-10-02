package com.example.onthilaixe.ui.inapp

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(private val sharedPreferences: SharedPreferences) {
    fun setValueCoin(value: Int) {
        sharedPreferences.edit().putInt(Constant.KEY_TOTAL_COIN, value).apply()
    }

    fun getValueCoin(): Int {
        return sharedPreferences.getInt(Constant.KEY_TOTAL_COIN, Constant.INT_ZERO)
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val PREFS_NAME = "share_prefs"
        private var INSTANCE: PrefHelper? = null
        @JvmStatic
        fun getInstance(context: Context): PrefHelper? {
            if (INSTANCE == null) {
                synchronized(PrefHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PrefHelper(context.getSharedPreferences(PREFS_NAME, 0))
                    }
                }
            }
            return INSTANCE
        }
    }
}