package com.yabancikelimedefteri.presentation.game

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

// TODO: Hata mesajları yazılacak

@Composable
fun GameScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: GameViewModel = hiltViewModel()

    val gameState by viewModel.gameState.collectAsState()
    val categoriesState by viewModel.categoriesState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    GameScreenContent(
        modifier = modifier,
        gameState = gameState,
        wordIndex = viewModel.wordIndex,
        onGuessClicked = {
            if (viewModel.isGuessWordReadyForSubmit()) {
                viewModel.addAnswer(it)
                viewModel.resetGuessWord()
                viewModel.incWordIndex()
            }
            if (!viewModel.isGameStillGoing()) {
                viewModel.calculateResult()
            }
        },
        onValueChanged = { viewModel.updateGuessWord(it) },
        value = viewModel.guessWord,
        isError = viewModel.guessWordFieldError,
        isGameOver = !viewModel.isGameStillGoing(),
        answers = viewModel.answers,
        words = viewModel.words ?: mutableListOf(),
        correctCount = viewModel.correctAnswerCount,
        inCorrectCount = viewModel.inCorrectAnswerCount,
        sharedPreferences = LocalContext.current.getSharedPreferences(
            "current_theme",
            Context.MODE_PRIVATE
        ),
        isCurrentThemeDark = LocalContext.current.getSharedPreferences(
            "current_theme",
            Context.MODE_PRIVATE
        ).getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_YES,
        setAllCateSelected = { viewModel.setAllCategorySelect(it) },
        isAllCatSelected = viewModel.isAllCategorySelected,
        categoriesState = categoriesState,
        addAllCategory = { viewModel.addAllCategories() },
        addSelectedCategory = { viewModel.addSelectedCategory(it) },
        removeSelectedCategory = { viewModel.removeSelectedCategory(it) },
        removeAllCategory = { viewModel.removeAllCategories() },
        isButtonEnabled = viewModel.isGameReadyToLaunch,
        launchTheGame = { viewModel.launchTheGame() }
    )
}

