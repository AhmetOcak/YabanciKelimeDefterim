package com.yabancikelimedefteri.presentation.game.games.writing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.BaseGameViewModel
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.core.helpers.GameUiState
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.domain.model.word.WordWithId
import com.yabancikelimedefteri.domain.usecase.category.ObserveCategoriesUseCase
import com.yabancikelimedefteri.domain.usecase.word.GetSpecificWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingGameViewModel @Inject constructor(
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getSpecificWordsUseCase: GetSpecificWordsUseCase,
): BaseGameViewModel(observeCategoriesUseCase) {

    val writingGameUiState: StateFlow<GameUiState> = super.uiState.asStateFlow()

    private var wordIndex = 0

    var answerValue by mutableStateOf("")
        private set
    var question by mutableStateOf("")
        private set

    fun updateAnswerValue(value: String) {
        answerValue = value
    }

    override fun launchTheGame() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val wordList = mutableListOf<WordWithId>()

                uiState.value.selectedCategories.forEach { categoryId ->
                    wordList.addAll(getSpecificWordsUseCase(categoryId))
                }

                uiState.update {
                    it.copy(
                        words = wordList.shuffled(),
                        gameStatus = GameStatus.STARTED
                    )
                }

                // Empty situation already handled
                if (uiState.value.words.isNotEmpty()) {
                    // init question
                    question = uiState.value.words[0].meaning
                }
            } catch (e: Exception) {
                uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    override fun playTheGame() {
        val words = uiState.value.words

        if (userAnswers.size < words.size) {
            if (answerValue.isNotBlank()) {
                userAnswers.add(
                    Answer(
                        question = words[wordIndex].meaning,
                        correctAnswer = words[wordIndex].foreignWord,
                        userAnswer = answerValue
                    )
                )

                // We are increasing the wordIndex. We need to check the wordIndex value.
                // Otherwise, a "NoSuchElementException: No value present" error may occur.
                wordIndex++

                if (wordIndex >= words.size) {
                    calculateResult(words)
                } else {
                    question = words[wordIndex].meaning
                }

                answerValue = ""
            }
        } else {
            // calculate result
            calculateResult(words)
        }
    }
}