package com.yabancikelimedefteri.core.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yabancikelimedefteri.core.ui.theme.YabanciKelimeDefteriTheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.CustomColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.SeaColorScheme
import com.yabancikelimedefteri.presentation.game.games.pairing.PairingGameScreen
import com.yabancikelimedefteri.presentation.game.games.quiz.QuizGameScreen
import com.yabancikelimedefteri.presentation.game.games.writing.WritingGameScreen
import com.yabancikelimedefteri.presentation.word.WordScreen

@Composable
fun MyVocabularyApp(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    customColorScheme: CustomColorScheme = SeaColorScheme
) {
    YabanciKelimeDefteriTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        customColorScheme = customColorScheme
    ) {
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
                    navigateToQuizGame = appNavController::navigateQuizGame,
                    navigateToWritingGame = appNavController::navigateWritingGame,
                    navigateToPairingGame = appNavController::navigatePairingGame
                )
            }
        }
    }
}

private fun NavGraphBuilder.appNavGraph(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    onCategoryClick: (Int, NavBackStackEntry) -> Unit,
    navigateToQuizGame: (NavBackStackEntry) -> Unit,
    navigateToWritingGame: (NavBackStackEntry) -> Unit,
    navigateToPairingGame: (NavBackStackEntry) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.CATEGORIES.route
    ) {
        addHomeGraph(
            onNavigateToRoute = onNavigateToRoute,
            onCategoryClick = onCategoryClick,
            navigateToQuizGame = navigateToQuizGame,
            navigateToWritingGame = navigateToWritingGame,
            navigateToPairingGame = navigateToPairingGame
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
    composable(route = MainDestinations.QUIZ_GAME_ROUTE) {
        QuizGameScreen(upPress = upPress)
    }
    composable(route = MainDestinations.WRITING_GAME_ROUTE) {
        WritingGameScreen(upPress = upPress)
    }
    composable(route = MainDestinations.PAIRING_GAME_ROUTE) {
        PairingGameScreen(upPress = upPress)
    }
}