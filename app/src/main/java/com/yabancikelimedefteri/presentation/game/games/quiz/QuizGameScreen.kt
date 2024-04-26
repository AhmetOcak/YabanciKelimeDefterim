package com.yabancikelimedefteri.presentation.game.games.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
fun QuizGameScreen(
    upPress: () -> Unit,
    viewModel: QuizGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.quizGameUiState.collectAsStateWithLifecycle()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current, uiState.errorMessages.first().asString(), Toast.LENGTH_LONG
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
        upPress = upPress,
        onFinishGameClicked = viewModel::setGameIsOver,
        topBarTitle = stringResource(id = R.string.quiz_game),
        showFinishGameButton = viewModel.showFinishGameBtn
    ) { paddingValues ->
        if (uiState.words.size < 5) {
            MinWordWarning()
        } else {
            QuizGame(
                modifier = Modifier.padding(paddingValues),
                question = viewModel.question,
                options = viewModel.getOptions(),
                onOptionClick = viewModel::handleOptionClick,
                correctAnswer = viewModel.correctAnswer,
                selectedOptionIndex = viewModel.selectedOptionIndex
            )
        }
    }
}

@Composable
private fun QuizGame(
    modifier: Modifier,
    question: String,
    options: List<String>,
    onOptionClick: (Int, String) -> Unit,
    correctAnswer: String,
    selectedOptionIndex: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameWordItem(word = question)
        Spacer(modifier = Modifier.height(64.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            options.forEachIndexed { index, option ->
                OptionItem(
                    index = index,
                    word = option,
                    onClick = onOptionClick,
                    isCorrectOption = option == correctAnswer,
                    isSelectedOption = index == selectedOptionIndex
                )
            }
        }
    }
}

@Composable
private fun OptionItem(
    index: Int,
    word: String,
    onClick: (Int, String) -> Unit,
    isCorrectOption: Boolean,
    isSelectedOption: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(index, word) },
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrectOption && isSelectedOption) successGreen
            else if (!isCorrectOption && isSelectedOption) MaterialTheme.colorScheme.error
            else if (isCorrectOption) successGreen
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = word,
            textAlign = TextAlign.Center
        )
    }
}