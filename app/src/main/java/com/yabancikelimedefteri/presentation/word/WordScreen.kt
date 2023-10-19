package com.yabancikelimedefteri.presentation.word

import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.core.ui.component.WordCard
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun WordScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit, resources: Resources) {

    val viewModel: WordViewModel = hiltViewModel()

    val getWordsState by viewModel.getWordState.collectAsState()
    val deleteWordState by viewModel.deleteWordState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    if (deleteWordState is DeleteWordState.Success) {
        CustomToast(context = LocalContext.current, message = resources.getString(R.string.word_removed))
        viewModel.resetDeleteWordState()
    } else if (deleteWordState is DeleteWordState.Error) {
        CustomToast(
            context = LocalContext.current,
            message = (deleteWordState as DeleteWordState.Error).message
        )
        viewModel.resetDeleteWordState()
    }

    WordScreenContent(
        modifier = modifier,
        getWordsState = getWordsState,
        onDeleteClick = { viewModel.deleteWord(it) },
        getWords = { viewModel.categoryId?.let { viewModel.getWords(it) } },
        emptyWordText = resources.getString(R.string.empty_word_message)
    )
}

@Composable
private fun WordScreenContent(
    modifier: Modifier,
    getWordsState: GetWordState,
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit,
    emptyWordText: String
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
                    getWordsState,
                    modifier,
                    onDeleteClick,
                    getWords,
                    emptyWordText
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
    getWordsState: GetWordState.Success,
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit,
    emptyWordText: String
) {
    if (getWordsState.data.isEmpty()) {
        EmptyWordListMessage(modifier = modifier, emptyWordText = emptyWordText)
    } else {
        ResponsiveWordList(
            modifier = modifier,
            data = getWordsState.data,
            onDeleteClick = onDeleteClick,
            getWords = getWords
        )
    }
}

@Composable
private fun ResponsiveWordList(
    modifier: Modifier,
    data: List<WordWithId>,
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(items = data, key = { it.wordId }) {
                WordCard(
                    modifier = modifier,
                    foreignWord = it.foreignWord,
                    meaning = it.meaning,
                    height = LocalConfiguration.current.screenWidthDp.dp / 2,
                    wordId = it.wordId,
                    onDeleteClick = onDeleteClick,
                    getWords = getWords
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(3),
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
                    height = LocalConfiguration.current.screenWidthDp.dp / 3,
                    width = LocalConfiguration.current.screenWidthDp.dp / 3,
                    getWords = getWords
                )
            }
        }
    }
}

@Composable
private fun EmptyWordListMessage(modifier: Modifier, emptyWordText: String) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emptyWordText, textAlign = TextAlign.Center)
    }
}