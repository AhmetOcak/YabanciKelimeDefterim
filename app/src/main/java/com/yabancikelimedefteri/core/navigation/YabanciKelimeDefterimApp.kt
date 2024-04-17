package com.yabancikelimedefteri.core.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yabancikelimedefteri.core.ui.theme.YabanciKelimeDefteriTheme
import com.yabancikelimedefteri.presentation.game.games.quiz.QuizGameScreen
import com.yabancikelimedefteri.presentation.word.WordScreen

@Composable
fun MyVocabularyApp(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false
) {
    YabanciKelimeDefteriTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        val appNavController = rememberAppNavController()

        Surface {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = appNavController.navController,
                startDestination = MainDestinations.HOME_ROUTE
            ) {
                appNavGraph(
                    onNavigateToRoute = appNavController::navigateToNavigationBar,
                    upPress = appNavController::upPress,
                    onCategoryClick = appNavController::navigateWords,
                    onReturnGamesScreenClick = {}
                )
            }
        }
    }
}

private fun NavGraphBuilder.appNavGraph(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    onCategoryClick: (Int, NavBackStackEntry) -> Unit,
    onReturnGamesScreenClick: (NavBackStackEntry) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.CATEGORIES.route
    ) {
        addHomeGraph(
            onNavigateToRoute = onNavigateToRoute,
            onCategoryClick = onCategoryClick
        )
    }
    composable(
        route = "${MainDestinations.WORDS_ROUTE}/{${MainDestinations.WORD_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.WORD_ID_KEY) { NavType.IntType }
        )
    ) {
        WordScreen(upPress = upPress)
    }
    composable(route = MainDestinations.QUIZ_GAME_ROUTE) { navBackStackEntry ->
        QuizGameScreen(
            upPress = upPress,
            onReturnGamesScreenClick = remember { { onReturnGamesScreenClick(navBackStackEntry) } })
    }
}