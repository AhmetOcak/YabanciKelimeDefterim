package com.yabancikelimedefteri.presentation.game

import com.yabancikelimedefteri.domain.model.WordWithId

sealed interface GameState {
    object Loading : GameState
    data class Success(val data: List<WordWithId>) : GameState
    data class Error(val message: String) : GameState
}