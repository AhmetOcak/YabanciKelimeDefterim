package com.yabancikelimedefteri.presentation.game.games.pairing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.theme.successGreen
import com.yabancikelimedefteri.presentation.game.games.components.GameScreenSkeleton
import com.yabancikelimedefteri.presentation.game.games.components.MinWordWarning

@Composable
fun PairingGameScreen(
    upPress: () -> Unit,
    viewModel: PairingGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.pairingGameUiState.collectAsStateWithLifecycle()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val puzzleWidth = screenWidth - 48.dp
    val puzzleHeight = (screenHeight - 128.dp) / 5

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
        topBarTitle = stringResource(id = R.string.word_pairing),
        upPress = upPress,
        gameEndContent = remember {
            { paddingValues ->
                EndGameMessage(modifier = Modifier.padding(paddingValues))
            }
        }
    ) { paddingValues ->
        if (uiState.words.size < 5) {
            MinWordWarning()
        } else {
            PairingGame(
                modifier = Modifier.padding(paddingValues),
                wordList = viewModel.getQuestions(),
                pairStatus = viewModel.pairStatus,
                onPuzzleClick = viewModel::handlePuzzleClick,
                selectedPuzzle1Index = viewModel.selectedPuzzle1.second,
                selectedPuzzle2Index = viewModel.selectedPuzzle2.second,
                isPuzzlesEnable = viewModel.isPuzzlesEnabled,
                hidePuzzle = remember {
                    { index ->
                        viewModel.getCorrectPuzzles().contains(index)
                    }
                },
                puzzleWidth = puzzleWidth,
                puzzleHeight = puzzleHeight
            )
        }
    }
}

@Composable
private fun PairingGame(
    modifier: Modifier,
    wordList: List<String>,
    pairStatus: PairStatus,
    onPuzzleClick: (String, Int) -> Unit,
    selectedPuzzle1Index: Int,
    selectedPuzzle2Index: Int,
    isPuzzlesEnable: Boolean,
    hidePuzzle: (Int) -> Boolean,
    puzzleWidth: Dp,
    puzzleHeight: Dp
) {
    LazyVerticalGrid(
        modifier = modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(LocalConfiguration.current.screenHeightDp.dp)
            .padding(16.dp),
        columns = GridCells.Fixed(2),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(wordList) { index, word ->
            PuzzleItem(
                index = index,
                word = word,
                onClick = onPuzzleClick,
                containerColor = if (index == selectedPuzzle1Index || index == selectedPuzzle2Index) {
                    when (pairStatus) {
                        PairStatus.NOTHING -> MaterialTheme.colorScheme.surfaceVariant
                        PairStatus.SELECTED -> MaterialTheme.colorScheme.primaryContainer
                        PairStatus.TRUE -> successGreen
                        PairStatus.FALSE -> MaterialTheme.colorScheme.error
                    }
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                enabled = if (index == selectedPuzzle1Index || index == selectedPuzzle2Index) {
                    true
                } else isPuzzlesEnable,
                hide = hidePuzzle(index),
                puzzleWidth = puzzleWidth,
                puzzleHeight = puzzleHeight
            )
        }
    }
}

@Composable
private fun PuzzleItem(
    index: Int,
    word: String,
    onClick: (String, Int) -> Unit,
    containerColor: Color,
    enabled: Boolean,
    hide: Boolean,
    puzzleWidth: Dp,
    puzzleHeight: Dp
) {
    AnimatedVisibility(
        visible = !hide,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Surface(
            modifier = Modifier
                .width(puzzleWidth)
                .height(puzzleHeight),
            onClick = { onClick(word, index) },
            enabled = enabled,
            shape = CardDefaults.shape,
            color = containerColor,
            border = CardDefaults.outlinedCardBorder(),
            contentColor = CardDefaults.cardColors().contentColor,
            interactionSource = remember { MutableInteractionSource() },
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = word, textAlign = TextAlign.Center)
                }
            }
        }
    }

    if (hide) {
        Box(
            modifier = Modifier
                .width(puzzleWidth)
                .height(puzzleHeight)
        )
    }
}

@Composable
private fun EndGameMessage(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_very_good),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = stringResource(id = R.string.pairing_game_end),
            textAlign = TextAlign.Center
        )
    }
}