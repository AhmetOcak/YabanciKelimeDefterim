package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

object SeaColorScheme : CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF236488),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFC7E7FF),
            onPrimaryContainer = Color(0xFF001E2E),
            secondary = Color(0xFF4F616E),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFD2E5F5),
            onSecondaryContainer = Color(0xFF0B1D29),
            tertiary = Color(0xFF63597C),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFE9DDFF),
            onTertiaryContainer = Color(0xFF1F1635),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFF6FAFE),
            onBackground = Color(0xFF181C20),
            surface = Color(0xFFF6FAFE),
            onSurface = Color(0xFF181C20),
            surfaceVariant = Color(0xFFDDE3EA),
            onSurfaceVariant = Color(0xFF41484D),
            outline = Color(0xFF71787E),
            outlineVariant = Color(0xFFC1C7CE),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF2D3135),
            inverseOnSurface = Color(0xFFEEF1F6),
            inversePrimary = Color(0xFF93CDF6)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFF93CDF6),
            onPrimary = Color(0xFF00344C),
            primaryContainer = Color(0xFF004C6D),
            onPrimaryContainer = Color(0xFFC7E7FF),
            secondary = Color(0xFFB6C9D8),
            onSecondary = Color(0xFF21323E),
            secondaryContainer = Color(0xFF384955),
            onSecondaryContainer = Color(0xFFD2E5F5),
            tertiary = Color(0xFFCDC0E9),
            onTertiary = Color(0xFF342B4B),
            tertiaryContainer = Color(0xFF4B4263),
            onTertiaryContainer = Color(0xFFE9DDFF),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF101417),
            onBackground = Color(0xFFDFE3E7),
            surface = Color(0xFF101417),
            onSurface = Color(0xFFDFE3E7),
            surfaceVariant = Color(0xFF41484D),
            onSurfaceVariant = Color(0xFFC1C7CE),
            outline = Color(0xFF8B9198),
            outlineVariant = Color(0xFF41484D),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFDFE3E7),
            inverseOnSurface = Color(0xFF2D3135),
            inversePrimary = Color(0xFF236488)
        )

}