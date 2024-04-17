package com.yabancikelimedefteri.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.domain.usecase.word.ObserveAllWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val observeAllWordsUseCase: ObserveAllWordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    init {
        observeAllWords()
    }

    val games: List<Game> = listOf(
        Game.QUIZ,
        Game.PAIR,
        Game.WRITING
    )

    private fun observeAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                observeAllWordsUseCase().collect { words ->
                    _uiState.update {
                        it.copy(isGamesCanPlay = words.size >= 5)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    fun consumedErrorMessages() {
        _uiState.update {
            it.copy(errorMessages = emptyList())
        }
    }
}

data class GamesUiState(
    val isGamesCanPlay: Boolean = false,
    val errorMessages: List<UiText> = emptyList()
)

enum class Game(val gameName: UiText, val gameImage: Int, val gameType: GameType) {
    QUIZ(
        gameName = UiText.StringResource(R.string.word_quiz),
        gameImage = R.drawable.quiz,
        gameType = GameType.QUIZ
    ),
    WRITING(
        gameName = UiText.StringResource(R.string.word_writing),
        gameImage = R.drawable.writing,
        gameType = GameType.WRITING
    ),
    PAIR(
        gameName = UiText.StringResource(R.string.word_pairing),
        gameImage = R.drawable.puzzle,
        gameType = GameType.PAIR
    )
}