package com.yabancikelimedefteri.presentation.game.games.pairing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairingGameScreen(
    upPress: () -> Unit,
    pairingGameViewModel: PairingGameViewModel = hiltViewModel()
) {

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.word_pairing))
            },
            navigationIcon = {
                IconButton(onClick = upPress) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            })
    }) { paddingValues ->
        PairingGameScreenContent(
            modifier = Modifier.padding(paddingValues), wordList = listOf(
                "WORD 1",
                "WORD 2",
                "WORD 3",
                "WORD 4",
                "WORD 5",
                "WORD 6",
                "WORD 7",
                "WORD 8",
            )
        )
    }
}

@Composable
private fun PairingGameScreenContent(modifier: Modifier, wordList: List<String>) {
    LazyVerticalGrid(
        modifier = modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .height(LocalConfiguration.current.screenHeightDp.dp)
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wordList) {
            PuzzleItem(
                word = it,
                onClick = {}
            )
        }
    }
}

@Composable
private fun PuzzleItem(word: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = onClick
    ) {
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