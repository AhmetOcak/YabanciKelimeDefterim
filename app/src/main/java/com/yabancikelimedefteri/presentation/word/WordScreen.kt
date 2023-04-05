package com.yabancikelimedefteri.presentation.word

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.core.ui.component.WordCard
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun WordScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: WordViewModel = hiltViewModel()

    val getWordsState by viewModel.getWordState.collectAsState()
    val deleteWordState by viewModel.deleteWordState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    WordScreenContent(
        modifier = modifier,
        getWordsState = getWordsState,
        deleteWordState = deleteWordState,
        onDeleteClick = { viewModel.deleteWord(it) },
        resetDeleteWordState = { viewModel.resetDeleteWordState() }
    )
}

@Composable
private fun WordScreenContent(
    modifier: Modifier,
    getWordsState: GetWordState,
    deleteWordState: DeleteWordState,
    onDeleteClick: (Int) -> Unit,
    resetDeleteWordState: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (getWordsState) {
            is GetWordState.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is GetWordState.Success -> {
                WordList(
                    deleteWordState,
                    getWordsState,
                    modifier,
                    onDeleteClick,
                    resetDeleteWordState
                )
            }
            is GetWordState.Error -> {
                CustomToast(context = LocalContext.current, message = getWordsState.message)
            }
        }
    }
}

@Composable
private fun WordList(
    deleteWordState: DeleteWordState,
    getWordsState: GetWordState.Success,
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    resetDeleteWordState: () -> Unit
) {
    when (deleteWordState) {
        is DeleteWordState.Nothing -> {
            if (getWordsState.data.isEmpty()) {
                EmptyWordListMessage(modifier = modifier)
            } else {
                ResponsiveWordList(
                    modifier = modifier,
                    data = getWordsState.data,
                    onDeleteClick = onDeleteClick
                )
            }
        }
        is DeleteWordState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DeleteWordState.Success -> {
            CustomToast(context = LocalContext.current, message = "Kelime kaldırıldı")
            resetDeleteWordState()
        }
        is DeleteWordState.Error -> {
            CustomToast(context = LocalContext.current, message = deleteWordState.message)
        }
    }
}

@Composable
private fun ResponsiveWordList(
    modifier: Modifier,
    data: List<WordWithId>,
    onDeleteClick: (Int) -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(data) {
                WordCard(
                    modifier = modifier,
                    foreignWord = it.foreignWord,
                    meaning = it.meaning,
                    wordId = it.wordId,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) {
                WordCard(
                    modifier = modifier,
                    foreignWord = it.foreignWord,
                    meaning = it.meaning,
                    wordId = it.wordId,
                    onDeleteClick = onDeleteClick,
                    height = LocalConfiguration.current.screenWidthDp.dp / 3
                )
            }
        }
    }
}

@Composable
private fun EmptyWordListMessage(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Kelime defterinde hiç kelime yok 😥", textAlign = TextAlign.Center)
    }
}