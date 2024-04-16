package com.yabancikelimedefteri.core.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yabancikelimedefteri.presentation.dictionary.DictionaryScreen
import com.yabancikelimedefteri.presentation.game.GameScreen
import com.yabancikelimedefteri.presentation.word_categories.WordCategoriesScreen

fun NavGraphBuilder.addHomeGraph(
    onNavigateToRoute: (String) -> Unit,
    onCategoryClick: (Int, NavBackStackEntry) -> Unit
) {
    composable(HomeSections.DICTIONARY.route) {
        DictionaryScreen(onNavigateToRoute = onNavigateToRoute)
    }
    composable(HomeSections.CATEGORIES.route) { from ->
        WordCategoriesScreen(
            onCategoryClick = remember { { id -> onCategoryClick(id, from) } },
            onNavigateToRoute = onNavigateToRoute
        )
    }
    composable(HomeSections.GAMES.route) {
        GameScreen(onNavigateToRoute = onNavigateToRoute)
    }
}