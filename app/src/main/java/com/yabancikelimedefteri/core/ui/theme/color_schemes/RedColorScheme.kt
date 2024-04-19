package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object RedColorScheme: CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF8E4955),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFD9DD),
            onPrimaryContainer = Color(0xFF3B0715),
            secondary = Color(0xFF76565A),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFD9DD),
            onSecondaryContainer = Color(0xFF2C1519),
            tertiary = Color(0xFF795831),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFDDB9),
            onTertiaryContainer = Color(0xFF2B1700),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFFF8F7),
            onBackground = Color(0xFF22191A),
            surface = Color(0xFFFFF8F7),
            onSurface = Color(0xFF22191A),
            surfaceVariant = Color(0xFFF3DDDF),
            onSurfaceVariant = Color(0xFF524345),
            outline = Color(0xFF847374),
            outlineVariant = Color(0xFFD7C1C3),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF382E2F),
            inverseOnSurface = Color(0xFFFEEDEE),
            inversePrimary = Color(0xFFFFB2BC)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFFFFB2BC),
            onPrimary = Color(0xFF561D29),
            primaryContainer = Color(0xFF72333E),
            onPrimaryContainer = Color(0xFFFFD9DD),
            secondary = Color(0xFFE5BDC1),
            onSecondary = Color(0xFF43292D),
            secondaryContainer = Color(0xFF5C3F43),
            onSecondaryContainer = Color(0xFFFFD9DD),
            tertiary = Color(0xFFEABF8F),
            onTertiary = Color(0xFF452B07),
            tertiaryContainer = Color(0xFF5E411C),
            onTertiaryContainer = Color(0xFFFFDDB9),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF1A1112),
            onBackground = Color(0xFFF0DEDF),
            surface = Color(0xFF1A1112),
            onSurface = Color(0xFFF0DEDF),
            surfaceVariant = Color(0xFF524345),
            onSurfaceVariant = Color(0xFFD7C1C3),
            outline = Color(0xFF9F8C8E),
            outlineVariant = Color(0xFF524345),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFF0DEDF),
            inverseOnSurface = Color(0xFF382E2F),
            inversePrimary = Color(0xFF8E4955)
        )
}