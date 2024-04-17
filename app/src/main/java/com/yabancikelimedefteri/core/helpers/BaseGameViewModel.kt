package com.yabancikelimedefteri.core.helpers

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.usecase.category.ObserveCategoriesUseCase
import com.yabancikelimedefteri.presentation.game.games.components.ALL_CATEGORY_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseGameViewModel(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase
) : ViewModel() {

    protected val uiState = MutableStateFlow(GameUiState())

    init {
        observeCategories()
    }

    abstract val userAnswers: MutableList<Answer>

    abstract var correctAnswerCount: Int
        protected set
    abstract var wrongAnswerCount: Int
        protected set
    abstract var successRate: String
        protected set

    abstract fun launchTheGame()

    abstract fun playTheGame()

    protected fun calculateResult(words: List<WordWithId>) {
        userAnswers.forEach { result ->
            if (result.correctAnswer.lowercase() == result.userAnswer.lowercase()) {
                correctAnswerCount++
            } else {
                wrongAnswerCount++
            }
        }

        val correctRate = (correctAnswerCount.toDouble() / words.size) * 100
        successRate = "%$correctRate"

        uiState.update {
            it.copy(
                gameResultEmote = when (correctRate.toInt()) {
                    in 0..20 -> GameResultEmote.VERY_BAD
                    in 21..40 -> GameResultEmote.BAD
                    in 41..60 -> GameResultEmote.NORMAL
                    in 61..80 -> GameResultEmote.GOOD
                    else -> GameResultEmote.VERY_GOOD
                },
                gameStatus = GameStatus.END
            )
        }
    }

    fun handleCategoryClick(categoryId: Int) {
        val selectedCategories = uiState.value.selectedCategories

        when (categoryId) {
            ALL_CATEGORY_ID -> {}

            else -> {
                if (selectedCategories.contains(categoryId)) {
                    val updatedCategoryList = selectedCategories.toMutableList()
                    updatedCategoryList.remove(categoryId)

                    uiState.update {
                        it.copy(
                            selectedCategories = updatedCategoryList,
                            isGameReadyToLaunch = updatedCategoryList.isNotEmpty()
                        )
                    }
                } else {
                    val updatedCategoryList = selectedCategories.toMutableList()
                    updatedCategoryList.add(categoryId)

                    uiState.update {
                        it.copy(
                            selectedCategories = updatedCategoryList,
                            isGameReadyToLaunch = true
                        )
                    }
                }
            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            observeCategoriesUseCase().collect { categories ->
                try {
                    uiState.update {
                        it.copy(categories = categories)
                    }
                } catch (e: Exception) {
                    uiState.update {
                        it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                    }
                }
            }
        }
    }

    fun consumedErrorMessage() {
        uiState.update {
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
    val gameResultEmote: GameResultEmote? = null
)

@Immutable
enum class GameResultEmote(val emote: String, val message: UiText) {
    VERY_BAD(emote = "😭", message = UiText.StringResource(R.string.quiz_result_very_bad)),
    BAD(emote = "😢", message = UiText.StringResource(R.string.quiz_result_bad)),
    NORMAL(emote = "😐", message = UiText.StringResource(R.string.quiz_result_normal)),
    GOOD(emote = "🙂", message = UiText.StringResource(R.string.quiz_result_good)),
    VERY_GOOD(emote = "😍", message = UiText.StringResource(R.string.quiz_result_very_bad))
}

@Immutable
data class Answer(val question: String, val correctAnswer: String, val userAnswer: String)

enum class GameStatus {
    PREPARATION,
    STARTED,
    END
}