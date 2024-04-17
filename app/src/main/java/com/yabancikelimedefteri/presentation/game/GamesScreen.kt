package com.yabancikelimedefteri.presentation.game

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onNavigateToRoute: (String) -> Unit,
    navigateToQuizGame: () -> Unit,
    viewModel: GamesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.errorMessages.first().asString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessages()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.choose_a_game))
            })
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
            isGamesCanPlay = uiState.isGamesCanPlay,
            games = viewModel.games,
            onGameClick = remember {
                { gameType ->
                    when (gameType) {
                        GameType.QUIZ -> navigateToQuizGame()

                        else -> {}
                    }
                }
            }
        )
    }
}

@Composable
private fun GameScreenContent(
    modifier: Modifier,
    isGamesCanPlay: Boolean,
    games: List<Game>,
    onGameClick: (GameType) -> Unit
) {
    if (isGamesCanPlay) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(games, key = { it.gameType }) { game ->
                GameItem(
                    gameName = game.gameName.asString(),
                    gameImage = game.gameImage,
                    gameType = game.gameType,
                    onClick = onGameClick
                )
            }
        }
    } else {
        AtLeastFiveWordMessage(modifier = modifier.fillMaxSize())
    }
}

@Composable
private fun GameItem(
    gameName: String, gameImage: Int, gameType: GameType, onClick: (GameType) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { onClick(gameType) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                Icon(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / 8)
                        .aspectRatio(1f)
                        .scale(3f),
                    painter = painterResource(id = gameImage),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                Text(
                    text = gameName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
private fun AtLeastFiveWordMessage(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🙃", fontSize = 96.sp)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = stringResource(R.string.at_least_five_word_warning),
            textAlign = TextAlign.Center
        )
    }
}