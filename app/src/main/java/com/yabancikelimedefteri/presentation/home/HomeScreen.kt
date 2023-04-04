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
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.core.ui.component.WordCard
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: HomeViewModel = hiltViewModel()
    val homeState by viewModel.homeState.collectAsState()
    val deleteWordState by viewModel.deleteWordState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    HomeScreenContent(
        modifier = modifier,
        onDeleteClick = { viewModel.deleteForeignWord(it) },
        homeState = homeState,
        deleteWordState = deleteWordState,
        resetDeleteWordState = { viewModel.resetDeleteWordState() }
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    onDeleteClick: (String) -> Unit,
    homeState: HomeState,
    deleteWordState: DeleteWordState,
    resetDeleteWordState: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (homeState) {
            is HomeState.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeState.Success -> {
                WordList(
                    deleteWordState,
                    homeState,
                    modifier,
                    onDeleteClick,
                    resetDeleteWordState
                )
            }
            is HomeState.Error -> {
                CustomToast(context = LocalContext.current, message = homeState.message)
            }
        }
    }
}

@Composable
private fun WordList(
    deleteWordState: DeleteWordState,
    homeState: HomeState.Success,
    modifier: Modifier,
    onDeleteClick: (String) -> Unit,
    resetDeleteWordState: () -> Unit
) {
    when (deleteWordState) {
        is DeleteWordState.Nothing -> {
            if (homeState.data.isNullOrEmpty()) {
                EmptyWordListMessage(modifier = modifier)
            } else {
                ResponsiveList(
                    modifier = modifier,
                    data = homeState.data,
                    onDeleteClick = onDeleteClick
                )
            }
        }
        is DeleteWordState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DeleteWordState.Success -> {
            CustomToast(context = LocalContext.current, message = "Kelime kaldırıldı")
            resetDeleteWordState()
        }
        is DeleteWordState.Error -> {
            CustomToast(context = LocalContext.current, message = deleteWordState.message)
        }
    }
}

@Composable
private fun ResponsiveList(
    modifier: Modifier,
    data: MutableMap<String, *>,
    onDeleteClick: (String) -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(data.keys.toList()) {
                WordCard(
                    modifier = modifier,
                    foreignWord = it,
                    meaning = data[it].toString(),
                    onDeleteClick = onDeleteClick
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data.keys.toList()) {
                WordCard(
                    modifier = modifier,
                    foreignWord = it,
                    meaning = data[it].toString(),
                    onDeleteClick = onDeleteClick,
                    height = LocalConfiguration.current.screenWidthDp.dp / 3
                )
            }
        }
    }
}

@Composable
private fun EmptyWordListMessage(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Kelime defterinde hiç kelime yok :(", textAlign = TextAlign.Center)
    }
}