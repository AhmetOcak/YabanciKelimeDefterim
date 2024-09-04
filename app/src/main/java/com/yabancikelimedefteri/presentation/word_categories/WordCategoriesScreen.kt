package com.yabancikelimedefteri.presentation.word_categories

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.yabancikelimedefteri.core.helpers.isScrollingUp
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

    var showUpdateCategoryDialog by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    if (uiState.errorMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current, uiState.errorMessages.first().asString(), Toast.LENGTH_LONG
        ).show()
        viewModel.consumedErrorMessage()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.word_categories))
            })
        }, bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.CATEGORIES.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp().value,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(onClick = { showAddCategoryDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add, contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        WordCategoriesScreenContent(
            modifier = Modifier.padding(paddingValues),
            onDeleteClick = viewModel::deleteCategory,
            onCategoryCardClick = onCategoryClick,
            onEditClick = remember {
                { categoryId ->
                    viewModel.updateSelectedCaId(categoryId)
                    showUpdateCategoryDialog = true
                }
            },
            categories = uiState.categories,
            isLoading = uiState.isLoading,
            lazyListState = lazyListState
        )

        if (showUpdateCategoryDialog) {
            UpdateCategoryDialog(
                categoryNameValue = viewModel.updatedCategoryName,
                onCategoryNameChanged = viewModel::updateUpdatedCategoryName,
                updateCategoryName = remember {
                    {
                        showUpdateCategoryDialog = false
                        viewModel.updateCategoryName()
                    }
                },
                onDismissRequest = remember {
                    {
                        showUpdateCategoryDialog = false
                        viewModel.clearUpdateCategoryNameVars()
                    }
                }
            )
        }

        if (showAddCategoryDialog) {
            AddCategoryDialog(
                categoryNameValue = viewModel.newCategoryName,
                onCategoryValueChange = viewModel::updateNewCategoryName,
                onAddCategoryClick = remember {
                    {
                        viewModel.addCategory()
                        showAddCategoryDialog = false
                    }
                },
                onDismissRequest = remember {
                    {
                        showAddCategoryDialog = false
                        viewModel.clearNewCategoryVars()
                    }
                }
            )
        }
    }
}

@Composable
private fun WordCategoriesScreenContent(
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    categories: List<CategoryWithId>,
    isLoading: Boolean,
    onCategoryCardClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    lazyListState: LazyListState
) {
    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
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
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
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

@Composable
private fun AddCategoryDialog(
    categoryNameValue: String,
    onCategoryValueChange: (String) -> Unit,
    onAddCategoryClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onAddCategoryClick,
                enabled = categoryNameValue.isNotBlank()
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
                text = stringResource(id = R.string.create_category),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = categoryNameValue,
                onValueChange = onCategoryValueChange,
                label = {
                    Text(text = stringResource(id = R.string.category_name))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = if (categoryNameValue.isNotBlank()) {
                    { onAddCategoryClick() }
                } else null)
            )
        }
    )
}

@Composable
private fun UpdateCategoryDialog(
    categoryNameValue: String,
    onCategoryNameChanged: (String) -> Unit,
    updateCategoryName: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = updateCategoryName,
                enabled = categoryNameValue.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.update_category_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = categoryNameValue,
                onValueChange = onCategoryNameChanged,
                label = {
                    Text(text = stringResource(id = R.string.category_name))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = if (categoryNameValue.isNotBlank()) {
                    { updateCategoryName() }
                } else null)
            )
        }
    )
}