package com.yabancikelimedefteri.presentation.game.games.writing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WritingGameViewModel @Inject constructor(): ViewModel() {

    var answerValue by mutableStateOf("")
        private set

    fun updateAnswerValue(value: String) {
        answerValue = value
    }
}