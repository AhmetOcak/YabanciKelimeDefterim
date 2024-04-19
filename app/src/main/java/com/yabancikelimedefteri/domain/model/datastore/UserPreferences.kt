package com.yabancikelimedefteri.domain.model.datastore

data class UserPreferences(
    val isDarkTheme: Boolean,
    val isDynamicColor: Boolean,
    val isWordListTypeThin: Boolean,
    val colorScheme: String
)

enum class ColorSchemeKeys {
    NEON,
    NATURE,
    PINK,
    RED,
    SEA
}