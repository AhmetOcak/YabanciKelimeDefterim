package com.yabancikelimedefteri.core.helpers

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.word.CategoryWithId
import com.yabancikelimedefteri.domain.model.word.WordWithId
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

    val userAnswers: MutableList<Answer> = mutableListOf()

    var correctAnswerCount: Int = 0
        protected set
    var wrongAnswerCount: Int = 0
        protected set
    var successRate: String = ""
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
        val selectedCategories = uiState.value.selectedCategories.toMutableList()
        val categories = uiState.value.categories

        when (categoryId) {
            ALL_CATEGORY_ID -> {
                // +1 coming from All Category Option
                if (selectedCategories.size < categories.size + 1) {
                    selectedCategories.apply {
                        clear()
                        addAll(categories.map { it.categoryId })
                        add(ALL_CATEGORY_ID)
                    }

                    uiState.update {
                        it.copy(isAllCategoriesOptionSelected = true)
                    }
                } else {
                    selectedCategories.clear()

                    uiState.update {
                        it.copy(isAllCategoriesOptionSelected = false)
                    }
                }
            }

            else -> {
                if (selectedCategories.contains(categoryId)) {
                    selectedCategories.remove(categoryId)
                } else {
                    selectedCategories.add(categoryId)
                }
            }
        }

        uiState.update {
            it.copy(
                selectedCategories = selectedCategories,
                isGameReadyToLaunch = selectedCategories.isNotEmpty()
            )
        }
    }

    fun isCategorySelected(categoryId: Int) = uiState.value.selectedCategories.contains(categoryId)

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

    fun setGameIsOver() {
        uiState.update {
            it.copy(gameStatus = GameStatus.END)
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
    val gameResultEmote: GameResultEmote? = null,
    val isAllCategoriesOptionSelected: Boolean = false
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