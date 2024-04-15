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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.ListType
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.core.ui.component.WordCard
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    resources: Resources,
    listType: ListType
) {
    val viewModel: WordViewModel = hiltViewModel()

    val getWordsState by viewModel.getWordState.collectAsState()
    val deleteWordState by viewModel.deleteWordState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    if (deleteWordState is DeleteWordState.Success) {
        CustomToast(
            context = LocalContext.current,
            message = resources.getString(R.string.word_removed)
        )
        CustomToast(context = LocalContext.current, message = stringResource(R.string.word_removed))
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
        onDeleteClick = remember {
            { viewModel.deleteWord(it) }
        },
        getWords = remember {
            { viewModel.categoryId?.let { viewModel.getWords(it) } }
        },
        listType = listType
    )
}

@Composable
private fun WordScreenContent(
    modifier: Modifier,
    getWordsState: GetWordState,
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit,
    listType: ListType
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
                    getWordsState = getWordsState,
                    onDeleteClick = onDeleteClick,
                    getWords = getWords,
                    listType = listType
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
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit,
    listType: ListType
) {
    if (getWordsState.data.isEmpty()) {
        EmptyWordListMessage()
    } else {
        ResponsiveWordList(
            data = getWordsState.data,
            onDeleteClick = onDeleteClick,
            getWords = getWords,
            listType = listType
        )
    }
}

@Composable
private fun ResponsiveWordList(
    data: List<WordWithId>,
    onDeleteClick: (Int) -> Unit,
    getWords: () -> Unit,
    listType: ListType
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(items = data, key = { it.wordId }) {
                when (listType) {
                    ListType.RECTANGLE -> {
                        WordCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenWidthDp.dp / 2),
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick,
                            getWords = getWords
                        )
                    }

                    ListType.THIN -> {
                        WordCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick,
                            getWords = getWords
                        )
                    }
                }
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(if (listType == ListType.RECTANGLE) 3 else 2),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) {
                when (listType) {
                    ListType.RECTANGLE -> {
                        WordCard(
                            modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3),
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick,
                            getWords = getWords
                        )
                    }

                    ListType.THIN -> {
                        WordCard(
                            modifier = Modifier
                                .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                                .wrapContentHeight(),
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick,
                            getWords = getWords
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyWordListMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.empty_word_message), textAlign = TextAlign.Center)
    }
}