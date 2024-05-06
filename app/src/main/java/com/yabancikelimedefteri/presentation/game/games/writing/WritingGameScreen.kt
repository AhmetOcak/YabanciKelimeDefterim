package com.yabancikelimedefteri.presentation.game.games.writing

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.theme.successGreen
import com.yabancikelimedefteri.presentation.game.games.components.GameScreenSkeleton
import com.yabancikelimedefteri.presentation.game.games.components.MinWordWarning
import com.yabancikelimedefteri.presentation.game.models.GameWordItem

@Composable
fun WritingGameScreen(
    upPress: () -> Unit,
    viewModel: WritingGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.writingGameUiState.collectAsStateWithLifecycle()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    GameScreenSkeleton(
        gameStatus = uiState.gameStatus,
        wordCategories = uiState.categories,
        isGameReadyToLaunch = uiState.isGameReadyToLaunch,
        launchTheGame = viewModel::launchTheGame,
        handleCategoryClick = viewModel::handleCategoryClick,
        correctAnswerCount = viewModel.correctAnswerCount,
        wrongAnswerCount = viewModel.wrongAnswerCount,
        successRate = viewModel.successRate,
        userAnswers = viewModel.userAnswers,
        onReturnGamesScreenClick = upPress,
        gameResultEmote = uiState.gameResultEmote,
        isCategorySelected = viewModel::isCategorySelected,
        onFinishGameClicked = viewModel::handleFinishTheGameClick,
        upPress = upPress,
        topBarTitle = stringResource(id = R.string.word_writing),
        showFinishGameButton = viewModel.showFinishGameBtn
    ) { paddingValues ->
        if (uiState.words.size < 5) {
            MinWordWarning()
        } else {
            WritingGame(
                modifier = Modifier.padding(paddingValues),
                question = viewModel.question,
                answerValue = viewModel.answerValue,
                onAnswerValueChange = viewModel::updateAnswerValue,
                onSubmitClick = viewModel::playTheGame,
                correctAnswer = viewModel.correctAnswer,
                showCorrectAnswer = viewModel.showCorrectAnswer
            )
        }
    }
}

@Composable
private fun WritingGame(
    modifier: Modifier,
    question: String,
    answerValue: String,
    onAnswerValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    correctAnswer: String,
    showCorrectAnswer: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.writing_game_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            GameWordItem(word = question)
            AnimatedVisibility(
                visible = showCorrectAnswer,
                exit = slideOutVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        color = if (answerValue.lowercase().trim() == correctAnswer.lowercase().trim()) successGreen
                        else MaterialTheme.colorScheme.error,
                        thickness = 2.dp
                    )
                    Text(text = correctAnswer, textAlign = TextAlign.Center)
                }
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = answerValue,
                    onValueChange = onAnswerValueChange,
                    maxLines = 1,
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = if (answerValue.isNotBlank()) {
                            { onSubmitClick() }
                        } else null
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSubmitClick,
                    enabled = answerValue.isNotBlank()
                ) {
                    Text(text = stringResource(id = R.string.submit_answer))
                }
            }
        }
    }
}