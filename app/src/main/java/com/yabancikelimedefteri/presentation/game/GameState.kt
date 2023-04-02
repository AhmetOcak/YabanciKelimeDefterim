package com.yabancikelimedefteri.presentation.game

sealed interface GameState {
    object Loading : GameState
    data class Success(val data: List<String>) : GameState
    data class Error(val message: String) : GameState
}