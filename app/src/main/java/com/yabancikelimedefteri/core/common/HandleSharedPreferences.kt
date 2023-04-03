package com.yabancikelimedefteri.core.common

import android.content.SharedPreferences

fun SharedPreferences.Editor.saveWord(foreignWord: String, meaning: String) {
    putString(foreignWord, meaning)
    apply()
}

fun SharedPreferences.getWords(): MutableMap<String, *>? {
    return all
}

fun SharedPreferences.Editor.removeWord(foreignWord: String) {
    remove(foreignWord)
    apply()
}

fun SharedPreferences.Editor.saveTheme(themeCode: Int) {
    putInt("current_theme", themeCode)
    apply()
}

fun SharedPreferences.getCurrentTheme(): Int {
    return getInt("current_theme", -1)
}