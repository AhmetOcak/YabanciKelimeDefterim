package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object OrangeColorScheme : CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF8B4F24),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDCC7),
            onPrimaryContainer = Color(0xFF311300),
            secondary = Color(0xFF755846),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDCC7),
            onSecondaryContainer = Color(0xFF2B1709),
            tertiary = Color(0xFF606134),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFE6E6AD),
            onTertiaryContainer = Color(0xFF1C1D00),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFFF8F5),
            onBackground = Color(0xFF221A15),
            surface = Color(0xFFFFF8F5),
            onSurface = Color(0xFF221A15),
            surfaceVariant = Color(0xFFF4DED3),
            onSurfaceVariant = Color(0xFF52443C),
            outline = Color(0xFF84746A),
            outlineVariant = Color(0xFFD7C3B8),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF382E29),
            inverseOnSurface = Color(0xFFFFEDE5),
            inversePrimary = Color(0xFFFFB787)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(

            primary = Color(0xFFFFB787),
            onPrimary = Color(0xFF502400),
            primaryContainer = Color(0xFF6E390E),
            onPrimaryContainer = Color(0xFFFFDCC7),
            secondary = Color(0xFFE5BFA8),
            onSecondary = Color(0xFF422B1B),
            secondaryContainer = Color(0xFF5B4130),
            onSecondaryContainer = Color(0xFFFFDCC7),
            tertiary = Color(0xFFCACA93),
            onTertiary = Color(0xFF31320A),
            tertiaryContainer = Color(0xFF48491F),
            onTertiaryContainer = Color(0xFFE6E6AD),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF19120D),
            onBackground = Color(0xFFF0DFD7),
            surface = Color(0xFF19120D),
            onSurface = Color(0xFFF0DFD7),
            surfaceVariant = Color(0xFF52443C),
            onSurfaceVariant = Color(0xFFD7C3B8),
            outline = Color(0xFF9F8D83),
            outlineVariant = Color(0xFF52443C),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFF0DFD7),
            inverseOnSurface = Color(0xFF382E29),
            inversePrimary = Color(0xFF8B4F24)
        )
}