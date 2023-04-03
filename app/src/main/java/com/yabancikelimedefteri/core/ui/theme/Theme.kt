package com.yabancikelimedefteri.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = CustomOrange,
    secondary = CustomOrange,
    background = CustomBlack
)

private val LightColorPalette = lightColors(
    primary = CustomOrange,
    secondary = CustomOrange,
    surface = WhiteSmoke
)

@Composable
fun YabanciKelimeDefteriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    val isDark = ThemeState.isDark.value ?: darkTheme

    if (isDark) {
        systemUiController.setStatusBarColor(
            color = CustomDarkGray,
            darkIcons = false
        )
    } else {
        systemUiController.setStatusBarColor(
            color = CustomDarkOrange,
            darkIcons = false
        )
    }

    val colors = if (isDark) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

object ThemeState {
    var isDark: MutableState<Boolean?> = mutableStateOf(null)
}