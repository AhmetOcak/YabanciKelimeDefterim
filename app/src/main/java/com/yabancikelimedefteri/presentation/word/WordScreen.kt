package com.yabancikelimedefteri.presentation.word

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.isScrollingUp
import com.yabancikelimedefteri.core.ui.component.AddOrUpdateWordDialog
import com.yabancikelimedefteri.core.ui.component.EmptyListMessage
import com.yabancikelimedefteri.core.ui.component.MultiActionBar
import com.yabancikelimedefteri.domain.model.DialogType
import com.yabancikelimedefteri.domain.model.word.WordWithId
import kotlinx.coroutines.launch

@Composable
fun WordScreen(
    upPress: () -> Unit,
    viewModel: WordViewModel = hiltViewModel(),
    isWordListTypeThin: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
            MultiActionBar(
                upPress = upPress,
                title = stringResource(id = R.string.my_words),
                searchValue = viewModel.searchValue,
                onSearchValueChange = viewModel::updateSearchValueAndSearch,
                onMenuItemClick = remember {
                    { sortType ->
                        coroutineScope.launch {
                            viewModel.handleOnMenuItemClick(sortType)
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp().value,
                enter = scaleIn(),
                exit = scaleOut()

            ) {
                FloatingActionButton(onClick = remember { { viewModel.showAddOrUpdateWordDialog(DialogType.Add) } }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        when (uiState.uiEvent) {
            UiEvent.WORDS -> {
                WordScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    onDeleteClick = viewModel::deleteWord,
                    words = uiState.words,
                    isLoading = uiState.isLoading,
                    isWordListTypeThin = isWordListTypeThin,
                    lazyListState = lazyListState,
                    onEditWordClick = viewModel::setInitialValuesForUpdateWord
                )
            }

            UiEvent.SEARCHING -> {
                SearchWordContent(
                    modifier = Modifier.padding(paddingValues),
                    searchResults = uiState.searchResults,
                    onDeleteClick = viewModel::deleteWord,
                    isWordListTypeThin = isWordListTypeThin,
                    onEditWordClick = viewModel::setInitialValuesForUpdateWord
                )
            }
        }

        if (uiState.showAddOrUpdateWordDialog) {
            AddOrUpdateWordDialog(
                foreignWordValue = viewModel.foreignWord,
                onForeignWordValueChange = viewModel::updateForeignWord,
                meaningWordValue = viewModel.meaningWord,
                onMeaningWordValueChange = viewModel::updateMeaningWord,
                onApply = viewModel::handleAddOrUpdateWordApplyClick,
                onDismissRequest = viewModel::handleDismissAddWordDialog,
                onImportanceLevelClick = viewModel::setImportanceLevel,
                importanceLevel = viewModel.importanceLevel.ordinal,
                dialogType = viewModel.dialogType
            )
        }
    }
}

@Composable
private fun WordScreenContent(
    modifier: Modifier,
    words: List<WordWithId>,
    onDeleteClick: (Int) -> Unit,
    isLoading: Boolean,
    isWordListTypeThin: Boolean,
    lazyListState: LazyListState,
    onEditWordClick: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (words.isEmpty()) {
                EmptyListMessage(message = stringResource(R.string.empty_word_message))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    items(items = words, key = { it.wordId }) {
                        WordCard(
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick,
                            isWordListTypeThin = isWordListTypeThin,
                            importanceLevel = it.importanceLevel,
                            onEditClick = onEditWordClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchWordContent(
    modifier: Modifier,
    searchResults: List<WordWithId>,
    onDeleteClick: (Int) -> Unit,
    isWordListTypeThin: Boolean,
    onEditWordClick: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (searchResults.isEmpty()) {
            EmptyListMessage(message = stringResource(id = R.string.word_not_found))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
            ) {
                items(items = searchResults, key = { it.wordId }) {
                    WordCard(
                        foreignWord = it.foreignWord,
                        meaning = it.meaning,
                        wordId = it.wordId,
                        onDeleteClick = onDeleteClick,
                        isWordListTypeThin = isWordListTypeThin,
                        importanceLevel = it.importanceLevel,
                        onEditClick = onEditWordClick
                    )
                }
            }
        }
    }
}