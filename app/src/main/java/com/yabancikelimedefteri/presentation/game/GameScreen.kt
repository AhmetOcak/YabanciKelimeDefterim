package com.yabancikelimedefteri.presentation.game

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.game.models.GameCategoryItem
import com.yabancikelimedefteri.presentation.game.models.GameWordItem

const val ALL_CATEGORY_ID = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigateToRoute: (String) -> Unit, viewModel: GameViewModel = hiltViewModel()) {

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
            TopAppBar(title = { Text(text = stringResource(id = R.string.word_game)) })
        },
        bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.GAMES.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        GameScreenContent(
            modifier = Modifier.padding(paddingValues),
            wordIndex = viewModel.wordIndex,
            onGuessClicked = viewModel::handleGuessClick,
            onValueChanged = viewModel::updateGuessWord,
            value = viewModel.guessWord,
            isError = viewModel.guessWordFieldError,
            words = uiState.words,
            isGameReadyToLaunch = uiState.isGameReadyToLaunch,
            launchTheGame = viewModel::launchTheGame,
            onCategoryClick = viewModel::handleCategoryClick,
            categories = uiState.categories,
            gameStatus = uiState.gameStatus,
            onReturnCategorySectionClick = viewModel::reLaunchTheGame
        )
    }
}

@Composable
private fun GameScreenContent(
    modifier: Modifier,
    wordIndex: Int,
    onGuessClicked: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    value: String,
    isError: Boolean,
    words: List<WordWithId>,
    isGameReadyToLaunch: Boolean,
    launchTheGame: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    categories: List<CategoryWithId>,
    gameStatus: GameStatus,
    onReturnCategorySectionClick: () -> Unit
) {
    when (gameStatus) {
        GameStatus.PREPARATION -> {
            ChooseGameCategorySection(
                modifier = modifier,
                categories = categories,
                isGameReadyToLaunch = isGameReadyToLaunch,
                launchTheGame = launchTheGame,
                onCategoryClick = onCategoryClick
            )
        }

        GameStatus.STARTED -> {
            GameSection(
                modifier = modifier,
                wordIndex = wordIndex,
                value = value,
                onValueChanged = onValueChanged,
                isError = isError,
                onGuessClicked = onGuessClicked,
                words = words,
                onReturnCategorySectionClick = onReturnCategorySectionClick
            )
        }

        GameStatus.END -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "End Of The Game")
            }
        }
    }
}

@Composable
private fun ChooseGameCategorySection(
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

@Composable
private fun GameSection(
    modifier: Modifier,
    words: List<WordWithId>,
    wordIndex: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    onGuessClicked: (String) -> Unit,
    onReturnCategorySectionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (words.size < 2) {
            AtLeastTwoWordMessage(onReturnCategorySectionClick = onReturnCategorySectionClick)
        } else {
            GameWordItem(word = words[wordIndex].foreignWord)
            Space()
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChanged,
                label = {
                    Text(
                        text = if (isError) stringResource(id = R.string.text_field_error)
                        else stringResource(R.string.user_guess)
                    )
                },
                isError = isError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onGuessClicked(words[wordIndex].foreignWord) }),
            )
            Space()
            Button(onClick = { onGuessClicked(words[wordIndex].foreignWord) }) {
                Text(text = stringResource(R.string.submit_answer))
            }
        }
    }
}

@Composable
private fun AtLeastTwoWordMessage(onReturnCategorySectionClick: () -> Unit) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        text = stringResource(R.string.at_least_two_word_warning),
        textAlign = TextAlign.Center
    )
    Button(onClick = onReturnCategorySectionClick) {
        Text(text = stringResource(id = R.string.ok))
    }
}

@Composable
private fun Space(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(32.dp))
}