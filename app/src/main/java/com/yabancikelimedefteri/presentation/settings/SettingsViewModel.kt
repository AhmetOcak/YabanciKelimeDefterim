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
import com.yabancikelimedefteri.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {
}

enum class Settings(val nameId: Int, val icon: ImageVector) {
    DARK_THEME(R.string.dark_theme, Icons.Filled.DarkMode),
    COLOR_THEMES(R.string.color_themes, Icons.Filled.ColorLens),
    DYNAMIC_COLOR(R.string.dynamic_color, Icons.Filled.BrightnessAuto),
    LIST_TYPE(R.string.list_type, Icons.AutoMirrored.Filled.List),
    RATE_APP(R.string.rate_app, Icons.Filled.StarRate),
    SHARE_APP(R.string.share_app, Icons.Filled.Share)
}