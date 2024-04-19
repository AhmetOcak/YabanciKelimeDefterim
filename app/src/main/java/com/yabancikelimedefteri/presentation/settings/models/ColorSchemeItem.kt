package com.yabancikelimedefteri.presentation.settings.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.ui.theme.color_schemes.CustomColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.NatureColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.NeonColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.PinkSchemeCustom
import com.yabancikelimedefteri.core.ui.theme.color_schemes.RedColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.SeaColorScheme

@Composable
fun ColorSchemesSettingItem(
    nameId: Int,
    icon: ImageVector,
    onClick: (CustomColorScheme) -> Unit,
    currentScheme: CustomColorScheme,
    isDarkTheme: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Text(text = stringResource(id = nameId))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                colorSchemes.forEach { scheme ->
                    ColorScheme(
                        onClick = onClick,
                        schemeType = scheme,
                        isCurrentScheme = currentScheme == scheme,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorScheme(
    isCurrentScheme: Boolean,
    onClick: (CustomColorScheme) -> Unit,
    schemeType: CustomColorScheme,
    isDarkTheme: Boolean
) {
    val scheme = if (isDarkTheme) schemeType.darkColorScheme else schemeType.lightColorScheme

    val brush = Brush.linearGradient(
        listOf(
            scheme.primary,
            scheme.secondary,
            scheme.onSurfaceVariant,
            scheme.background
        )
    )

    Card(
        modifier = Modifier.size(32.dp),
        onClick = { onClick(schemeType) },
        shape = CircleShape,
        border = if (isCurrentScheme) BorderStroke(2.dp, MaterialTheme.colorScheme.error) else null
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawCircle(brush)
            }
        )
    }
}

private val colorSchemes = listOf(
    NeonColorScheme,
    NatureColorScheme,
    PinkSchemeCustom,
    SeaColorScheme,
    RedColorScheme
)