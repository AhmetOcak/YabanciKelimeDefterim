package com.yabancikelimedefteri.presentation.game.games.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.BaseGameViewModel
import com.yabancikelimedefteri.core.helpers.GameResultEmote
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.core.helpers.GameUiState
import com.yabancikelimedefteri.core.helpers.UiText
import com.yabancikelimedefteri.domain.model.word.WordWithId
import com.yabancikelimedefteri.domain.usecase.category.ObserveCategoriesUseCase
import com.yabancikelimedefteri.domain.usecase.word.GetSpecificWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class QuizGameViewModel @Inject constructor(
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getSpecificWordsUseCase: GetSpecificWordsUseCase,
) : BaseGameViewModel(observeCategoriesUseCase) {

    val quizGameUiState: StateFlow<GameUiState> = super.uiState.asStateFlow()

    var question by mutableStateOf("")
        private set

    var showFinishGameBtn by mutableStateOf(false)
        private set

    private var options = mutableStateListOf<String>()

    private val allOptions = mutableListOf<String>()

    private var index = 0

    var correctAnswer by mutableStateOf("")
        private set
    var selectedOptionIndex by mutableIntStateOf(-1)
        private set

    fun handleOptionClick(index: Int, word: String) {
        val words = uiState.value.words
        correctAnswer = words.find { it.meaning == question }!!.foreignWord

        selectedOptionIndex = index

        if (words.find { it.meaning == question && it.foreignWord == word } == null) {
            wrongAnswerCount++
        } else {
            correctAnswerCount++
        }

        userAnswers.add(
            Answer(
                question = question,
                userAnswer = word,
                correctAnswer = correctAnswer
            )
        )

        showFinishGameBtn = true
        playTheGame()
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
                    uiState.value.words.forEach { word ->
                        allOptions.add(word.foreignWord)
                    }

                    val shuffledWordList = allOptions.shuffled()
                    allOptions.apply {
                        clear()
                        addAll(shuffledWordList)
                    }

                    question = uiState.value.words[index].meaning
                    createOptions(true)
                }
            } catch (e: Exception) {
                uiState.update {
                    it.copy(errorMessages = listOf(UiText.StringResource(R.string.error)))
                }
            }
        }
    }

    override fun playTheGame() {
        if (allOptions.size >= 3) {
            createOptions(false)
        } else {
            viewModelScope.launch {
                delay(500)
                calculateResult()
            }
        }
    }

    override fun calculateResult() {
        val correctRate = (correctAnswerCount.toDouble() / index) * 100
        successRate = "%${DecimalFormat("#.##").format(correctRate)}"

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

    fun getOptions() = options

    private fun createOptions(isItInitial: Boolean) {
        viewModelScope.launch {
            if (!isItInitial) {
                delay(500)
            }

            question = uiState.value.words[index].meaning

            options.clear()

            val wordList = uiState.value.words
            val correctAnswer = wordList.find { it.meaning == question }!!.foreignWord
            options.add(correctAnswer)
            allOptions.remove(correctAnswer)

            while (options.size != 3) {
                val randOpt = allOptions.random()
                if (!options.contains(randOpt)) {
                    options.add(randOpt)
                }
            }
            selectedOptionIndex = -1
            this@QuizGameViewModel.correctAnswer = ""

            val shuffledOptions = options.shuffled()
            options.apply {
                clear()
                addAll(shuffledOptions)
            }

            index++
        }
    }
}