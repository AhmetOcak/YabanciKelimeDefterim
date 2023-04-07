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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.core.ui.component.CategoryCard
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateNext: (Int) -> Unit
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val getCategoriesState by viewModel.getCategoriesState.collectAsState()
    val deleteCategoryState by viewModel.deleteCategoryState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    if (deleteCategoryState is DeleteCategoryState.Success) {
        CustomToast(context = LocalContext.current, message = "Kategori kaldırıldı")
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
        getCategories = { viewModel.getCategories() }
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    getCategoriesState: GetCategoriesState,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit
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
                    getCategories
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
    modifier: Modifier,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit
) {
    if (getCategoriesState.data.isEmpty()) {
        NoCategoryMessage(modifier = modifier)
    } else {
        ResponsiveCategoryList(
            modifier = modifier,
            data = getCategoriesState.data,
            onDeleteClick = onDeleteClick,
            onCategoryCardClick = onCategoryCardClick,
            getCategories = getCategories
        )
    }
}

@Composable
private fun ResponsiveCategoryList(
    modifier: Modifier,
    data: List<CategoryWithId>,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit
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
                    getCategories = getCategories
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
                    getCategories = getCategories
                )
            }
        }
    }
}

@Composable
private fun NoCategoryMessage(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Kelime defterinde hiç kategori yok 😥", textAlign = TextAlign.Center)
    }
}