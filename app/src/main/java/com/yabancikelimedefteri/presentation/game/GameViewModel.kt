package com.yabancikelimedefteri.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getSpecificWordsUseCase: GetSpecificWordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    var guessWord by mutableStateOf("")
        private set

    fun updateGuessWord(newValue: String) {
        guessWord = newValue
    }

    var guessWordFieldError by mutableStateOf(false)
        private set

    var wordIndex by mutableIntStateOf(0)
        private set

    fun handleGuessClick(foreignWord: String) {
        if (wordIndex < _uiState.value.words.size) {
            if (guessWord.isNotBlank()) {
                guessWord = ""
                wordIndex++
                guessWordFieldError = false
            } else {
                guessWordFieldError = true
            }
        } else {
            // calculate result
            _uiState.update {
                it.copy(gameStatus = GameStatus.END)
            }
        }
    }

    // TODO: BUG
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

    fun reLaunchTheGame() {
        _uiState.update {
            it.copy(gameStatus = GameStatus.PREPARATION)
        }
    }
}

data class GameUiState(
    val categories: List<CategoryWithId> = emptyList(),
    val words: List<WordWithId> = emptyList(),
    val selectedCategories: List<Int> = emptyList(),
    val isGameReadyToLaunch: Boolean = false,
    val isAllCategoriesSelected: Boolean = false,
    val errorMessages: List<UiText> = emptyList(),
    val gameStatus: GameStatus = GameStatus.PREPARATION
)

enum class GameStatus {
    PREPARATION,
    STARTED,
    END
}