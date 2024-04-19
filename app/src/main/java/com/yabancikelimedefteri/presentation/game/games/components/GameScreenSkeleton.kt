package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.GameResultEmote
import com.yabancikelimedefteri.core.helpers.GameStatus
import com.yabancikelimedefteri.core.ui.component.BackButton
import com.yabancikelimedefteri.core.ui.component.FinishGameButton
import com.yabancikelimedefteri.domain.model.word.CategoryWithId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenSkeleton(
    gameStatus: GameStatus,
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
    upPress: () -> Unit,
    onFinishGameClicked: () -> Unit,
    topBarTitle: String,
    gameEndContent: @Composable ((paddingValues: PaddingValues) -> Unit)? = null,
    gameStartContent: @Composable ((paddingValues: PaddingValues) -> Unit)? = null,
    gameContent: @Composable ((paddingValues: PaddingValues) -> Unit)
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = topBarTitle)
                },
                navigationIcon = {
                    BackButton(onClick = upPress)
                },
                actions = {
                    if (gameStatus == GameStatus.STARTED) {
                        FinishGameButton(onClick = onFinishGameClicked)
                    }
                }
            )
        }
    ) { paddingValues ->
        when (gameStatus) {
            GameStatus.PREPARATION -> {
                if (gameStartContent == null) {
                    ChooseWordCategorySection(
                        modifier = Modifier.padding(paddingValues),
                        categories = wordCategories,
                        isGameReadyToLaunch = isGameReadyToLaunch,
                        launchTheGame = launchTheGame,
                        onCategoryClick = handleCategoryClick,
                        isCategorySelected = isCategorySelected
                    )
                } else {
                    gameStartContent(paddingValues)
                }
            }

            GameStatus.STARTED -> {
                gameContent(paddingValues)
            }

            GameStatus.END -> {
                if (gameEndContent == null) {
                    GameResultTableSection(
                        modifier = Modifier.padding(paddingValues),
                        correctAnswerCount = correctAnswerCount,
                        wrongAnswerCount = wrongAnswerCount,
                        successRate = successRate,
                        userAnswers = userAnswers,
                        onReturnGamesScreenClick = onReturnGamesScreenClick,
                        quizResultEmote = gameResultEmote
                    )
                } else {
                    gameEndContent(paddingValues)
                }
            }
        }
    }
}