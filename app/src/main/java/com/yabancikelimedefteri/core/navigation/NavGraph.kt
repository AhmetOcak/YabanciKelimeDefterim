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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.helpers.saveTheme
import com.yabancikelimedefteri.core.ui.theme.ThemeState
import com.yabancikelimedefteri.presentation.add_word.AddWordScreen
import com.yabancikelimedefteri.presentation.game.GameScreen
import com.yabancikelimedefteri.presentation.home.HomeScreen

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (showFab) {
                Fab(
                    modifier = modifier,
                    onClick = {
                        navController.navigate(NavScreen.AddWordScreen.route)
                        pageTitle = PageTitles.add_word
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
                    val theme = when(sharedPreferences.getCurrentTheme()) {
                        1 -> AppCompatDelegate.MODE_NIGHT_YES
                        2 -> AppCompatDelegate.MODE_NIGHT_NO
                        else -> { -1 }
                    }
                    sharedPreferences.edit {
                        saveTheme(theme)
                    }
                    AppCompatDelegate.setDefaultNightMode(theme)
                    ThemeState.isDark.value = sharedPreferences.getCurrentTheme() == 2
                },
                onBackClick = {
                    navController.navigate(NavScreen.HomeScreen.route)
                    pageTitle = PageTitles.home
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
                HomeScreen(onNavigateBack = { activity.finish() })
            }
            composable(route = NavScreen.AddWordScreen.route) {
                showFab = false
                AddWordScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = PageTitles.home
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