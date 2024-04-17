package com.yabancikelimedefteri.presentation.game.games.quiz

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.usecase.category.ObserveCategoriesUseCase
import com.yabancikelimedefteri.domain.usecase.word.GetSpecificWordsUseCase
import com.yabancikelimedefteri.presentation.game.GameStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizGameViewModel @Inject constructor(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getSpecificWordsUseCase: GetSpecificWordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    val userAnswers = mutableListOf<QuizResult>()

    var correctAnswerCount = 0
        private set
    var wrongAnswerCount = 0
        private set
    var successRate = ""
        private set


    private var wordIndex = 0

    var question by mutableStateOf("")
        private set

    var answerValue by mutableStateOf("")
        private set

    fun updateAnswerValue(value: String) {
        answerValue = value
    }

    var answerValueFieldError by mutableStateOf(false)
        private set

    fun handleSubmitClick() {
        val words = _uiState.value.words

        if (userAnswers.size < words.size) {
            if (answerValue.isNotBlank()) {
                userAnswers.add(
                    QuizResult(
                        question = words[wordIndex].foreignWord,
                        correctAnswer = words[wordIndex].meaning,
                        userAnswer = answerValue
                    )
                )

                // We are increasing the wordIndex. We need to check the wordIndex value.
                // Otherwise, a "NoSuchElementException: No value present" error may occur.
                wordIndex++

                if (wordIndex >= words.size) {
                    calculateQuizResult(words)
                } else {
                    question = words[wordIndex].foreignWord
                }

                answerValue = ""
                answerValueFieldError = false
            } else {
                answerValueFieldError = true
            }
        } else {
            // calculate result
            calculateQuizResult(words)
        }
    }

    private fun calculateQuizResult(words: List<WordWithId>) {
        userAnswers.forEach { result ->
            if (result.correctAnswer.lowercase() == result.userAnswer.lowercase()) {
                correctAnswerCount++
            } else {
                wrongAnswerCount++
            }
        }

        val correctRate = (correctAnswerCount.toDouble() / words.size) * 100
        successRate = "%$correctRate"

        _uiState.update {
            it.copy(
                gameResultEmote = when (correctRate.toInt()) {
                    in 0..20 -> QuizResultEmote.VERY_BAD
                    in 21..40 -> QuizResultEmote.BAD
                    in 41..60 -> QuizResultEmote.NORMAL
                    in 61..80 -> QuizResultEmote.GOOD
                    else -> QuizResultEmote.VERY_GOOD
                },
                gameStatus = GameStatus.END
            )
        }
    }

    fun handleCategoryClick(categoryId: Int) {
        val selectedCategories = _uiState.value.selectedCategories

        when (categoryId) {
            ALL_CATEGORY_ID -> {}

            else -> {
                if (selectedCategories.contains(categoryId)) {
                    val updatedCategoryList = selectedCategories.toMutableList()
                    updatedCategoryList.remove(categoryId)

                    _uiState.update {
                        it.copy(
                            selectedCategories = updatedCategoryList,
                            isGameReadyToLaunch = updatedCategoryList.isNotEmpty()
                        )
                    }
                } else {
                    val updatedCategoryList = selectedCategories.toMutableList()
                    updatedCategoryList.add(categoryId)

                    _uiState.update {
                        it.copy(
                            selectedCategories = updatedCategoryList,
                            isGameReadyToLaunch = true
                        )
                    }
                }
            }
        }
    }

    fun launchTheGame() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val wordList = mutableListOf<WordWithId>()

                _uiState.value.selectedCategories.forEach { categoryId ->
                    wordList.addAll(getSpecificWordsUseCase(categoryId))
                }

                _uiState.update {
                    it.copy(
                        words = wordList.shuffled(),
                        gameStatus = GameStatus.STARTED
                    )
                }

                // init question
                question = _uiState.value.words[0].foreignWord
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            observeCategoriesUseCase().collect { categories ->
                try {
                    _uiState.update {
                        it.copy(categories = categories)
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                    }
                }
            }
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = emptyList())
        }
    }
}

data class GameUiState(
    val categories: List<CategoryWithId> = emptyList(),
    val words: List<WordWithId> = emptyList(),
    val selectedCategories: List<Int> = emptyList(),
    val isGameReadyToLaunch: Boolean = false,
    val errorMessages: List<UiText> = emptyList(),
    val gameStatus: GameStatus = GameStatus.PREPARATION,
    val gameResultEmote: QuizResultEmote? = null
)

@Immutable
data class QuizResult(val question: String, val correctAnswer: String, val userAnswer: String)

@Immutable
enum class QuizResultEmote(val emote: String, val message: UiText) {
    VERY_BAD(emote = "😭", message = UiText.StringResource(R.string.quiz_result_very_bad)),
    BAD(emote = "😢", message = UiText.StringResource(R.string.quiz_result_bad)),
    NORMAL(emote = "😐", message = UiText.StringResource(R.string.quiz_result_normal)),
    GOOD(emote = "🙂", message = UiText.StringResource(R.string.quiz_result_good)),
    VERY_GOOD(emote = "😍", message = UiText.StringResource(R.string.quiz_result_very_bad))
}