package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object YellowColorScheme : CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF6C5E10),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFF6E388),
            onPrimaryContainer = Color(0xFF211B00),
            secondary = Color(0xFF655E40),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFEDE3BC),
            onSecondaryContainer = Color(0xFF201C04),
            tertiary = Color(0xFF426650),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFC4ECCF),
            onTertiaryContainer = Color(0xFF002111),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFFF9EC),
            onBackground = Color(0xFF1E1C13),
            surface = Color(0xFFFFF9EC),
            onSurface = Color(0xFF1E1C13),
            surfaceVariant = Color(0xFFE9E2D0),
            onSurfaceVariant = Color(0xFF4A4739),
            outline = Color(0xFF7C7768),
            outlineVariant = Color(0xFFCDC6B4),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF333027),
            inverseOnSurface = Color(0xFFF7F0E2),
            inversePrimary = Color(0xFFD9C76F)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFFD9C76F),
            onPrimary = Color(0xFF393000),
            primaryContainer = Color(0xFF524700),
            onPrimaryContainer = Color(0xFFF6E388),
            secondary = Color(0xFFD0C7A2),
            onSecondary = Color(0xFF363016),
            secondaryContainer = Color(0xFF4D472B),
            onSecondaryContainer = Color(0xFFEDE3BC),
            tertiary = Color(0xFFA8D0B4),
            onTertiary = Color(0xFF133724),
            tertiaryContainer = Color(0xFF2B4E39),
            onTertiaryContainer = Color(0xFFC4ECCF),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF15130C),
            onBackground = Color(0xFFE8E2D4),
            surface = Color(0xFF15130C),
            onSurface = Color(0xFFE8E2D4),
            surfaceVariant = Color(0xFF4A4739),
            onSurfaceVariant = Color(0xFFCDC6B4),
            outline = Color(0xFF969080),
            outlineVariant = Color(0xFF4A4739),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFE8E2D4),
            inverseOnSurface = Color(0xFF333027),
            inversePrimary = Color(0xFF6C5E10)
        )
}