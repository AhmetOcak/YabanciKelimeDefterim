package com.yabancikelimedefteri.presentation.home

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateNext: (Int) -> Unit,
    resources: Resources,
    listType: ListType
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val getCategoriesState by viewModel.getCategoriesState.collectAsState()
    val deleteCategoryState by viewModel.deleteCategoryState.collectAsState()
    val updateCategoryState by viewModel.updateCategoryState.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }

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
        onEditClick = {
            viewModel.updateSelectedCaId(it)
            HomeScreenFab.showFab.value = false
            showBottomSheet = true
        },

        listType = listType
    )

    if (showBottomSheet) {
        SheetContent(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            categoryNameVal = viewModel.newCategoryName,
            onCategoryNameChanged = { viewModel.updateNewCategoryName(it) },
            updateCategoryName = {
                viewModel.updateCategoryName(
                    viewModel.selectedCatId,
                    viewModel.newCategoryName
                )
                viewModel.resetNewCatName()
                showBottomSheet = false
                viewModel.getCategories()
            },
            updateCategoryNameFieldError = viewModel.newCategoryNameFieldError,
            textFieldErrorMessage = resources.getString(R.string.text_field_error),
            updateCatNameLabel = resources.getString(R.string.new_cat_name),
            buttonText = resources.getString(R.string.save),
        )
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    getCategoriesState: GetCategoriesState,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    emptyCategoryText: String,
    onEditClick: (Int) -> Unit,
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
                    getCategoriesState = getCategoriesState,
                    onDeleteClick = onDeleteClick,
                    onCategoryCardClick = onCategoryCardClick,
                    getCategories = getCategories,
                    emptyCategoryText = emptyCategoryText,
                    onEditClick = onEditClick,
                    listType = listType
                )
            }

            is GetCategoriesState.Error -> {
                CustomToast(context = LocalContext.current, message = getCategoriesState.message)
            }
        }
    }
}

@Composable
private fun CategoryList(
    getCategoriesState: GetCategoriesState.Success,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    emptyCategoryText: String,
    onEditClick: (Int) -> Unit,
    listType: ListType
) {
    if (getCategoriesState.data.isEmpty()) {
        NoCategoryMessage(emptyCategoryText = emptyCategoryText)
    } else {
        ResponsiveCategoryList(
            data = getCategoriesState.data,
            onDeleteClick = onDeleteClick,
            onCategoryCardClick = onCategoryCardClick,
            getCategories = getCategories,
            onEditClick = onEditClick,
            listType = listType
        )
    }
}

@Composable
private fun ResponsiveCategoryList(
    data: List<CategoryWithId>,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    onEditClick: (Int) -> Unit,
    listType: ListType
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(items = data, key = { it.categoryId }) {
                when (listType) {
                    ListType.RECTANGLE -> {
                        CategoryCard(
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenHeightDp.dp / 2),
                            isCatCardThin = false
                        )
                    }

                    ListType.THIN -> {
                        CategoryCard(
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            isCatCardThin = true
                        )
                    }
                }
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = data, key = { it.categoryId }) {
                when (listType) {
                    ListType.RECTANGLE -> {
                        CategoryCard(
                            modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3),
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick,
                            isCatCardThin = false
                        )
                    }

                    ListType.THIN -> {
                        CategoryCard(
                            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 3),
                            categoryName = it.categoryName,
                            categoryId = it.categoryId,
                            onDeleteClick = onDeleteClick,
                            onCategoryCardClick = onCategoryCardClick,
                            getCategories = getCategories,
                            onEditClick = onEditClick,
                            isCatCardThin = true
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
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = categoryNameVal,
            onValueChange = { onCategoryNameChanged(it) },
            labelText = updateCatNameLabel,
            isError = updateCategoryNameFieldError,
            errorMessage = textFieldErrorMessage
        )
        CustomButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = updateCategoryName,
            buttonText = buttonText
        )
    }
}

@Composable
private fun NoCategoryMessage(emptyCategoryText: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emptyCategoryText, textAlign = TextAlign.Center)
    }
}