@Composable
private fun GameScreenContent(
    modifier: Modifier,
    gameState: GameState,
    wordIndex: Int,
    onGuessClicked: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    value: String,
    isError: Boolean,
    isGameOver: Boolean,
    answers: MutableMap<String, String>,
    words: List<WordWithId>,
    correctCount: Int,
    inCorrectCount: Int,
    sharedPreferences: SharedPreferences,
    isCurrentThemeDark: Boolean,
    setAllCateSelected: (Boolean) -> Unit,
    isAllCatSelected: Boolean,
    categoriesState: GetGameCategoriesState,
    addAllCategory: () -> Unit,
    addSelectedCategory: (Int) -> Unit,
    removeSelectedCategory: (Int) -> Unit,
    removeAllCategory: () -> Unit,
    isButtonEnabled: Boolean,
    launchTheGame: () -> Unit
) {
    when (gameState) {
        is GameState.Nothing -> {
            when (categoriesState) {
                is GetGameCategoriesState.Loading -> {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is GetGameCategoriesState.Success -> {
                    ChooseGameCategorySection(
                        modifier = modifier,
                        setAllCateSelected = setAllCateSelected,
                        isAllCatSelected = isAllCatSelected,
                        categories = categoriesState.data,
                        addAllCategory = addAllCategory,
                        addSelectedCategory = addSelectedCategory,
                        removeSelectedCategory = removeSelectedCategory,
                        removeAllCategory = removeAllCategory,
                        isButtonEnabled = isButtonEnabled,
                        launchTheGame = launchTheGame
                    )
                }
                is GetGameCategoriesState.Error -> {
                    CustomToast(context = LocalContext.current, message = categoriesState.message)
                }
            }
        }
        is GameState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is GameState.Success -> {
            if (!isGameOver) {
                GameSection(
                    modifier = modifier,
                    gameState = gameState,
                    wordIndex = wordIndex,
                    value = value,
                    onValueChanged = onValueChanged,
                    isError = isError,
                    onGuessClicked = onGuessClicked
                )
            } else {
                GameResultSection(
                    modifier = modifier,
                    answers = answers,
                    words = words,
                    correctCount = correctCount,
                    inCorrectCount = inCorrectCount,
                    sharedPreferences = sharedPreferences,
                    isCurrentThemeDark = isCurrentThemeDark
                )
            }
        }
        is GameState.Error -> {
            CustomToast(context = LocalContext.current, message = gameState.message)
        }
    }
}

@Composable
private fun ChooseGameCategorySection(
    modifier: Modifier,
    setAllCateSelected: (Boolean) -> Unit,
    isAllCatSelected: Boolean,
    categories: List<CategoryWithId>,
    addAllCategory: () -> Unit,
    addSelectedCategory: (Int) -> Unit,
    removeSelectedCategory: (Int) -> Unit,
    removeAllCategory: () -> Unit,
    isButtonEnabled: Boolean,
    launchTheGame: () -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = "Oyunu oynamak istediğin kategoriyi seç",
                textAlign = TextAlign.Center
            )
            Space(modifier = modifier.height(16.dp))
            Space(
                modifier = modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.primary)
            )
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenWidthDp.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) {
                    GameCategory(
                        modifier = modifier,
                        categoryName = it.categoryName,
                        isAllCatSelected = isAllCatSelected,
                        categoryId = it.categoryId,
                        addSelectedCategory = addSelectedCategory,
                        removeSelectedCategory = removeSelectedCategory
                    )
                }
                item {
                    GameCategory(
                        modifier = modifier,
                        categoryName = "Hepsi",
                        allClicked = {
                            setAllCateSelected(it)
                            if (it) {
                                addAllCategory()
                            } else {
                                removeAllCategory()
                            }
                        },
                        isAllCatSelected = isAllCatSelected,
                        categoryId = -1,
                        addSelectedCategory = addSelectedCategory,
                        removeSelectedCategory = removeSelectedCategory
                    )
                }
            }
            Space(
                modifier = modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.primary)
            )
            Space(modifier = modifier.height(16.dp))
            CustomButton(
                modifier = modifier,
                onClick = launchTheGame,
                buttonText = "Oyunu Başlat",
                enabled = isButtonEnabled
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = modifier.weight(1f)) {
                LazyVerticalGrid(
                    modifier = modifier.height(LocalConfiguration.current.screenWidthDp.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) {
                        GameCategory(
                            modifier = modifier,
                            categoryName = it.categoryName,
                            isAllCatSelected = isAllCatSelected,
                            categoryId = it.categoryId,
                            addSelectedCategory = addSelectedCategory,
                            removeSelectedCategory = removeSelectedCategory
                        )
                    }
                    item {
                        GameCategory(
                            modifier = modifier,
                            categoryName = "Hepsi",
                            allClicked = {
                                setAllCateSelected(it)
                                if (it) {
                                    addAllCategory()
                                } else {
                                    removeAllCategory()
                                }
                            },
                            isAllCatSelected = isAllCatSelected,
                            categoryId = -1,
                            addSelectedCategory = addSelectedCategory,
                            removeSelectedCategory = removeSelectedCategory
                        )
                    }
                }
            }
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Oyunu oynamak istediğin kategoriyi seç",
                    textAlign = TextAlign.Center
                )
                Space(modifier = modifier.height(16.dp))
                CustomButton(
                    modifier = modifier,
                    onClick = launchTheGame,
                    buttonText = "Oyunu Başlat",
                    enabled = isButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun GameCategory(
    modifier: Modifier,
    categoryName: String,
    categoryId: Int,
    allClicked: (Boolean) -> Unit = {},
    isAllCatSelected: Boolean,
    addSelectedCategory: (Int) -> Unit,
    removeSelectedCategory: (Int) -> Unit
) {
    var clicked by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .size(96.dp)
            .clickable(
                enabled = if (categoryName == "Hepsi") {
                    true
                } else !isAllCatSelected,
                onClick = {
                    clicked = !clicked
                    if (categoryName == "Hepsi") {
                        allClicked(clicked)
                    } else {
                        if (clicked) {
                            addSelectedCategory(categoryId)
                        } else {
                            removeSelectedCategory(categoryId)
                        }
                    }
                }
            ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        ),
        shape = RoundedCornerShape(10),
        backgroundColor = if (isAllCatSelected)
            MaterialTheme.colors.secondary.copy(alpha = 0.5f)
        else if (clicked) {
            MaterialTheme.colors.secondary.copy(alpha = 0.5f)
        } else
            MaterialTheme.colors.surface
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GameResultSection(
    modifier: Modifier,
    answers: MutableMap<String, String>,
    words: List<WordWithId>,
    correctCount: Int,
    inCorrectCount: Int,
    sharedPreferences: SharedPreferences,
    isCurrentThemeDark: Boolean
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameResultSubTitle(
            modifier = modifier,
            correctCount = correctCount,
            inCorrectCount = inCorrectCount
        )
        InfoAboutResultTable(modifier = modifier)
        GameResultTableHeaders(
            modifier = modifier,
            tableCellTextColor = if (
                sharedPreferences.getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_YES
            ) Color.White
            else
                Color.Black,
            isCurrentThemeDark = isCurrentThemeDark
        )
        GameResultTable(
            modifier = modifier,
            answers = answers,
            words = words,
            tableCellTextColor = if (
                sharedPreferences.getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_YES
            ) Color.White
            else
                Color.Black,
            isCurrentThemeDark = isCurrentThemeDark
        )
    }
}

@Composable
private fun GameResultTable(
    modifier: Modifier,
    answers: MutableMap<String, String>,
    words: List<WordWithId>,
    tableCellTextColor: Color,
    isCurrentThemeDark: Boolean
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(answers.keys.toList()) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    modifier = modifier,
                    text = it,
                    textColor = tableCellTextColor,
                    isCurrentThemeDark = isCurrentThemeDark
                )
                TableCell(
                    modifier = modifier,
                    text = answers[it] ?: "",
                    textColor = if (
                        (words.find { word ->
                            word.foreignWord == it
                        }?.meaning?.uppercase() ?: "")
                        ==
                        (answers[it]?.uppercase() ?: "")
                    ) Color.Green else Color.Red,
                    isCurrentThemeDark = isCurrentThemeDark
                )
            }
        }
    }
}

