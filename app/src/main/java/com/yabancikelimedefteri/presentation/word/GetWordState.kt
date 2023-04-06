package com.yabancikelimedefteri.presentation.word

import com.yabancikelimedefteri.domain.model.WordWithId

sealed interface GetWordState {
    object Loading : GetWordState
    data class Success(val data: List<WordWithId>) : GetWordState
    data class Error(val message: String) : GetWordState
}