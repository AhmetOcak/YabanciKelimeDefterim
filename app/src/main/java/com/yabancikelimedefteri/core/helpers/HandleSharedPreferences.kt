package com.yabancikelimedefteri.core.helpers

import android.content.SharedPreferences
import com.yabancikelimedefteri.core.navigation.LIST_TYPE_KEY
import com.yabancikelimedefteri.core.navigation.ListType

fun SharedPreferences.Editor.saveTheme(themeCode: Int) {
    putInt("current_theme", themeCode)
    apply()
}

fun SharedPreferences.getCurrentTheme(): Int {
    return getInt("current_theme", -1)
}

fun SharedPreferences.getCurrentListType(): Boolean {
    return getBoolean(LIST_TYPE_KEY, true)
}

fun SharedPreferences.Editor.saveNewListType(listType: ListType) {
    when(listType) {
        ListType.RECTANGLE -> {
            putBoolean(LIST_TYPE_KEY, true)
        }
        ListType.THIN -> {
            putBoolean(LIST_TYPE_KEY, false)
        }
    }
    apply()
}