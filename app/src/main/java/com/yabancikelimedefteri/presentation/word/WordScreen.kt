package com.yabancikelimedefteri.presentation.word

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.yabancikelimedefteri.core.ui.component.EmptyListMessage
import com.yabancikelimedefteri.domain.model.WordWithId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(upPress: () -> Unit, viewModel: WordViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddWordSheet by remember { mutableStateOf(false) }

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
                title = { Text(text = stringResource(id = R.string.my_words)) },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddWordSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        WordScreenContent(
            modifier = Modifier.padding(paddingValues),
            onDeleteClick = viewModel::deleteWord,
            words = uiState.words,
            isLoading = uiState.isLoading
        )

        if (showAddWordSheet) {
            AddWordSheet(
                foreignWordValue = viewModel.foreignWord,
                onForeignWordValueChange = viewModel::updateForeignWord,
                isForeignWordFieldError = viewModel.foreignWordFieldError,
                meaningWordValue = viewModel.meaningWord,
                onMeaningWordValueChange = viewModel::updateMeaningWord,
                isMeaningWordFieldError = viewModel.meaningWordFieldError,
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
    isLoading: Boolean
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    items(items = words, key = { it.wordId }) {
                        WordCard(
                            foreignWord = it.foreignWord,
                            meaning = it.meaning,
                            wordId = it.wordId,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddWordSheet(
    foreignWordValue: String,
    onForeignWordValueChange: (String) -> Unit,
    isForeignWordFieldError: Boolean,
    meaningWordValue: String,
    onMeaningWordValueChange: (String) -> Unit,
    isMeaningWordFieldError: Boolean,
    onAddWordClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = foreignWordValue,
                onValueChange = onForeignWordValueChange,
                label = {
                    Text(
                        text = stringResource(
                            id = if (isForeignWordFieldError) R.string.text_field_error else R.string.foreign_word
                        )
                    )
                },
                isError = isForeignWordFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = meaningWordValue,
                onValueChange = onMeaningWordValueChange,
                label = {
                    Text(
                        text = stringResource(
                            id = if (isMeaningWordFieldError) R.string.text_field_error else R.string.meaning
                        )
                    )
                },
                isError = isMeaningWordFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddWordClick()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddWordClick) {
                Text(text = stringResource(id = R.string.add_word))
            }
        }
    }
}