package com.yabancikelimedefteri.presentation.add_word

sealed interface CreateWordState {
    object Nothing : CreateWordState
    object Loading : CreateWordState
    data class Success(val data: Unit) : CreateWordState
    data class Error(val message: String) : CreateWordState
}