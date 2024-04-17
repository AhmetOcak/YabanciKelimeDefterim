package com.yabancikelimedefteri.presentation.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigateToRoute: (String) -> Unit) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.word_game)) })
        },
        bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.GAMES.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        GameScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun GameScreenContent(modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {

    }
}