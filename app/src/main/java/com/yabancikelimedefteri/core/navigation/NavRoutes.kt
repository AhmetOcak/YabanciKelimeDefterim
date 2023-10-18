package com.yabancikelimedefteri.core.navigation

object NavRoutes {
    const val home_screen = "home_screen"
    const val add_word_screen = "${NavNames.add_word_screen}/{categoryId}"
    const val game_screen = "game_screen"
    const val add_category_screen = "add_category_screen"
    const val word_screen = "${NavNames.word_screen}/{categoryId}"
    const val dictionary_screen = "dictionary_screen"
}

object NavNames {
    const val word_screen = "word_screen"
    const val add_word_screen = "add_word_screen"
}