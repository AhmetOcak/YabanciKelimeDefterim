package com.yabancikelimedefteri.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yabancikelimedefteri.domain.model.datastore.ColorSchemeKeys
import com.yabancikelimedefteri.domain.model.datastore.UserPreferences
import com.yabancikelimedefteri.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val userPreferencesFlow: Flow<UserPreferences> =
        dataStore.data.catch { exception ->
            throw exception
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val WORD_LIST_TYPE = booleanPreferencesKey("word_list_type")
        val COLOR_SCHEME = stringPreferencesKey("color_scheme")
    }

    override suspend fun updateDarkTheme(darkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = darkTheme
        }
    }

    override suspend fun updateColorScheme(colorScheme: ColorSchemeKeys) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COLOR_SCHEME] = colorScheme.name
        }
    }

    override suspend fun updateDynamicColor(dynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR] = dynamicColor
        }
    }

    override suspend fun updateWordListType(wordListType: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WORD_LIST_TYPE] = wordListType
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val isDarkTheme = preferences[PreferencesKeys.DARK_THEME] ?: false
        val isDynamicColor = preferences[PreferencesKeys.DYNAMIC_COLOR] ?: false
        val isWordListTypeThin = preferences[PreferencesKeys.WORD_LIST_TYPE] ?: false
        val colorScheme = preferences[PreferencesKeys.COLOR_SCHEME] ?: ColorSchemeKeys.PINK.name

        return UserPreferences(
            isDarkTheme = isDarkTheme,
            isDynamicColor = isDynamicColor,
            isWordListTypeThin = isWordListTypeThin,
            colorScheme = colorScheme
        )
    }
}