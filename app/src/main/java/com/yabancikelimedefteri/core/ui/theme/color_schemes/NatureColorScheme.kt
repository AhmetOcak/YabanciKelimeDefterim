package com.yabancikelimedefteri.core.ui.theme.color_schemes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object NatureColorScheme : CustomColorScheme {
    override val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = Color(0xFF546524),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFD7EB9B),
            onPrimaryContainer = Color(0xFF161E00),
            secondary = Color(0xFF5B6146),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFE0E6C4),
            onSecondaryContainer = Color(0xFF181E09),
            tertiary = Color(0xFF3A665E),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFBCECE2),
            onTertiaryContainer = Color(0xFF00201C),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFBFAED),
            onBackground = Color(0xFF1B1C15),
            surface = Color(0xFFFBFAED),
            onSurface = Color(0xFF1B1C15),
            surfaceVariant = Color(0xFFE3E4D3),
            onSurfaceVariant = Color(0xFF46483C),
            outline = Color(0xFF76786B),
            outlineVariant = Color(0xFFC6C8B8),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF303129),
            inverseOnSurface = Color(0xFFF2F1E5),
            inversePrimary = Color(0xFFBBCF81)
        )

    override val darkColorScheme: ColorScheme
        get() = darkColorScheme(
            primary = Color(0xFFBBCF81),
            onPrimary = Color(0xFF283500),
            primaryContainer = Color(0xFF3D4C0D),
            onPrimaryContainer = Color(0xFFD7EB9B),
            secondary = Color(0xFFC3CAA9),
            onSecondary = Color(0xFF2D331C),
            secondaryContainer = Color(0xFF434930),
            onSecondaryContainer = Color(0xFFE0E6C4),
            tertiary = Color(0xFFA1D0C6),
            onTertiary = Color(0xFF033731),
            tertiaryContainer = Color(0xFF204E47),
            onTertiaryContainer = Color(0xFFBCECE2),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF13140D),
            onBackground = Color(0xFFE3E3D7),
            surface = Color(0xFF13140D),
            onSurface = Color(0xFFE3E3D7),
            surfaceVariant = Color(0xFF46483C),
            onSurfaceVariant = Color(0xFFC6C8B8),
            outline = Color(0xFF909284),
            outlineVariant = Color(0xFF46483C),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFE3E3D7),
            inverseOnSurface = Color(0xFF303129),
            inversePrimary = Color(0xFF546524)
        )
}