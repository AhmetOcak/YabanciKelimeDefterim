package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object PinkColorScheme : CustomColorScheme {

    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF7E4E7C),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFD6F9),
            onPrimaryContainer = Color(0xFF320935),
            secondary = Color(0xFF6D586A),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFF6DBF0),
            onSecondaryContainer = Color(0xFF261625),
            tertiary = Color(0xFF825247),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFDAD2),
            onTertiaryContainer = Color(0xFF33110A),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFFF7FA),
            onBackground = Color(0xFF201A1E),
            surface = Color(0xFFFFF7FA),
            onSurface = Color(0xFF201A1E),
            surfaceVariant = Color(0xFFEDDEE8),
            onSurfaceVariant = Color(0xFF4D444B),
            outline = Color(0xFF7F747C),
            outlineVariant = Color(0xFFD0C3CC),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF352E33),
            inverseOnSurface = Color(0xFFFAEDF4),
            inversePrimary = Color(0xFFEEB4E9)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFFEEB4E9),
            onPrimary = Color(0xFF4A204B),
            primaryContainer = Color(0xFF643663),
            onPrimaryContainer = Color(0xFFFFD6F9),
            secondary = Color(0xFFD9BFD4),
            onSecondary = Color(0xFF3C2B3B),
            secondaryContainer = Color(0xFF544152),
            onSecondaryContainer = Color(0xFFF6DBF0),
            tertiary = Color(0xFFF6B8AA),
            onTertiary = Color(0xFF4C261D),
            tertiaryContainer = Color(0xFF663B31),
            onTertiaryContainer = Color(0xFFFFDAD2),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF171216),
            onBackground = Color(0xFFEBDFE6),
            surface = Color(0xFF171216),
            onSurface = Color(0xFFEBDFE6),
            surfaceVariant = Color(0xFF4D444B),
            onSurfaceVariant = Color(0xFFD0C3CC),
            outline = Color(0xFF998D96),
            outlineVariant = Color(0xFF4D444B),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFEBDFE6),
            inverseOnSurface = Color(0xFF352E33),
            inversePrimary = Color(0xFF7E4E7C)
        )
}