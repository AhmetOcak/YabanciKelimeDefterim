package com.yabancikelimedefteri.presentation.add_word

sealed interface AddWordState {
    object Nothing : AddWordState
    object Loading : AddWordState
    data class Success(val data: Unit) : AddWordState
    data class Error(val message: String) : AddWordState
}