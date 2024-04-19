package com.yabancikelimedefteri.presentation.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.datastore.ColorSchemeKeys
import com.yabancikelimedefteri.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    fun updateDarkTheme(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateDarkTheme(value)
        }
    }

    fun updateDynamicColor(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateDynamicColor(value)
        }
    }

    fun updateWordListType(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateWordListType(value)
        }
    }

    fun updateColorScheme(colorScheme: ColorSchemeKeys) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateColorScheme(colorScheme)
        }
    }
}

enum class Settings(val nameId: Int, val icon: ImageVector) {
    DARK_THEME(R.string.dark_theme, Icons.Filled.DarkMode),
    COLOR_THEMES(R.string.color_themes, Icons.Filled.ColorLens),
    DYNAMIC_COLOR(R.string.dynamic_color, Icons.Filled.BrightnessAuto),
    LIST_TYPE(R.string.list_type, Icons.AutoMirrored.Filled.List),
    RATE_APP(R.string.rate_app, Icons.Filled.StarRate),
    SHARE_APP(R.string.share_app, Icons.Filled.Share)
}