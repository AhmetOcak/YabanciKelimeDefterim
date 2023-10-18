package com.yabancikelimedefteri.core.navigation

sealed class NavScreen(val route: String) {
    object HomeScreen : NavScreen(route = NavRoutes.home_screen)
    object AddWordScreen : NavScreen(route = NavRoutes.add_word_screen)
    object GameScreen : NavScreen(route = NavRoutes.game_screen)
    object AddCategoryScreen : NavScreen(route = NavRoutes.add_category_screen)
    object WordScreen : NavScreen(route = NavRoutes.word_screen)
    object DictionaryScreen : NavScreen(route = NavRoutes.dictionary_screen)
}