package com.yabancikelimedefteri.presentation.add_word

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.Word
import com.yabancikelimedefteri.domain.usecase.word.CreateWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val createWordUseCase: CreateWordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _addWordState = MutableStateFlow<CreateWordState>(CreateWordState.Nothing)
    val addWordState = _addWordState.asStateFlow()

    private var categoryId: String? = null

    init {
        categoryId = savedStateHandle["categoryId"]
    }

    var foreignWord by mutableStateOf("")
        private set
    var meaning by mutableStateOf("")
        private set

    var foreignWordFieldError by mutableStateOf(false)
        private set
    var meaningFieldError by mutableStateOf(false)
        private set

    fun updateForeignWord(newValue: String) {
        foreignWord = newValue
    }

    fun updateMeaning(newValue: String) {
        meaning = newValue
    }

    fun addForeignWord() = viewModelScope.launch(Dispatchers.IO) {
        if (foreignWord.isNotBlank() && meaning.isNotBlank()) {
            categoryId?.let {
                createWordUseCase(
                    word = Word(
                        categoryId = it.toInt(),
                        foreignWord = foreignWord,
                        meaning = meaning
                    )
                ).collect() { response ->
                    when (response) {
                        is Response.Loading -> {
                            _addWordState.value = CreateWordState.Loading
                        }
                        is Response.Success -> {
                            _addWordState.value = CreateWordState.Success(data = response.data)
                            resetTextFields()
                        }
                        is Response.Error -> {
                            _addWordState.value = CreateWordState.Error(message = response.message)
                        }
                    }
                }
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

    fun resetAddWordState() {
        _addWordState.value = CreateWordState.Nothing
    }

    private fun resetTextFields() {
        foreignWord = ""
        meaning = ""

        foreignWordFieldError = false
        meaningFieldError = false
    }
}