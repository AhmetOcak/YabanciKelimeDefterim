package com.yabancikelimedefteri.presentation.dictionary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.domain.model.WordWithId

@Composable
fun DictionaryScreen(modifier: Modifier = Modifier) {

    val viewModel: DictionaryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    DictionaryScreenContent(
        modifier = modifier,
        searchFieldVal = uiState.searchText,
        onSearchTextChange = {
            viewModel.updateSearchField(it)
        },
        searchResults = uiState.searchResults,
        showSearchResultEmpty = uiState.isSearching && uiState.searchResults.isEmpty(),
        onSearchClicked = { viewModel.onSearchClicked() },
        isSearchFieldError = uiState.searchFieldError
    )
}

@Composable
private fun DictionaryScreenContent(
    modifier: Modifier,
    searchFieldVal: String,
    onSearchTextChange: (String) -> Unit,
    searchResults: List<WordWithId>,
    showSearchResultEmpty: Boolean,
    onSearchClicked: () -> Unit,
    isSearchFieldError: Boolean
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(
            modifier = modifier,
            searchFieldVal = searchFieldVal,
            onSearchTextChange = onSearchTextChange,
            onSearchClicked = onSearchClicked,
            isSearchFieldError = isSearchFieldError
        )
        SearchList(
            modifier = modifier,
            searchResults = searchResults
        )
        if (showSearchResultEmpty) {
            SearchResultEmpty(modifier)
        }
    }
}

@Composable
private fun SearchList(
    modifier: Modifier,
    searchResults: List<WordWithId>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(searchResults, key = { it.wordId }) {
            WordItem(modifier, it.foreignWord)
        }
    }
}

@Composable
private fun SearchField(
    modifier: Modifier,
    searchFieldVal: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    isSearchFieldError: Boolean
) {
    CustomTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = searchFieldVal,
        onValueChange = onSearchTextChange,
        labelText = stringResource(id = R.string.search),
        errorMessage = stringResource(id = R.string.text_field_error),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        isError = isSearchFieldError
    )
}

@Composable
private fun SearchResultEmpty(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.search_empty),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WordItem(modifier: Modifier, word: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = word,
            style = MaterialTheme.typography.body1
        )
        Divider(modifier = modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDictionaryScreen() {

    DictionaryScreen()
}