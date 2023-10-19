package com.yabancikelimedefteri.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.HomeScreenFab
import com.yabancikelimedefteri.core.helpers.getCurrentCatListType
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.helpers.getCurrentWordListType
import com.yabancikelimedefteri.core.helpers.saveNewCatListType
import com.yabancikelimedefteri.core.helpers.saveNewWordListType
import com.yabancikelimedefteri.core.helpers.saveTheme
import com.yabancikelimedefteri.core.ui.component.OverflowMenu
import com.yabancikelimedefteri.core.ui.theme.ThemeState
import com.yabancikelimedefteri.presentation.add_category.AddCategoryScreen
import com.yabancikelimedefteri.presentation.add_word.AddWordScreen
import com.yabancikelimedefteri.presentation.dictionary.DictionaryScreen
import com.yabancikelimedefteri.presentation.game.GameScreen
import com.yabancikelimedefteri.presentation.home.HomeScreen
import com.yabancikelimedefteri.presentation.word.WordScreen

// if true -> ListType.Rectangle
// else -> ListType.Thin
const val WORD_LIST_TYPE_KEY = "word_list_type"
const val CAT_LIST_TYPE_KEY = "cat_list_type"

enum class ListType {
    THIN,
    RECTANGLE
}

@SuppressLint("CommitPrefEdits")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = NavScreen.HomeScreen.route,
    activity: Activity,
    sharedPreferences: SharedPreferences,
    resources: Resources
) {
    val navController = rememberAnimatedNavController()

    var pageTitle by rememberSaveable { mutableStateOf(resources.getString(R.string.app_name)) }

    var showFab by rememberSaveable { mutableStateOf(true) }

    var wordListType by rememberSaveable {
        mutableStateOf(
            if (sharedPreferences.getCurrentWordListType()) {
                ListType.RECTANGLE
            } else {
                ListType.THIN
            }
        )
    }

    var catListType by rememberSaveable {
        mutableStateOf(
            if (sharedPreferences.getCurrentCatListType()) {
                ListType.RECTANGLE
            } else {
                ListType.THIN
            }
        )
    }

    var categoryId: Int? = null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = {
            if (showFab) {
                Fab(
                    onClick = {
                        if (
                            navController.currentBackStackEntry?.destination?.route != null
                            &&
                            navController.currentBackStackEntry?.destination?.route == NavScreen.HomeScreen.route
                        ) {
                            navController.navigate(NavScreen.AddCategoryScreen.route)
                            pageTitle = resources.getString(R.string.create_category)
                        } else {
                            categoryId?.let {
                                navController.navigate("${NavNames.add_word_screen}/$it")
                                pageTitle = resources.getString(R.string.add_word)
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
                    pageTitle = resources.getString(R.string.word_guessing_game)
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
                        pageTitle = resources.getString(R.string.app_name)
                    } else {
                        navController.navigate("${NavNames.word_screen}/$categoryId")
                        pageTitle = resources.getString(R.string.my_words)
                    }
                },
                pageTitle = pageTitle,
                resources = resources,
                onDictClick = {
                    navController.navigate(NavScreen.DictionaryScreen.route)
                },
                onListTypeClick = {
                    when (wordListType) {
                        ListType.RECTANGLE -> {
                            wordListType = ListType.THIN
                            sharedPreferences.edit().saveNewWordListType(ListType.THIN)
                        }

                        ListType.THIN -> {
                            wordListType = ListType.RECTANGLE
                            sharedPreferences.edit().saveNewWordListType(ListType.RECTANGLE)
                        }
                    }
                },
                listType = wordListType,
                onChangeCatListType = {
                    when (catListType) {
                        ListType.RECTANGLE -> {
                            catListType = ListType.THIN
                            sharedPreferences.edit().saveNewCatListType(ListType.THIN)
                        }

                        ListType.THIN -> {
                            catListType = ListType.RECTANGLE
                            sharedPreferences.edit().saveNewCatListType(ListType.RECTANGLE)
                        }
                    }
                },
                catListType = catListType
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
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(route = NavScreen.HomeScreen.route) {
                showFab = HomeScreenFab.showFab.value
                HomeScreen(
                    onNavigateBack = { activity.finish() },
                    onNavigateNext = { id ->
                        categoryId = id
                        navController.navigate("${NavNames.word_screen}/$id")
                        pageTitle = resources.getString(R.string.my_words)
                    },
                    resources = resources,
                    listType = catListType
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
                        pageTitle = resources.getString(R.string.my_words)
                    }
                )
            }
            composable(route = NavScreen.GameScreen.route) {
                showFab = false
                GameScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = resources.getString(R.string.app_name)
                    }
                )
            }
            composable(route = NavScreen.AddCategoryScreen.route) {
                showFab = false
                AddCategoryScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = resources.getString(R.string.app_name)
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
                        pageTitle = resources.getString(R.string.app_name)
                    },
                    resources = resources,
                    listType = wordListType
                )
            }
            composable(route = NavScreen.DictionaryScreen.route) {
                showFab = false
                DictionaryScreen(
                    onNavigateBack = {
                        navController.navigate(NavScreen.HomeScreen.route)
                        pageTitle = resources.getString(R.string.app_name)
                    }
                )
            }
        }
    }
}

@Composable
private fun Fab(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun TopBar(
    onGameClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onDictClick: () -> Unit,
    onBackClick: () -> Unit,
    onListTypeClick: () -> Unit,
    onChangeCatListType: () -> Unit,
    listType: ListType,
    pageTitle: String,
    resources: Resources,
    catListType: ListType
) {
    TopAppBar(
        title = {
            Text(text = pageTitle)
        },
        actions = {
            if (pageTitle == resources.getString(R.string.app_name)) {
                HomeScreenTopAppBar(
                    onDictClick,
                    resources,
                    onDarkModeClick,
                    onGameClick,
                    onChangeCatListType,
                    catListType
                )
            } else if (pageTitle == resources.getString(R.string.my_words)) {
                IconButton(onClick = onListTypeClick) {
                    Icon(
                        painter = painterResource(
                            id = when (listType) {
                                ListType.RECTANGLE -> {
                                    R.drawable.ic_rectangle
                                }

                                ListType.THIN -> {
                                    R.drawable.ic_line
                                }
                            }
                        ),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        navigationIcon = if (pageTitle != resources.getString(R.string.app_name)) {
            {
                GoBackScreen(onClick = onBackClick)
            }
        } else null
    )
}

@Composable
private fun HomeScreenTopAppBar(
    onDictClick: () -> Unit,
    resources: Resources,
    onDarkModeClick: () -> Unit,
    onGameClick: () -> Unit,
    onChangeCatListType: () -> Unit,
    catListType: ListType
) {
    IconButton(onClick = onDictClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_dictionary),
            contentDescription = resources.getString(R.string.content_open_dict),
            tint = Color.White
        )
    }
    IconButton(onClick = onDarkModeClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_dark_mode),
            contentDescription = null,
            tint = Color.White
        )
    }
    OverflowMenu {
        DropdownMenuItem(onClick = onGameClick) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxHeight(),
                    painter = painterResource(id = R.drawable.ic_baseline_games),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = resources.getString(R.string.word_game),
                    style = TextStyle(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
        DropdownMenuItem(onClick = onChangeCatListType) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxHeight(),
                    painter = painterResource(
                        id = when(catListType) {
                            ListType.RECTANGLE -> {
                                R.drawable.ic_rectangle
                            }
                            ListType.THIN -> {
                                R.drawable.ic_line
                            }
                        }
                    ),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = resources.getString(R.string.appearance),
                    style = TextStyle(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun GoBackScreen(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}