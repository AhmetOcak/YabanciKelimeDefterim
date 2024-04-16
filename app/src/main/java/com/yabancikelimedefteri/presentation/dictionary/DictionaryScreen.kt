package com.yabancikelimedefteri.presentation.dictionary

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.EmptyListMessage
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar
import com.yabancikelimedefteri.domain.model.WordWithId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    onNavigateToRoute: (String) -> Unit,
    viewModel: DictionaryViewModel = hiltViewModel()
) {
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
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.my_dictionary))
            })
        },
        bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.DICTIONARY.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        DictionaryScreenContent(
            modifier = Modifier.padding(paddingValues),
            searchFieldVal = uiState.searchText,
            onSearchTextChange = viewModel::updateSearchField,
            searchResults = uiState.searchResults,
            showSearchResultEmpty = uiState.isSearching && uiState.searchResults.isEmpty(),
            onSearchClicked = viewModel::onSearchClicked,
            isSearchFieldError = uiState.searchFieldError,
            meaningOfWord = uiState.wordMeaning ?: "",
            onWordClicked = viewModel::getWordMeaning,
            searchType = uiState.searchType
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun DictionaryScreenContent(
    modifier: Modifier,
    searchFieldVal: String,
    onSearchTextChange: (String) -> Unit,
    searchResults: List<WordWithId>,
    showSearchResultEmpty: Boolean,
    onSearchClicked: () -> Unit,
    isSearchFieldError: Boolean,
    meaningOfWord: String,
    onWordClicked: (WordWithId) -> Unit,
    searchType: SearchType
) {
    var showWordDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchFieldVal,
            onValueChange = onSearchTextChange,
            label = {
                Text(
                    text = stringResource(
                        id = if (isSearchFieldError) R.string.text_field_error else R.string.search
                    )
                )
            },
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            isError = isSearchFieldError
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults, key = { it.wordId }) { word ->
                WordItem(
                    word = word,
                    onWordClicked = {
                        onWordClicked(it)
                        showWordDialog = true
                    },
                    searchType = searchType
                )
            }
        }
        if (showSearchResultEmpty) {
            EmptyListMessage(message = stringResource(id = R.string.search_empty))
        }
        if (showWordDialog) {
            WordMeaning(
                meaningOfWord = meaningOfWord,
                onDismiss = { showWordDialog = false }
            )
        }
    }
}

@Composable
private fun WordItem(
    word: WordWithId,
    onWordClicked: (WordWithId) -> Unit,
    searchType: SearchType
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        onClick = { onWordClicked(word) }) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = when (searchType) {
                SearchType.FOREIGN_WORD -> {
                    word.foreignWord
                }

                SearchType.MEANING_OF_WORD -> {
                    word.meaning
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WordMeaning(meaningOfWord: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 8.dp),
                text = meaningOfWord,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
