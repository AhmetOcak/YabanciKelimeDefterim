package com.yabancikelimedefteri.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.ui.theme.color_schemes.CustomColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.NatureColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.NeonColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.PinkColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.RedColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.SeaColorScheme
import com.yabancikelimedefteri.domain.model.datastore.ColorSchemeKeys
import com.yabancikelimedefteri.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeUserPreferences()
    }

    private fun observeUserPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.userPreferencesFlow.collect { preferences ->
                _uiState.update {
                    it.copy(
                        isDarkTheme = preferences.isDarkTheme,
                        isDynamicColor = preferences.isDynamicColor,
                        isWordListTypeThin = preferences.isWordListTypeThin,
                        colorScheme = when (preferences.colorScheme) {
                            ColorSchemeKeys.NATURE.name -> NatureColorScheme
                            ColorSchemeKeys.NEON.name -> NeonColorScheme
                            ColorSchemeKeys.RED.name -> RedColorScheme
                            ColorSchemeKeys.SEA.name -> SeaColorScheme
                            else -> PinkColorScheme
                        }
                    )
                }
            }
        }
    }
}

data class MainUiState(
    val isDarkTheme: Boolean = false,
    val isDynamicColor: Boolean = false,
    val isWordListTypeThin: Boolean = false,
    val colorScheme: CustomColorScheme = PinkColorScheme
)