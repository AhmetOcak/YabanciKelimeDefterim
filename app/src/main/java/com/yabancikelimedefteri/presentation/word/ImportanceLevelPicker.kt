package com.yabancikelimedefteri.presentation.word

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ImportanceLevelPicker(isSelected: Boolean, text: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(text = text)
    }
}