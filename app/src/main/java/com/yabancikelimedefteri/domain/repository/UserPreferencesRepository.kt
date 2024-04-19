package com.yabancikelimedefteri.domain.repository

import com.yabancikelimedefteri.domain.model.datastore.ColorSchemeKeys
import com.yabancikelimedefteri.domain.model.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateDarkTheme(darkTheme: Boolean)

    suspend fun updateColorScheme(colorScheme: ColorSchemeKeys)

    suspend fun updateDynamicColor(dynamicColor: Boolean)

    suspend fun updateWordListType(wordListType: Boolean)
}