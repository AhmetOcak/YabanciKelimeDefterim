package com.yabancikelimedefteri.presentation.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.domain.usecase.word.GetAllWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchType {
    FOREIGN_WORD,
    MEANING_OF_WORD
}

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val getAllWordsUseCase: GetAllWordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> get() = _uiState.asStateFlow()

    private var wordsList: List<WordWithId> = listOf()

    init {
        getAllWords()
    }

    fun updateSearchField(newValue: String) {
        _uiState.update { it.copy(searchText = newValue) }
        searchWord()
    }

    fun onSearchClicked() {
        _uiState.update {
            it.copy(
                searchFieldError = _uiState.value.searchText.isBlank()
            )
        }
    }

    private fun getAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllWordsUseCase().collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        wordsList = response.data
                        _uiState.update {
                            it.copy(isError = false)
                        }
                    }

                    is Response.Error -> {
                        _uiState.update {
                            it.copy(isError = true)
                        }
                    }
                }
            }
        }
    }


    private fun searchWord() {
        if (_uiState.value.searchText.isNotBlank()) {
            _uiState.update { state ->
                state.copy(
                    searchResults = wordsList.filter { word ->
                        word.foreignWord.contains(_uiState.value.searchText.lowercase())
                            .also { result ->
                                if (result) {
                                    _uiState.update { state.copy(searchType = SearchType.FOREIGN_WORD) }
                                }
                            } || word.meaning.contains(_uiState.value.searchText.lowercase())
                            .also { result ->
                                if (result) {
                                    _uiState.update { state.copy(searchType = SearchType.MEANING_OF_WORD) }
                                }
                            }
                    },
                    isSearching = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isSearching = false,
                    searchResults = listOf()
                )
            }
        }
    }

    fun getWordMeaning(wordWithId: WordWithId) {
        val meaning = _uiState.value.searchResults.first {
            wordWithId.wordId == it.wordId
        }
        _uiState.update {
            it.copy(
                wordMeaning = when (_uiState.value.searchType) {
                    SearchType.FOREIGN_WORD -> {
                        meaning.meaning
                    }

                    SearchType.MEANING_OF_WORD -> {
                        meaning.foreignWord
                    }
                }
            )
        }
    }
}

data class DictionaryUiState(
    val searchText: String = "",
    val searchFieldError: Boolean = false,
    val searchResults: List<WordWithId> = listOf(),
    val isSearching: Boolean = false,
    val isError: Boolean = false,
    val searchType: SearchType = SearchType.FOREIGN_WORD,
    val wordMeaning: String? = null
)