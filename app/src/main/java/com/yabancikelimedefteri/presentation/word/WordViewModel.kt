package com.yabancikelimedefteri.presentation.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.core.navigation.MainDestinations
import com.yabancikelimedefteri.core.ui.component.SortType
import com.yabancikelimedefteri.domain.model.word.Word
import com.yabancikelimedefteri.domain.model.word.WordWithId
import com.yabancikelimedefteri.domain.usecase.word.AddWordUseCase
import com.yabancikelimedefteri.domain.usecase.word.DeleteWordUseCase
import com.yabancikelimedefteri.domain.usecase.word.ObserveSpecificWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val observeSpecificWordsUseCase: ObserveSpecificWordsUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val addWordUseCase: AddWordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordsUiState())
    val uiState: StateFlow<WordsUiState> = _uiState.asStateFlow()

    var categoryId: String? = null
        private set

    init {
        categoryId = savedStateHandle[MainDestinations.WORD_ID_KEY]
        categoryId?.let { getWords(it.toInt()) }
    }

    var foreignWord by mutableStateOf("")
        private set
    var meaningWord by mutableStateOf("")
        private set

    var searchValue by mutableStateOf("")
        private set

    fun updateForeignWord(newValue: String) {
        foreignWord = newValue
    }

    fun updateMeaningWord(newValue: String) {
        meaningWord = newValue
    }

    fun updateSearchValueAndSearch(newValue: String) {
        searchValue = newValue

        val words = _uiState.value.words

        if (searchValue.isNotBlank()) {
            _uiState.update {
                it.copy(
                    uiEvent = UiEvent.SEARCHING,
                    searchResults = words.filter { word ->
                        word.foreignWord.contains(searchValue.lowercase())
                                || word.meaning.contains(searchValue.lowercase())
                    }
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    uiEvent = UiEvent.WORDS,
                    searchResults = emptyList()
                )
            }
        }
    }

    private fun getWords(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                observeSpecificWordsUseCase(categoryId).collect { wordList ->
                    _uiState.update {
                        it.copy(words = wordList, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessages = listOf(UiText.StringResource(R.string.error)),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun deleteWord(wordId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteWordUseCase(wordId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    fun addWord() {
        if (foreignWord.isNotBlank() && meaningWord.isNotBlank() && categoryId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    categoryId?.let { categoryId ->
                        addWordUseCase(
                            word = Word(
                                categoryId = categoryId.toInt(),
                                foreignWord = foreignWord,
                                meaning = meaningWord
                            )
                        )
                    } ?: {
                        _uiState.update {
                            it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                        }
                    }

                    foreignWord = ""
                    meaningWord = ""
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                    }
                }
            }
        }
    }

    fun clearAddWordVars() {
        foreignWord = ""
        meaningWord = ""
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = emptyList())
        }
    }

    // More sort types may be added in the future.
    fun handleOnMenuItemClick(sortType: SortType) {
        when (sortType) {
            SortType.ALPHABETICALLY -> {
                _uiState.update {
                    it.copy(
                        words = _uiState.value.words.sortedBy { word -> word.foreignWord }
                    )
                }
            }
        }
    }
}

data class WordsUiState(
    val isLoading: Boolean = true,
    val words: List<WordWithId> = emptyList(),
    val errorMessages: List<UiText> = emptyList(),
    val uiEvent: UiEvent = UiEvent.WORDS,
    val searchResults: List<WordWithId> = emptyList()
)

enum class UiEvent {
    SEARCHING,
    WORDS
}