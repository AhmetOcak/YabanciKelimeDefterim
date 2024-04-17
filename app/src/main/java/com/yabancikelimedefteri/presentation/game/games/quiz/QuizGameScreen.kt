package com.yabancikelimedefteri.presentation.game.games.quiz

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.presentation.game.GameStatus
import com.yabancikelimedefteri.presentation.game.models.GameCategoryItem
import com.yabancikelimedefteri.presentation.game.models.GameWordItem

const val ALL_CATEGORY_ID = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGameScreen(
    upPress: () -> Unit,
    viewModel: QuizGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        when (uiState.gameStatus) {
            GameStatus.PREPARATION -> {
                ChooseWordCategorySection(
                    modifier = Modifier.padding(paddingValues),
                    categories = uiState.categories,
                    isGameReadyToLaunch = uiState.isGameReadyToLaunch,
                    launchTheGame = viewModel::launchTheGame,
                    onCategoryClick = viewModel::handleCategoryClick
                )
            }

            GameStatus.STARTED -> {
                QuizGame(
                    modifier = Modifier.padding(paddingValues),
                    question = viewModel.question,
                    answerValue = viewModel.answerValue,
                    onAnswerValueChanged = viewModel::updateAnswerValue,
                    isError = viewModel.answerValueFieldError,
                    onSubmitClicked = viewModel::handleSubmitClick
                )
            }

            GameStatus.END -> {
                GameResultSection(
                    modifier = Modifier.padding(paddingValues),
                    correctAnswerCount = viewModel.correctAnswerCount,
                    wrongAnswerCount = viewModel.wrongAnswerCount,
                    successRate = viewModel.successRate,
                    userAnswers = viewModel.userAnswers,
                    onReturnGamesScreenClick = upPress,
                    quizResultEmote = uiState.gameResultEmote
                )
            }
        }
    }
}

@Composable
private fun QuizGame(
    modifier: Modifier,
    question: String,
    answerValue: String,
    onAnswerValueChanged: (String) -> Unit,
    isError: Boolean,
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
                Text(
                    text = if (isError) stringResource(id = R.string.text_field_error)
                    else stringResource(R.string.user_guess)
                )
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmitClicked() }),
        )
        Spacer(modifier = modifier.height(32.dp))
        Button(onClick = onSubmitClicked) {
            Text(text = stringResource(R.string.submit_answer))
        }
    }
}

@Composable
private fun ChooseWordCategorySection(
    modifier: Modifier,
    categories: List<CategoryWithId>,
    isGameReadyToLaunch: Boolean,
    launchTheGame: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.game_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        LazyVerticalGrid(
            modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 2),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GameCategoryItem(
                    categoryName = stringResource(R.string.category_all),
                    categoryId = ALL_CATEGORY_ID,
                    onClick = onCategoryClick,
                )
            }
            items(categories) {
                GameCategoryItem(
                    categoryName = it.categoryName,
                    categoryId = it.categoryId,
                    onClick = onCategoryClick
                )
            }
        }
        Button(onClick = launchTheGame, enabled = isGameReadyToLaunch) {
            Text(text = stringResource(R.string.start_the_game))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameResultSection(
    modifier: Modifier,
    correctAnswerCount: Int,
    wrongAnswerCount: Int,
    successRate: String,
    userAnswers: List<QuizResult>,
    onReturnGamesScreenClick: () -> Unit,
    quizResultEmote: QuizResultEmote?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Subtitle(
            title = stringResource(id = R.string.correct_answer),
            contentText = "$correctAnswerCount"
        )
        Subtitle(
            title = stringResource(id = R.string.wrong_answer),
            contentText = "$wrongAnswerCount"
        )
        Subtitle(
            title = stringResource(id = R.string.success_rate),
            contentText = successRate
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stickyHeader {
                RowCellTitle(
                    s1 = stringResource(id = R.string.question),
                    s2 = stringResource(id = R.string.correct_answer),
                    s3 = stringResource(id = R.string.your_answer)
                )
            }
            items(userAnswers) { result ->
                RowCell(s1 = result.question, s2 = result.correctAnswer, s3 = result.userAnswer)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (quizResultEmote != null) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = quizResultEmote.emote, fontSize = 100.sp)
                Text(text = quizResultEmote.message.asString(), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = onReturnGamesScreenClick) {
                Text(text = stringResource(id = R.string.return_games_screen))
            }
        }
    }
}

@Composable
private fun Subtitle(title: String, contentText: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$title: ")
            }
            append(contentText)
        }
    )
}

@Composable
private fun RowCell(
    s1: String,
    s2: String,
    s3: String,
    color: Color? = null
) {
    val isAnswerCorrect = s2 == s3

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, shape = RectangleShape, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = s1
        )
        Text(modifier = Modifier.weight(1f), text = s2)
        Text(
            modifier = Modifier.weight(1f),
            text = s3,
            color = color
                ?: if (isAnswerCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RowCellTitle(
    s1: String,
    s2: String,
    s3: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RectangleShape,
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = s1,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f),
            text = s2,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f),
            text = s3,
            fontWeight = FontWeight.Bold
        )
    }
}