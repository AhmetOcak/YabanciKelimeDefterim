package com.yabancikelimedefteri.presentation.word

sealed interface DeleteWordState {
    object Nothing : DeleteWordState
    object Loading : DeleteWordState
    data class Success(val data: Unit) : DeleteWordState
    data class Error(val message: String) : DeleteWordState
}