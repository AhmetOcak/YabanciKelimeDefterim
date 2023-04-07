package com.yabancikelimedefteri.presentation.word

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.Response
import com.yabancikelimedefteri.domain.usecase.word.DeleteWordUseCase
import com.yabancikelimedefteri.domain.usecase.word.GetWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val getWordsUseCase: GetWordsUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _getWordsState = MutableStateFlow<GetWordState>(GetWordState.Loading)
    val getWordState = _getWordsState.asStateFlow()

    private val _deleteWordState = MutableStateFlow<DeleteWordState>(DeleteWordState.Nothing)
    val deleteWordState = _deleteWordState.asStateFlow()

    var categoryId: Int? = null
        private set

    init {
        categoryId = savedStateHandle["categoryId"]
        categoryId?.let { getWords(it) }
    }

    fun getWords(categoryId: Int) = viewModelScope.launch(Dispatchers.IO) {
        getWordsUseCase(listOf(categoryId)).collect() {
            when (it) {
                is Response.Loading -> { }
                is Response.Success -> {
                    _getWordsState.value = GetWordState.Success(data = it.data)
                }
                is Response.Error -> {
                    _getWordsState.value = GetWordState.Error(message = it.message)
                }
            }
        }
    }

    fun deleteWord(wordId: Int) = viewModelScope.launch(Dispatchers.IO) {
        deleteWordUseCase(wordId).collect() {
            when (it) {
                is Response.Loading -> { }
                is Response.Success -> {
                    _deleteWordState.value = DeleteWordState.Success(data = it.data)
                }
                is Response.Error -> {
                    _deleteWordState.value = DeleteWordState.Error(message = it.message)
                }
            }
        }
    }

    fun resetDeleteWordState() { _deleteWordState.value = DeleteWordState.Nothing }

}