package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.GameResultEmote
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.domain.model.CategoryWithId

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
    gameContent: @Composable (() -> Unit)
) {
    when (gameStatus) {
        GameStatus.PREPARATION -> {
            ChooseWordCategorySection(
                modifier = Modifier.padding(scaffoldPadding),
                categories = wordCategories,
                isGameReadyToLaunch = isGameReadyToLaunch,
                launchTheGame = launchTheGame,
                onCategoryClick = handleCategoryClick
            )
        }

        GameStatus.STARTED -> {
            gameContent()
        }

        GameStatus.END -> {
            GameResultTableSection(
                modifier = Modifier.padding(scaffoldPadding),
                correctAnswerCount = correctAnswerCount,
                wrongAnswerCount = wrongAnswerCount,
                successRate = successRate,
                userAnswers = userAnswers,
                onReturnGamesScreenClick = onReturnGamesScreenClick,
                quizResultEmote = gameResultEmote
            )
        }
    }
}