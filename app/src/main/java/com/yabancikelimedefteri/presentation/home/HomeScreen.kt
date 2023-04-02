package com.yabancikelimedefteri.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.ui.component.WordCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    BackHandler {
        onNavigateBack()
    }

    HomeScreenContent(modifier = modifier)
}

@Composable
private fun HomeScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(10) {
                WordCard(modifier = modifier, foreignWord = "love", meaning = "aşk")
            }
        }
    }
}