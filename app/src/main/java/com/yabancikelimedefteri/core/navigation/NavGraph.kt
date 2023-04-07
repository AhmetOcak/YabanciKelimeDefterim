package com.yabancikelimedefteri.core.navigation

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.helpers.saveTheme
import com.yabancikelimedefteri.core.ui.theme.ThemeState
import com.yabancikelimedefteri.presentation.add_category.AddCategoryScreen
import com.yabancikelimedefteri.presentation.add_word.AddWordScreen
import com.yabancikelimedefteri.presentation.game.GameScreen
import com.yabancikelimedefteri.presentation.home.HomeScreen
import com.yabancikelimedefteri.presentation.word.WordScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = NavScreen.HomeScreen.route,
    activity: Activity,
    sharedPreferences: SharedPreferences
) {
    val navController = rememberAnimatedNavController()

    var pageTitle by rememberSaveable { mutableStateOf(PageTitles.home) }

    var showFab by rememberSaveable { mutableStateOf(true) }

    var categoryId: Int? = null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = {
            if (showFab) {
                Fab(
                    modifier = modifier,
                    onClick = {
                        if (
                            navController.currentBackStackEntry?.destination?.route != null
                            &&
                            navController.currentBackStackEntry?.destination?.route == NavScreen.HomeScreen.route
                        ) {
                            navController.navigate(NavScreen.AddCategoryScreen.route)
                            pageTitle = PageTitles.add_category
                        } else {
                            categoryId?.let {
                                navController.navigate("${NavNames.add_word_screen}/$it")
                                pageTitle = PageTitles.add_word
                            }
                        }
                    }
                )
            }
        },
        topBar = {
            TopBar(
                onGameClick = {
                    navController.navigate(NavScreen.GameScreen.route)
                    pageTitle = PageTitles.game
                },
                onDarkModeClick = {
                    // eğer tema kodu 1 ise tam karşıtı yapılır
                    val theme = when (sharedPreferences.getCurrentTheme()) {
                        1 -> AppCompatDelegate.MODE_NIGHT_YES
                        2 -> AppCompatDelegate.MODE_NIGHT_NO
                        else -> {
                            -1
                        }
                    }
                    sharedPreferences.edit {
                        saveTheme(theme)
                    }
                    AppCompatDelegate.setDefaultNightMode(theme)
                    ThemeState.isDark.value = sharedPreferences.getCurrentTheme() == 2
                },
                onBackClick = {
                    if (navController.currentBackStackEntry?.destination?.route != NavScreen.AddWordScreen.route) {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = PageTitles.home
                    } else {
                        navController.navigate("${NavNames.word_screen}/$categoryId")
                        pageTitle = PageTitles.word
                    }
                },
                pageTitle = pageTitle
            )
        }
    ) {
        AnimatedNavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            composable(route = NavScreen.HomeScreen.route) {
                showFab = true
                HomeScreen(
                    onNavigateBack = { activity.finish() },
                    onNavigateNext = { id ->
                        categoryId = id
                        navController.navigate("${NavNames.word_screen}/$id")
                        pageTitle = PageTitles.word
                    }
                )
            }
            composable(
                route = NavScreen.AddWordScreen.route,
                arguments = listOf(
                    navArgument("categoryId") { NavType.IntType }
                )
            ) {
                showFab = false
                AddWordScreen(
                    onNavigateBack = {
                        navController.navigate("${NavNames.word_screen}/$categoryId")
                        pageTitle = PageTitles.word
                    }
                )
            }
            composable(route = NavScreen.GameScreen.route) {
                showFab = false
                GameScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = PageTitles.home
                    }
                )
            }
            composable(route = NavScreen.AddCategoryScreen.route) {
                showFab = false
                AddCategoryScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = PageTitles.home
                    }
                )
            }
            composable(
                route = NavScreen.WordScreen.route,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType }
                )
            ) {
                showFab = true
                WordScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = PageTitles.home
                    }
                )
            }
        }
    }
}

@Composable
private fun Fab(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "kelime ekle"
        )
    }
}

@Composable
private fun TopBar(
    onGameClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onBackClick: () -> Unit,
    pageTitle: String
) {
    TopAppBar(
        title = {
            Text(text = pageTitle)
        },
        actions = {
            if (pageTitle == PageTitles.home) {
                IconButton(onClick = onDarkModeClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_dark_mode),
                        contentDescription = "Karanlık mod açıksa kapat, kapalıysa aç",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onGameClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_games),
                        contentDescription = "Kelime tahmin oyunu sayfasını aç",
                        tint = Color.White
                    )
                }
            }
        },
        navigationIcon = if (pageTitle != PageTitles.home) {
            {
                GoBackScreen(onClick = onBackClick)
            }
        } else null
    )
}

@Composable
private fun GoBackScreen(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Bir önceki sayfaya git"
        )
    }
}