package com.yabancikelimedefteri.presentation.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import com.yabancikelimedefteri.core.common.getCurrentTheme
import com.yabancikelimedefteri.core.common.saveTheme
import com.yabancikelimedefteri.core.navigation.NavGraph
import com.yabancikelimedefteri.core.ui.theme.YabanciKelimeDefteriTheme
import dagger.hilt.android.AndroidEntryPoint

private const val THEME_KEY = "current_theme"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("current_theme", Context.MODE_PRIVATE)

        val currentTheme = sharedPreferences.getCurrentTheme()

        setContent {
            // MODE DAY -> 1
            // MODE NIGHT -> 2
            if (currentTheme == -1) {
                sharedPreferences.edit {
                    saveTheme(
                        when (isSystemInDarkTheme()) {
                            true -> AppCompatDelegate.MODE_NIGHT_YES
                            false -> AppCompatDelegate.MODE_NIGHT_NO
                        }
                    )
                }
            }

            YabanciKelimeDefteriTheme(
                darkTheme = when (sharedPreferences.getCurrentTheme()) {
                    AppCompatDelegate.MODE_NIGHT_YES -> true
                    AppCompatDelegate.MODE_NIGHT_NO -> false
                    else -> {
                        isSystemInDarkTheme()
                    }
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph(
                        activity = this,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}