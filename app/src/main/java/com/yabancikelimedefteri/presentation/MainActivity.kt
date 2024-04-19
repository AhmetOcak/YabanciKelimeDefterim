package com.yabancikelimedefteri.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.core.navigation.MyVocabularyApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition(condition = {
            !viewModel.isPreferencesReady
        })

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (viewModel.isPreferencesReady) {
                MyVocabularyApp(
                    isDarkThemeChecked = uiState.isDarkTheme,
                    isDynamicColorChecked = uiState.isDynamicColor,
                    isThinListTypeChecked = uiState.isWordListTypeThin,
                    currentScheme = uiState.colorScheme
                )
            }
        }
    }
}