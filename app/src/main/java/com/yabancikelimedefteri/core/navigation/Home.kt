package com.yabancikelimedefteri.core.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yabancikelimedefteri.core.ui.theme.color_schemes.CustomColorScheme
import com.yabancikelimedefteri.presentation.dictionary.DictionaryScreen
import com.yabancikelimedefteri.presentation.game.GamesScreen
import com.yabancikelimedefteri.presentation.settings.SettingsScreen
import com.yabancikelimedefteri.presentation.word_categories.WordCategoriesScreen

fun NavGraphBuilder.addHomeGraph(
    onNavigateToRoute: (String) -> Unit,
    onCategoryClick: (Int, NavBackStackEntry) -> Unit,
    navigateToQuizGame: (NavBackStackEntry) -> Unit,
    navigateToWritingGame: (NavBackStackEntry) -> Unit,
    navigateToPairingGame: (NavBackStackEntry) -> Unit,
    isDarkThemeChecked: Boolean,
    isDynamicColorChecked: Boolean,
    isThinListTypeChecked: Boolean,
    currentScheme: CustomColorScheme
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
    composable(HomeSections.GAMES.route) { from ->
        GamesScreen(
            onNavigateToRoute = onNavigateToRoute,
            navigateToQuizGame = remember { { navigateToQuizGame(from) } },
            navigateToWritingGame = remember { { navigateToWritingGame(from) } },
            navigateToPairingGame = remember { { navigateToPairingGame(from) } }
        )
    }
    composable(HomeSections.SETTINGS.route) {
        SettingsScreen(
            onNavigateToRoute = onNavigateToRoute,
            isDarkThemeChecked = isDarkThemeChecked,
            isDynamicColorChecked = isDynamicColorChecked,
            isThinListTypeChecked = isThinListTypeChecked,
            currentScheme = currentScheme
        )
    }
}