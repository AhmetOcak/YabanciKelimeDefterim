package com.yabancikelimedefteri.presentation.home

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.getWords
import com.yabancikelimedefteri.core.helpers.removeWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    private val _deleteWordState = MutableStateFlow<DeleteWordState>(DeleteWordState.Nothing)
    val deleteWordState = _deleteWordState.asStateFlow()

    init {
        getAllWords()
    }

    private fun getAllWords() = viewModelScope.launch(Dispatchers.IO) {
        _homeState.value = HomeState.Loading
        try {
            _homeState.value = HomeState.Success(data = sharedPreferences.getWords())
            Log.d("SAVED DATA", sharedPreferences.getWords().toString())
        } catch (e: Exception) {
            _homeState.value = HomeState.Error(message = e.message ?: e.stackTraceToString())
        }
    }

    fun deleteForeignWord(foreignWord: String) = viewModelScope.launch(Dispatchers.IO) {
        _deleteWordState.value = DeleteWordState.Loading
        try {
            _deleteWordState.value =
                DeleteWordState.Success(
                    data = sharedPreferences.edit {
                        removeWord(foreignWord)
                    }
                )
            getAllWords()
        } catch (e: Exception) {
            _deleteWordState.value = DeleteWordState.Error(message = e.message ?: e.localizedMessage)
        }
    }

    fun resetDeleteWordState() { _deleteWordState.value = DeleteWordState.Nothing }
}