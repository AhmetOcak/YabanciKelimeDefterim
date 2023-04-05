package com.yabancikelimedefteri.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.usecase.word.GetAllWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getAllWordsUseCase: GetAllWordsUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Loading)
    val gameState = _gameState.asStateFlow()

    var guessWord by mutableStateOf("")
        private set

    var guessWordFieldError by mutableStateOf(false)
        private set

    var words: List<WordWithId>? = null
        private set

    var correctAnswerCount by mutableStateOf(0)
        private set
    var inCorrectAnswerCount by mutableStateOf(0)
        private set

    // Key = question, value = answer
    var answers: MutableMap<String, String> = mutableMapOf()
        private set

    var wordIndex by mutableStateOf(0)
        private set

    init {
        getAllWords()
    }

    fun updateGuessWord(newValue: String) { guessWord = newValue }

    fun incWordIndex() {
        if (isGameStillGoing()) {
            wordIndex++
        }
    }

    fun addAnswer(foreignWord: String) {
        if (isGameStillGoing()) {
            answers[foreignWord] = guessWord
        }
    }

    fun resetGuessWord() { guessWord = "" }

    fun isGuessWordReadyForSubmit(): Boolean {
        return if (guessWord.isBlank()) {
            guessWordFieldError = true
            false
        } else {
            guessWordFieldError = false
            true
        }
    }

    fun isGameStillGoing(): Boolean {
        return if (words.isNullOrEmpty()) {
            true
        } else wordIndex <= (words?.size ?: 0) - 1
    }

    private fun getAllWords() = viewModelScope.launch(Dispatchers.IO) {
        getAllWordsUseCase().collect() {
            when(it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    words = it.data
                    words?.let { words ->
                        _gameState.value = GameState.Success(data = words.shuffled())
                    }
                }
                is Response.Error -> {
                    _gameState.value = GameState.Error(message = it.message)
                }
            }
        }
    }

    fun calculateResult() {
        answers.keys.forEach {
            if (answers[it]?.uppercase() == words?.find { word -> word.foreignWord == it }?.meaning) {
                correctAnswerCount++
            } else {
                inCorrectAnswerCount++
            }
        }
    }

}