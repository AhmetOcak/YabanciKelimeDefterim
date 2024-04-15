package com.yabancikelimedefteri.presentation.main

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.getCurrentOrientation
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.helpers.saveTheme
import com.yabancikelimedefteri.core.navigation.NavGraph
import com.yabancikelimedefteri.core.ui.theme.YabanciKelimeDefteriTheme
import dagger.hilt.android.AndroidEntryPoint

private const val THEME_KEY = "current_theme"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_YabanciKelimeDefteri)
        super.onCreate(savedInstanceState)

        val resources = resources

        sharedPreferences = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE)

        val currentTheme = sharedPreferences.getCurrentTheme()

        setContent {
            InitCurrentTheme(currentTheme)

            OrientationState.orientation.value = getCurrentOrientation()

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
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        sharedPreferences = sharedPreferences,
                        resources = resources
                    )
                }
            }
        }
    }

    @Composable
    private fun InitCurrentTheme(currentTheme: Int) {
        // MODE DAY -> 1
        // MODE NIGHT -> 2
        // Eğer geçerli bir tema bilgisi kaydedilmediyse bu temayı init eder
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
    }
}

object OrientationState {
    val orientation: MutableState<Int> = mutableStateOf(Configuration.ORIENTATION_LANDSCAPE)
}