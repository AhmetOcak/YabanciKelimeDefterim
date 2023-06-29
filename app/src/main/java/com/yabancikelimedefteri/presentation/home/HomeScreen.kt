package com.yabancikelimedefteri.presentation.home

import android.content.res.Configuration
import android.content.res.Resources
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.HomeScreenFab
import com.yabancikelimedefteri.core.ui.component.CategoryCard
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.presentation.main.OrientationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateNext: (Int) -> Unit,
    resources: Resources
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val getCategoriesState by viewModel.getCategoriesState.collectAsState()
    val deleteCategoryState by viewModel.deleteCategoryState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = sheetState.isVisible) {
        coroutineScope.launch {
            HomeScreenFab.showFab.value = !sheetState.isVisible
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
            message = resources.getString(R.string.category_removed)
        )
        viewModel.resetDeleteWordState()
    } else if (deleteCategoryState is DeleteCategoryState.Error) {
        CustomToast(
            context = LocalContext.current,
            message = (deleteCategoryState as DeleteCategoryState.Error).message
        )
        viewModel.resetDeleteWordState()
    }

    HomeScreenContent(
        modifier = modifier,
        onDeleteClick = { viewModel.deleteCategories(it) },
        getCategoriesState = getCategoriesState,
        onCategoryCardClick = { onNavigateNext(it) },
        getCategories = { viewModel.getCategories() },
        emptyCategoryText = resources.getString(R.string.empty_category_message),
        sheetState = sheetState,
        onEditClick = {
            coroutineScope.launch {
                HomeScreenFab.showFab.value = false
                sheetState.show()
            }
        }
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
    onEditClick: (Int) -> Unit
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
                    onEditClick
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
    onEditClick: (Int) -> Unit
) {
    ModalBottomSheetLayout(
        modifier = modifier.fillMaxSize(),
        sheetContent = {
            SheetContent(modifier = modifier)
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
                onEditClick = onEditClick
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
    onEditClick: (Int) -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(items = data, key = { it.categoryId }) {
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
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = data, key = { it.categoryId }) {
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
        }
    }
}

@Composable
private fun SheetContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        CustomTextField(
            modifier = modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            labelText = "Yeni kategori ismi",
            errorMessage = ""
        )
        CustomButton(
            modifier = modifier.padding(top = 16.dp),
            onClick = { /*TODO*/ },
            buttonText = "Kaydet"
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