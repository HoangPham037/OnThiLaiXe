package com.example.onthilaixe.utils

import android.os.Bundle
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveBundleToPreferences(bundle: Bundle, preferences: Preferences) {
    val gson = Gson()
    val jsonString = gson.toJson(bundleToMap(bundle))
    preferences.setString("ShareDataToOnBackPressed", jsonString)
}

fun bundleToMap(bundle: Bundle): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    for (key in bundle.keySet()) {
        map[key] = bundle.get(key)
    }
    return map
}

 fun saveListToPreferences(preferences: Preferences,list: List<Question>, key: String) {
    val gson = Gson()
    val json = gson.toJson(list)
    preferences.setString(key, json)
}
 fun loadListFromPreferences(preferences: Preferences, key:String): List<Question> {
    val gson = Gson()
    val json = preferences.getString(key)
    return if (json != null) {
        val type = object : TypeToken<List<Question>>() {}.type
        gson.fromJson(json, type)
    } else {
        emptyList()
    }
}

fun updateListDefault(listDefault: List<Question>, listQuestionWrong: List<Question>): List<Question> {
    val newListDefault = listQuestionWrong.map { question ->
        listDefault.find { it.pk == question.pk } ?: question.copy(marked = 0)
    }
    return newListDefault
}

