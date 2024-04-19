package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object NeonColorScheme : CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF68548E),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFEBDDFF),
            onPrimaryContainer = Color(0xFF230F46),
            secondary = Color(0xFF635B70),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFEADEF8),
            onSecondaryContainer = Color(0xFF1F182B),
            tertiary = Color(0xFF7F525D),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFD9E1),
            onTertiaryContainer = Color(0xFF32101B),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFEF7FF),
            onBackground = Color(0xFF1D1B20),
            surface = Color(0xFFFEF7FF),
            onSurface = Color(0xFF1D1B20),
            surfaceVariant = Color(0xFFE7E0EB),
            onSurfaceVariant = Color(0xFF49454E),
            outline = Color(0xFF7A757F),
            outlineVariant = Color(0xFFCBC4CF),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF322F35),
            inverseOnSurface = Color(0xFFF5EFF7),
            inversePrimary = Color(0xFFD3BCFD)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFFD3BCFD),
            onPrimary = Color(0xFF39265C),
            primaryContainer = Color(0xFF503D74),
            onPrimaryContainer = Color(0xFFEBDDFF),
            secondary = Color(0xFFCDC2DB),
            onSecondary = Color(0xFF342D40),
            secondaryContainer = Color(0xFF4B4358),
            onSecondaryContainer = Color(0xFFEADEF8),
            tertiary = Color(0xFFF1B7C5),
            onTertiary = Color(0xFF4A2530),
            tertiaryContainer = Color(0xFF643B46),
            onTertiaryContainer = Color(0xFFFFD9E1),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF151218),
            onBackground = Color(0xFFE7E0E8),
            surface = Color(0xFF151218),
            onSurface = Color(0xFFE7E0E8),
            surfaceVariant = Color(0xFF49454E),
            onSurfaceVariant = Color(0xFFCBC4CF),
            outline = Color(0xFF948F99),
            outlineVariant = Color(0xFF49454E),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFE7E0E8),
            inverseOnSurface = Color(0xFF322F35),
            inversePrimary = Color(0xFF68548E)
        )
}