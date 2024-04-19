package com.yabancikelimedefteri.presentation.game.games.pairing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.yabancikelimedefteri.core.helpers.BaseGameViewModel
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.core.helpers.GameUiState
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
import javax.inject.Inject
import kotlin.math.floor

const val WORD_COUNT = 5

@HiltViewModel
class PairingGameViewModel @Inject constructor(
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val getSpecificWordsUseCase: GetSpecificWordsUseCase,
) : BaseGameViewModel(observeCategoriesUseCase) {

    val pairingGameUiState: StateFlow<GameUiState> = super.uiState.asStateFlow()

    private val questions = mutableListOf<String>()

    private var askedWordCount: Int = 0

    var pairStatus by mutableStateOf(PairStatus.NOTHING)
        private set

    // Selected Word, Selected Index
    var selectedPuzzle1 by mutableStateOf(Pair("", -1))
        private set
    var selectedPuzzle2 by mutableStateOf(Pair("", -1))
        private set

    // For hide correct puzzles
    private val correctPuzzles = mutableListOf<Int>()

    var isPuzzlesEnabled by mutableStateOf(true)
        private set

    fun handlePuzzleClick(word: String, index: Int) {
        if (selectedPuzzle1.first.isBlank()) {
            selectedPuzzle1 = Pair(word, index)
            pairStatus = PairStatus.SELECTED
        } else {
            selectedPuzzle2 = Pair(word, index)
        }

        if (selectedPuzzle1.first.isNotBlank() && selectedPuzzle2.first.isNotBlank()) {
            checkSelectedWords()
        }
    }

    override fun launchTheGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val wordList = mutableListOf<WordWithId>()

            uiState.value.selectedCategories.forEach { categoryId ->
                wordList.addAll(getSpecificWordsUseCase(categoryId))
            }

            val shuffledWordList = wordList.shuffled()

            shuffledWordList.take(WORD_COUNT).forEach {
                questions.apply {
                    add(it.foreignWord)
                    add(it.meaning)
                }
            }

            shuffleQuestionList()

            askedWordCount = WORD_COUNT

            uiState.update {
                it.copy(
                    words = shuffledWordList,
                    gameStatus = GameStatus.STARTED
                )
            }
        }
    }

    private fun checkSelectedWords() {
        val words = uiState.value.words

        val word = words.find {
            if (it.foreignWord == selectedPuzzle1.first && it.meaning == selectedPuzzle2.first) {
                correctPuzzles.apply {
                    add(selectedPuzzle1.second)
                    add(selectedPuzzle2.second)
                }
                true
            } else if (it.foreignWord == selectedPuzzle2.first && it.meaning == selectedPuzzle1.first) {
                correctPuzzles.apply {
                    add(selectedPuzzle1.second)
                    add(selectedPuzzle2.second)
                }
                true
            } else {
                false
            }
        }

        viewModelScope.launch {
            if (word != null) {
                correctAnswerCount++

                pairStatus = PairStatus.TRUE
                isPuzzlesEnabled = false
                delay(500)
                pairStatus = PairStatus.NOTHING
                selectedPuzzle1 = Pair("", -1)
                selectedPuzzle2 = Pair("", -1)
                isPuzzlesEnabled = true
            } else {
                pairStatus = PairStatus.FALSE
                isPuzzlesEnabled = false
                delay(500)
                pairStatus = PairStatus.NOTHING
                selectedPuzzle1 = Pair("", -1)
                selectedPuzzle2 = Pair("", -1)
                isPuzzlesEnabled = true
            }

            if (correctAnswerCount != 0 && correctAnswerCount % WORD_COUNT == 0) {
                playTheGame()
            }
        }
    }

    override fun playTheGame() {
        val words = uiState.value.words

        questions.clear()
        correctPuzzles.clear()

        if (askedWordCount + WORD_COUNT <= floor(words.size.toDouble() / 5) * 5) {
            val starterIndex = askedWordCount

            words.subList(starterIndex, askedWordCount + WORD_COUNT).forEach {
                questions.apply {
                    add(it.foreignWord)
                    add(it.meaning)
                }
            }

            askedWordCount += WORD_COUNT
            shuffleQuestionList()
        } else {
            setGameIsOver()
        }
    }

    private fun shuffleQuestionList() {
        val shuffledQuestions = questions.shuffled()
        questions.apply {
            clear()
            addAll(shuffledQuestions)
        }
    }

    fun getQuestions() = questions
    fun getCorrectPuzzles() = correctPuzzles
}

enum class PairStatus {
    NOTHING,
    SELECTED,
    TRUE,
    FALSE
}