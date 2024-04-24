package com.yabancikelimedefteri.presentation.word

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.isScrollingUp
import com.yabancikelimedefteri.core.ui.component.EmptyListMessage
import com.yabancikelimedefteri.core.ui.component.MultiActionBar
import com.yabancikelimedefteri.domain.model.word.WordWithId

@Composable
fun WordScreen(
    upPress: () -> Unit,
    viewModel: WordViewModel = hiltViewModel(),
    isWordListTypeThin: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddWordSheet by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

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
                onMenuItemClick = viewModel::handleOnMenuItemClick
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp().value,
                enter = scaleIn(),
                exit = scaleOut()

            ) {
                FloatingActionButton(onClick = { showAddWordSheet = true }) {
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
                    lazyListState = lazyListState
                )
            }

            UiEvent.SEARCHING -> {
                SearchWordContent(
                    modifier = Modifier.padding(paddingValues),
                    searchResults = uiState.searchResults,
                    onDeleteClick = viewModel::deleteWord,
                    isWordListTypeThin = isWordListTypeThin
                )
            }
        }

        if (showAddWordSheet) {
            AddWordSheet(
                foreignWordValue = viewModel.foreignWord,
                onForeignWordValueChange = viewModel::updateForeignWord,
                meaningWordValue = viewModel.meaningWord,
                onMeaningWordValueChange = viewModel::updateMeaningWord,
                onAddWordClick = {
                    showAddWordSheet = false
                    viewModel.addWord()
                },
                onDismissRequest = {
                    showAddWordSheet = false
                    viewModel.clearAddWordVars()
                }
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
    lazyListState: LazyListState
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
                            isWordListTypeThin = isWordListTypeThin
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
    isWordListTypeThin: Boolean
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
                        isWordListTypeThin = isWordListTypeThin
                    )
                }
            }
        }
    }
}

@Composable
private fun AddWordSheet(
    foreignWordValue: String,
    onForeignWordValueChange: (String) -> Unit,
    meaningWordValue: String,
    onMeaningWordValueChange: (String) -> Unit,
    onAddWordClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onAddWordClick,
                enabled = foreignWordValue.isNotBlank() && meaningWordValue.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_word),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = foreignWordValue,
                    onValueChange = onForeignWordValueChange,
                    label = {
                        Text(text = stringResource(id = R.string.foreign_word))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = meaningWordValue,
                    onValueChange = onMeaningWordValueChange,
                    label = {
                        Text(text = stringResource(id = R.string.meaning))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = if (foreignWordValue.isNotBlank() && meaningWordValue.isNotBlank()) {
                            { onAddWordClick() }
                        } else null
                    ),
                    supportingText = {
                        Text(text = stringResource(id = R.string.add_word_info))
                    }
                )
            }
        }
    )
}