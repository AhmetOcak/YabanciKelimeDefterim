package com.yabancikelimedefteri.domain.utils

import androidx.compose.ui.graphics.Color
import com.yabancikelimedefteri.domain.model.ImportanceLevel

fun Int.setImportanceLevel(): Color {
    return when (this) {
        ImportanceLevel.Green.ordinal -> Color.Green
        ImportanceLevel.Yellow.ordinal -> Color.Yellow
        ImportanceLevel.Red.ordinal -> Color.Red
        else -> Color.Green
    }
}

fun Int.convertImportanceLevel(): ImportanceLevel {
    return when(this) {
        ImportanceLevel.Green.ordinal -> ImportanceLevel.Green
        ImportanceLevel.Yellow.ordinal -> ImportanceLevel.Yellow
        ImportanceLevel.Red.ordinal -> ImportanceLevel.Red
        else -> ImportanceLevel.Green
    }
}