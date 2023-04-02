package com.yabancikelimedefteri.presentation.add_word

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.common.saveWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _addWordState = MutableStateFlow<AddWordState>(AddWordState.Nothing)
    val addWordState = _addWordState.asStateFlow()

    var foreignWord by mutableStateOf("")
        private set
    var meaning by mutableStateOf("")
        private set

    var foreignWordFieldError by mutableStateOf(false)
        private set
    var meaningFieldError by mutableStateOf(false)
        private set

    fun updateForeignWord(newValue: String) { foreignWord = newValue }

    fun updateMeaning(newValue: String) { meaning = newValue }

    fun addForeignWord() = viewModelScope.launch(Dispatchers.IO) {
        if (foreignWord.isNotBlank() && meaning.isNotBlank()) {
            _addWordState.value = AddWordState.Loading
            try {
                _addWordState.value = AddWordState.Success(
                    sharedPreferences.edit {
                        saveWord(foreignWord.lowercase(), meaning.lowercase())
                    }
                )

                resetTextFields()
            } catch (e: Exception) {
                _addWordState.value = AddWordState.Error(message = e.message ?: e.localizedMessage)
            }
        } else if (foreignWord.isBlank() && meaning.isBlank()) {
            foreignWordFieldError = true
            meaningFieldError = true
        } else if (foreignWord.isBlank()) {
            foreignWordFieldError = true
            meaningFieldError = false
        } else {
            foreignWordFieldError = false
            meaningFieldError = true
        }
    }

    fun resetAddWordState() { _addWordState.value = AddWordState.Nothing }

    private fun resetTextFields() {
        foreignWord = ""
        meaning = ""

        foreignWordFieldError = false
        meaningFieldError = false
    }
}