package com.yabancikelimedefteri.presentation.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.HomeScreenFab
import com.yabancikelimedefteri.core.navigation.ListType
import com.yabancikelimedefteri.core.ui.component.CategoryCard
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.presentation.main.OrientationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateNext: (Int) -> Unit,
    resources: Resources,
    listType: ListType
    onNavigateNext: (Int) -> Unit

) {

    val viewModel: HomeViewModel = hiltViewModel()
    val getCategoriesState by viewModel.getCategoriesState.collectAsState()
    val deleteCategoryState by viewModel.deleteCategoryState.collectAsState()
    val updateCategoryState by viewModel.updateCategoryState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = sheetState.isVisible) {
        coroutineScope.launch {
            HomeScreenFab.showFab.value = !sheetState.isVisible
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { sheetState.currentValue }.collect {
            if (it == ModalBottomSheetValue.Hidden) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
    }

    BackHandler(!sheetState.isVisible) {
        onNavigateBack()
    }

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    if (deleteCategoryState is DeleteCategoryState.Success) {
        CustomToast(
            context = LocalContext.current,
            message = stringResource(R.string.category_removed)
        )
        viewModel.resetDeleteWordState()
    } else if (deleteCategoryState is DeleteCategoryState.Error) {
        CustomToast(
            context = LocalContext.current,
            message = (deleteCategoryState as DeleteCategoryState.Error).message
        )
        viewModel.resetDeleteWordState()
    }

    if (updateCategoryState is UpdateCategoryState.Success) {
        CustomToast(
            context = LocalContext.current,
            message = stringResource(R.string.update_cat_successfull)
        )
        viewModel.resetUpdateCatState()
    } else if (updateCategoryState is UpdateCategoryState.Error) {
        CustomToast(
            context = LocalContext.current,
            message = (deleteCategoryState as DeleteCategoryState.Error).message
        )
        viewModel.resetUpdateCatState()
    }

    HomeScreenContent(
        modifier = modifier,
        onDeleteClick = { viewModel.deleteCategories(it) },
        getCategoriesState = getCategoriesState,
        onCategoryCardClick = { onNavigateNext(it) },
        getCategories = { viewModel.getCategories() },
        emptyCategoryText = stringResource(R.string.empty_category_message),
        sheetState = sheetState,
        onEditClick = {
            viewModel.updateSelectedCaId(it)
            coroutineScope.launch {
                HomeScreenFab.showFab.value = false
                sheetState.show()
            }
        },
        categoryNameVal = viewModel.newCategoryName,
        onCategoryNameChanged = { viewModel.updateNewCategoryName(it) },
        updateCategoryName = {
            viewModel.updateCategoryName(
                viewModel.selectedCatId,
                viewModel.newCategoryName
            )
            coroutineScope.launch {
                viewModel.resetNewCatName()
                sheetState.hide()
                viewModel.getCategories()
            }
        },
        updateCategoryNameFieldError = viewModel.newCategoryNameFieldError,
        textFieldErrorMessage = resources.getString(R.string.text_field_error),
        updateCatNameLabel = resources.getString(R.string.new_cat_name),
        buttonText = resources.getString(R.string.save),
        listType = listType
        textFieldErrorMessage = stringResource(R.string.text_field_error),
        updateCatNameLabel = stringResource(R.string.new_cat_name),
        buttonText = stringResource(R.string.save)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    getCategoriesState: GetCategoriesState,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    emptyCategoryText: String,
    sheetState: ModalBottomSheetState,
    onEditClick: (Int) -> Unit,
    categoryNameVal: String,
    onCategoryNameChanged: (String) -> Unit,
    updateCategoryName: () -> Unit,
    updateCategoryNameFieldError: Boolean,
    textFieldErrorMessage: String,
    updateCatNameLabel: String,
    buttonText: String,
    listType: ListType
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (getCategoriesState) {
            is GetCategoriesState.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is GetCategoriesState.Success -> {
                CategoryList(
                    getCategoriesState,
                    modifier,
                    onDeleteClick,
                    onCategoryCardClick,
                    getCategories,
                    emptyCategoryText,
                    sheetState,
                    onEditClick,
                    categoryNameVal,
                    onCategoryNameChanged,
                    updateCategoryName,
                    updateCategoryNameFieldError,
                    textFieldErrorMessage,
                    updateCatNameLabel,
                    buttonText,
                    listType
                )
            }

            is GetCategoriesState.Error -> {
                CustomToast(context = LocalContext.current, message = getCategoriesState.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategoryList(
    getCategoriesState: GetCategoriesState.Success,
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    emptyCategoryText: String,
    sheetState: ModalBottomSheetState,
    onEditClick: (Int) -> Unit,
    categoryNameVal: String,
    onCategoryNameChanged: (String) -> Unit,
    updateCategoryName: () -> Unit,
    updateCategoryNameFieldError: Boolean,
    textFieldErrorMessage: String,
    updateCatNameLabel: String,
    buttonText: String,
    listType: ListType
) {
    ModalBottomSheetLayout(
        modifier = modifier.fillMaxSize(),
        sheetContent = {
            SheetContent(
                modifier = modifier,
                categoryNameVal = categoryNameVal,
                onCategoryNameChanged = onCategoryNameChanged,
                updateCategoryName = updateCategoryName,
                updateCategoryNameFieldError = updateCategoryNameFieldError,
                textFieldErrorMessage = textFieldErrorMessage,
                updateCatNameLabel = updateCatNameLabel,
                buttonText = buttonText
            )
        },
        sheetState = sheetState
    ) {
        if (getCategoriesState.data.isEmpty()) {
            NoCategoryMessage(modifier = modifier, emptyCategoryText = emptyCategoryText)
        } else {
            ResponsiveCategoryList(
                modifier = modifier,
                data = getCategoriesState.data,
                onDeleteClick = onDeleteClick,
                onCategoryCardClick = onCategoryCardClick,
                getCategories = getCategories,
                onEditClick = onEditClick,
                listType = listType
            )
        }
    }
}

@Composable
private fun ResponsiveCategoryList(
    modifier: Modifier,
    data: List<CategoryWithId>,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    onEditClick: (Int) -> Unit,
    listType: ListType
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(items = data, key = { it.categoryId }) {
                when(listType) {
                    ListType.RECTANGLE -> {
                        CategoryCard(
                            modifier = modifier,
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            height = LocalConfiguration.current.screenWidthDp.dp / 2,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick
                        )
                    }
                    ListType.THIN -> {
                        CategoryCard(
                            modifier = modifier,
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick
                        )
                    }
                }
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
            items(items = data, key = { it.categoryId }) {
                when(listType) {
                    ListType.RECTANGLE -> {
                        CategoryCard(
                            modifier = modifier,
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            height = LocalConfiguration.current.screenWidthDp.dp / 3,
                            width = LocalConfiguration.current.screenWidthDp.dp / 3,
                            getCategories = getCategories,
                            onEditClick = onEditClick
                        )
                    }
                    ListType.THIN -> {
                        CategoryCard(
                            modifier = modifier,
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick,
                            width = LocalConfiguration.current.screenWidthDp.dp / 3,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetContent(
    modifier: Modifier,
    categoryNameVal: String,
    onCategoryNameChanged: (String) -> Unit,
    updateCategoryName: () -> Unit,
    updateCategoryNameFieldError: Boolean,
    textFieldErrorMessage: String,
    updateCatNameLabel: String,
    buttonText: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        CustomTextField(
            modifier = modifier.fillMaxWidth(),
            value = categoryNameVal,
            onValueChange = { onCategoryNameChanged(it) },
            labelText = updateCatNameLabel,
            isError = updateCategoryNameFieldError,
            errorMessage = textFieldErrorMessage
        )
        CustomButton(
            modifier = modifier.padding(top = 16.dp),
            onClick = updateCategoryName,
            buttonText = buttonText
        )
    }
}

@Composable
private fun NoCategoryMessage(modifier: Modifier, emptyCategoryText: String) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emptyCategoryText, textAlign = TextAlign.Center)
    }
}