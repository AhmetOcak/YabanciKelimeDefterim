package com.yabancikelimedefteri.core.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
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
        Text(text = buttonText.uppercase())
    }
}