@Composable
private fun GameResultTableHeaders(
    modifier: Modifier,
    tableCellTextColor: Color,
    isCurrentThemeDark: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(
            modifier = modifier,
            text = "Doğru Cevap",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false,
            textColor = tableCellTextColor,
            isCurrentThemeDark = isCurrentThemeDark
        )
        TableCell(
            modifier = modifier,
            text = "Senin Cevabın",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false,
            textColor = tableCellTextColor,
            isCurrentThemeDark = isCurrentThemeDark
        )
    }
}

@Composable
private fun InfoAboutResultTable(modifier: Modifier) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Green)) {
                append("Doğru ")
            }
            append("cevaplar yeşil, ")
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("yanlış ")
            }
            append("cevaplar kırmızı renkte gösterilmiştir.")
        },
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun GameResultSubTitle(modifier: Modifier, correctCount: Int, inCorrectCount: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Oyun Sonucun:",
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            modifier = modifier.padding(start = 4.dp),
            text = "$correctCount doğru $inCorrectCount yanlış",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun RowScope.TableCell(
    modifier: Modifier,
    text: String,
    textColor: Color,
    style: TextStyle = MaterialTheme.typography.body2,
    isTextLowerCase: Boolean = true,
    isCurrentThemeDark: Boolean
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .border(1.dp, if (isCurrentThemeDark) Color.Gray else Color.Black)
            .padding(8.dp),
        text = if (isTextLowerCase) text.lowercase() else text,
        color = textColor,
        style = style
    )
}

@Composable
private fun GameSection(
    modifier: Modifier,
    gameState: GameState.Success,
    wordIndex: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    onGuessClicked: (String) -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (gameState.data.isEmpty()) {
                EmptyWordMessage(modifier = modifier)
            } else if (gameState.data.size < 2) {
                AtLeastTwoWordMessage(modifier = modifier)
            } else {
                ForeignWord(
                    modifier = modifier,
                    word = gameState.data[wordIndex].foreignWord,
                    isOrientPortrait = true
                )
                Space(modifier = modifier)
                CustomTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = value,
                    onValueChange = { onValueChanged(it) },
                    labelText = "Tahminin",
                    isError = isError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onGuessClicked(gameState.data[wordIndex].foreignWord) })
                )
                Space(modifier = modifier)
                CustomButton(
                    modifier = modifier,
                    onClick = { onGuessClicked(gameState.data[wordIndex].foreignWord) },
                    buttonText = "Tahmin et"
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (gameState.data.isEmpty()) {
                EmptyWordMessage(modifier = modifier)
            } else if (gameState.data.size < 2) {
                AtLeastTwoWordMessage(modifier = modifier)
            } else {
                ForeignWord(
                    modifier = modifier.weight(1f),
                    word = gameState.data[wordIndex].foreignWord,
                    isOrientPortrait = false
                )
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp),
                        value = value,
                        onValueChange = { onValueChanged(it) },
                        labelText = "Tahminin",
                        isError = isError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onGuessClicked(gameState.data[wordIndex].foreignWord) })
                    )
                    Space(modifier = modifier)
                    CustomButton(
                        modifier = modifier,
                        onClick = { onGuessClicked(gameState.data[wordIndex].foreignWord) },
                        buttonText = "Tahmin et"
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyWordMessage(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        text = "Kelime tahmin oyununu oynayabilmen için önce kelime defterine yeni kelimeler ekle.",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AtLeastTwoWordMessage(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        text = "Kelime tahmin oyununu oynayabilmen için kelime defterinde en az 2 kelime bulunmalı.",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ForeignWord(modifier: Modifier, word: String, isOrientPortrait: Boolean) {
    Card(
        modifier = if (isOrientPortrait) {
            modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenWidthDp.dp / 2)
        } else {
            modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                .padding(32.dp)
        },
        shape = RoundedCornerShape(10),
        elevation = 8.dp
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(modifier = Modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun Space(modifier: Modifier) {
    Spacer(modifier = modifier.height(32.dp))
}