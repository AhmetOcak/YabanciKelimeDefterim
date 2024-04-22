package com.yabancikelimedefteri.presentation.word_categories

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.EmptyListMessage
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar
import com.yabancikelimedefteri.domain.model.word.CategoryWithId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordCategoriesScreen(
    onCategoryClick: (Int) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: WordCategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showUpdateCategoryNameSheet by remember { mutableStateOf(false) }
    var showAddCategorySheet by remember { mutableStateOf(false) }

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
                Text(text = stringResource(id = R.string.word_categories))
            }
            )
        },
        bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.CATEGORIES.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddCategorySheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(paddingValues),
            onDeleteClick = viewModel::deleteCategory,
            onCategoryCardClick = onCategoryClick,
            onEditClick = { categoryId ->
                viewModel.updateSelectedCaId(categoryId)
                showUpdateCategoryNameSheet = true
            },
            categories = uiState.categories,
            isLoading = uiState.isLoading
        )

        if (showUpdateCategoryNameSheet) {
            UpdateCategoryNameSheet(
                categoryNameValue = viewModel.updatedCategoryName,
                onCategoryNameChanged = viewModel::updateUpdatedCategoryName,
                updateCategoryName = remember {
                    {
                        viewModel.updateCategoryName()
                        showUpdateCategoryNameSheet = false
                    }
                },
                onDismissRequest = remember {
                    {
                        showUpdateCategoryNameSheet = false
                        viewModel.clearUpdateCategoryNameVars()
                    }
                }
            )
        }

        if (showAddCategorySheet) {
            AddCategorySheet(
                categoryNameValue = viewModel.newCategoryName,
                onCategoryValueChange = viewModel::updateNewCategoryName,
                onAddCategoryClick = remember {
                    {
                        viewModel.addCategory()
                        showAddCategorySheet = false
                    }
                },
                onDismissRequest = remember {
                    {
                        showAddCategorySheet = false
                        viewModel.clearNewCategoryVars()
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    categories: List<CategoryWithId>,
    isLoading: Boolean,
    onCategoryCardClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
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
            if (categories.isEmpty()) {
                EmptyListMessage(message = stringResource(R.string.empty_category_message))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    items(items = categories, key = { it.categoryId }) {
                        CategoryCard(
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            onEditClick = onEditClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateCategoryNameSheet(
    categoryNameValue: String,
    onCategoryNameChanged: (String) -> Unit,
    updateCategoryName: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = categoryNameValue,
                onValueChange = onCategoryNameChanged,
                label = {
                    Text(text = stringResource(R.string.new_cat_name))
                }
            )
            Button(
                onClick = updateCategoryName,
                enabled = categoryNameValue.isNotBlank()
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCategorySheet(
    categoryNameValue: String,
    onCategoryValueChange: (String) -> Unit,
    onAddCategoryClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = categoryNameValue,
                onValueChange = onCategoryValueChange,
                label = {
                    Text(text = stringResource(id = R.string.category_name))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = if (categoryNameValue.isNotBlank()) {
                        { onAddCategoryClick() }
                    } else null
                )
            )
            Button(onClick = onAddCategoryClick, enabled = categoryNameValue.isNotBlank()) {
                Text(text = stringResource(id = R.string.create_category))
            }
        }
    }
}