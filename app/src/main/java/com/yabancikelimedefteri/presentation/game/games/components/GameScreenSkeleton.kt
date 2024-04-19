package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.GameResultEmote
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.domain.model.word.CategoryWithId

@Composable
fun GameScreenSkeleton(
    gameStatus: GameStatus,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
    wordCategories: List<CategoryWithId>,
    isGameReadyToLaunch: Boolean,
    launchTheGame: () -> Unit,
    handleCategoryClick: (Int) -> Unit,
    correctAnswerCount: Int,
    wrongAnswerCount: Int,
    successRate: String,
    userAnswers: List<Answer>,
    onReturnGamesScreenClick: () -> Unit,
    gameResultEmote: GameResultEmote?,
    isCategorySelected: (Int) -> Boolean,
    gameEndContent: @Composable (() -> Unit)? = null,
    gameStartContent: @Composable (() -> Unit)? = null,
    gameContent: @Composable (() -> Unit)
) {
    when (gameStatus) {
        GameStatus.PREPARATION -> {
            if (gameStartContent == null) {
                ChooseWordCategorySection(
                    modifier = Modifier.padding(scaffoldPadding),
                    categories = wordCategories,
                    isGameReadyToLaunch = isGameReadyToLaunch,
                    launchTheGame = launchTheGame,
                    onCategoryClick = handleCategoryClick,
                    isCategorySelected = isCategorySelected
                )
            } else {
                gameStartContent()
            }
        }

        GameStatus.STARTED -> {
            gameContent()
        }

        GameStatus.END -> {
            if (gameEndContent == null) {
                GameResultTableSection(
                    modifier = Modifier.padding(scaffoldPadding),
                    correctAnswerCount = correctAnswerCount,
                    wrongAnswerCount = wrongAnswerCount,
                    successRate = successRate,
                    userAnswers = userAnswers,
                    onReturnGamesScreenClick = onReturnGamesScreenClick,
                    quizResultEmote = gameResultEmote
                )
            } else {
                gameEndContent()
            }
        }
    }
}