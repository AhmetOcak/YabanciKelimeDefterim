package com.yabancikelimedefteri.core.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.ui.theme.CustomWhite

@Composable
fun CustomButton(
    modifier: Modifier,
    onClick: () -> Unit,
    buttonText: String,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20),
        contentPadding = PaddingValues(16.dp),
        enabled = enabled
    ) {
        Text(text = buttonText.uppercase(), color = CustomWhite)
    }
}