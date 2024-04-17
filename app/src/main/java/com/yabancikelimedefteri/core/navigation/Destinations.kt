package com.yabancikelimedefteri.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Games
import androidx.compose.ui.graphics.vector.ImageVector
import com.yabancikelimedefteri.R

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val WORDS_ROUTE = "word"
    const val WORD_ID_KEY = "word_id"
    const val QUIZ_GAME_ROUTE = "quiz_game"
    const val WRITING_GAME_ROUTE = "writing_game"
    const val PAIRING_GAME_ROUTE = "pairing_game"
}

enum class HomeSections(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    DICTIONARY(R.string.dict, Icons.Filled.Book, Icons.Outlined.Book, "home/dictionary"),
    CATEGORIES(R.string.categories, Icons.Filled.Category, Icons.Outlined.Category, "home/categories"),
    GAMES(R.string.games, Icons.Filled.Games, Icons.Outlined.Games, "home/games"),
}