package com.yabancikelimedefteri.presentation.game.games.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.presentation.game.games.components.GameScreenSkeleton
import com.yabancikelimedefteri.presentation.game.models.GameWordItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGameScreen(
    upPress: () -> Unit,
    viewModel: QuizGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.quizGameUiState.collectAsStateWithLifecycle()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.quiz_game))
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
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
            scaffoldPadding = paddingValues
        ) {
            QuizGame(
                modifier = Modifier.padding(paddingValues),
                question = viewModel.question,
                answerValue = viewModel.answerValue,
                onAnswerValueChanged = viewModel::updateAnswerValue,
                onSubmitClicked = viewModel::playTheGame
            )
        }
    }
}

@Composable
private fun QuizGame(
    modifier: Modifier,
    question: String,
    answerValue: String,
    onAnswerValueChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameWordItem(word = question)
        Spacer(modifier = modifier.height(32.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = answerValue,
            onValueChange = onAnswerValueChanged,
            label = {
                Text(text = stringResource(R.string.user_guess))
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmitClicked() }),
        )
        Spacer(modifier = modifier.height(32.dp))
        Button(onClick = onSubmitClicked, enabled = answerValue.isNotBlank()) {
            Text(text = stringResource(R.string.submit_answer))
        }
    }
}