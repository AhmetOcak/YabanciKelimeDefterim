package com.yabancikelimedefteri.core.helpers

import android.content.SharedPreferences
import com.yabancikelimedefteri.core.navigation.CAT_LIST_TYPE_KEY
import com.yabancikelimedefteri.core.navigation.WORD_LIST_TYPE_KEY
import com.yabancikelimedefteri.core.navigation.ListType

fun SharedPreferences.Editor.saveTheme(themeCode: Int) {
    putInt("current_theme", themeCode)
    apply()
}

fun SharedPreferences.getCurrentTheme(): Int {
    return getInt("current_theme", -1)
}

fun SharedPreferences.getCurrentWordListType(): Boolean {
    return getBoolean(WORD_LIST_TYPE_KEY, false)
}

fun SharedPreferences.getCurrentCatListType(): Boolean {
    return getBoolean(CAT_LIST_TYPE_KEY, false)
}

fun SharedPreferences.Editor.saveNewWordListType(listType: ListType) {
    when(listType) {
        ListType.RECTANGLE -> {
            putBoolean(WORD_LIST_TYPE_KEY, true)
        }
        ListType.THIN -> {
            putBoolean(WORD_LIST_TYPE_KEY, false)
        }
    }
    apply()
}

fun SharedPreferences.Editor.saveNewCatListType(listType: ListType) {
    when(listType) {
        ListType.RECTANGLE -> {
            putBoolean(CAT_LIST_TYPE_KEY, true)
        }
        ListType.THIN -> {
            putBoolean(CAT_LIST_TYPE_KEY, false)
        }
    }
    apply()
}