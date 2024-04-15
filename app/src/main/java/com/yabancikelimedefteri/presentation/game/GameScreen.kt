package com.yabancikelimedefteri.presentation.game

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.getCurrentTheme
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.domain.model.CategoryWithId
import com.yabancikelimedefteri.domain.model.WordWithId
import com.yabancikelimedefteri.presentation.main.OrientationState

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
        onGuessClicked = remember {
            {
                if (viewModel.isGuessWordReadyForSubmit()) {
                    viewModel.addAnswer(it)
                    viewModel.resetGuessWord()
                    viewModel.incWordIndex()
                }
                if (!viewModel.isGameStillGoing()) {
                    viewModel.calculateResult()
                }
            }
        },
        onValueChanged = remember {
            { viewModel.updateGuessWord(it) }
        },
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
        setAllCateSelected = remember {
            { viewModel.setAllCategorySelect(it) }
        },
        isAllCatSelected = viewModel.isAllCategorySelected,
        categoriesState = categoriesState,
        addAllCategory = viewModel::addAllCategories,
        addSelectedCategory = remember {
            { viewModel.addSelectedCategory(it) }
        },
        removeSelectedCategory = remember {
            { viewModel.removeSelectedCategory(it) }
        },
        removeAllCategory = viewModel::removeAllCategories,
        isButtonEnabled = viewModel.isGameReadyToLaunch,
        launchTheGame = viewModel::launchTheGame
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
    launchTheGame: () -> Unit,
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
                        launchTheGame = launchTheGame,
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
                    gameState = gameState,
                    wordIndex = wordIndex,
                    value = value,
                    onValueChanged = onValueChanged,
                    isError = isError,
                    onGuessClicked = onGuessClicked,
                )
            } else {
                GameResultSection(
                    answers = answers,
                    words = words,
                    correctCount = correctCount,
                    inCorrectCount = inCorrectCount,
                    sharedPreferences = sharedPreferences,
                    isCurrentThemeDark = isCurrentThemeDark,
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
            CategoryDescription()
            Space(modifier = Modifier.height(16.dp))
            CategoriesList(
                categories,
                isAllCatSelected,
                addSelectedCategory,
                removeSelectedCategory,
                setAllCateSelected,
                addAllCategory,
                removeAllCategory,
            )
            Space(modifier = Modifier.height(16.dp))
            LaunchTheGameButton(launchTheGame = launchTheGame, isButtonEnabled = isButtonEnabled)
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                CategoriesList(
                    categories,
                    isAllCatSelected,
                    addSelectedCategory,
                    removeSelectedCategory,
                    setAllCateSelected,
                    addAllCategory,
                    removeAllCategory,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CategoryDescription()
                Space(modifier = Modifier.height(16.dp))
                LaunchTheGameButton(
                    launchTheGame = launchTheGame,
                    isButtonEnabled = isButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun LaunchTheGameButton(
    launchTheGame: () -> Unit,
    isButtonEnabled: Boolean
) {
    CustomButton(
        onClick = launchTheGame,
        buttonText = stringResource(R.string.start_the_game),
        enabled = isButtonEnabled
    )
}

@Composable
private fun CategoriesList(
    categories: List<CategoryWithId>,
    isAllCatSelected: Boolean,
    addSelectedCategory: (Int) -> Unit,
    removeSelectedCategory: (Int) -> Unit,
    setAllCateSelected: (Boolean) -> Unit,
    addAllCategory: () -> Unit,
    removeAllCategory: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.height(LocalConfiguration.current.screenWidthDp.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) {
            GameCategory(
                categoryName = it.categoryName,
                isAllCatSelected = isAllCatSelected,
                categoryId = it.categoryId,
                addSelectedCategory = addSelectedCategory,
                removeSelectedCategory = removeSelectedCategory,
            )
        }
        item {
            GameCategory(
                categoryName = stringResource(R.string.category_all),
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

@Composable
private fun CategoryDescription() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.game_description),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun GameCategory(
    categoryName: String,
    categoryId: Int,
    allClicked: (Boolean) -> Unit = {},
    isAllCatSelected: Boolean,
    addSelectedCategory: (Int) -> Unit,
    removeSelectedCategory: (Int) -> Unit
) {
    var clicked by rememberSaveable { mutableStateOf(false) }

    val allCategoryText = stringResource(R.string.category_all)

    Card(
        modifier = Modifier
            .size(96.dp)
            .clickable(
                enabled = if (categoryName == allCategoryText) {
                    true
                } else !isAllCatSelected,
                onClick = {
                    clicked = !clicked
                    if (categoryName == allCategoryText) {
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
            color = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = if (isAllCatSelected)
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            else if (clicked) {
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            } else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GameResultSection(
    answers: MutableMap<String, String>,
    words: List<WordWithId>,
    correctCount: Int,
    inCorrectCount: Int,
    sharedPreferences: SharedPreferences,
    isCurrentThemeDark: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameResultSubTitle(
            correctCount = correctCount,
            inCorrectCount = inCorrectCount,
        )
        InfoAboutResultTable()
        GameResultTableHeaders(
            tableCellTextColor = if (
                sharedPreferences.getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_YES
            ) Color.White
            else
                Color.Black,
            isCurrentThemeDark = isCurrentThemeDark,
        )
        GameResultTable(
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
    answers: MutableMap<String, String>,
    words: List<WordWithId>,
    tableCellTextColor: Color,
    isCurrentThemeDark: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(answers.keys.toList()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = it,
                    textColor = tableCellTextColor,
                    isCurrentThemeDark = isCurrentThemeDark
                )
                TableCell(
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
    tableCellTextColor: Color,
    isCurrentThemeDark: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(
            text = stringResource(R.string.correct_answer),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false,
            textColor = tableCellTextColor,
            isCurrentThemeDark = isCurrentThemeDark
        )
        TableCell(
            text = stringResource(R.string.user_answer),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false,
            textColor = tableCellTextColor,
            isCurrentThemeDark = isCurrentThemeDark
        )
    }
}

@Composable
private fun InfoAboutResultTable() {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Green)) {
                append("${stringResource(R.string.true_word)} ")
            }
            append("${stringResource(R.string.game_result_table_description_part_one)} ")
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("${stringResource(R.string.false_word)} ")
            }
            append(stringResource(R.string.game_result_table_description_part_two))
        },
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun GameResultSubTitle(
    correctCount: Int,
    inCorrectCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(R.string.game_result_message)}:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "$correctCount ${stringResource(R.string.true_word)} $inCorrectCount ${
                stringResource(
                    R.string.false_word
                )
            }",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    textColor: Color,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    isTextLowerCase: Boolean = true,
    isCurrentThemeDark: Boolean
) {
    Text(
        modifier = Modifier
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
    gameState: GameState.Success,
    wordIndex: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    onGuessClicked: (String) -> Unit,
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (gameState.data.isEmpty()) {
                EmptyWordMessage()
            } else if (gameState.data.size < 2) {
                AtLeastTwoWordMessage()
            } else {
                ForeignWord(
                    word = gameState.data[wordIndex].foreignWord,
                    isOrientPortrait = true
                )
                Space()
                AnswerField(
                    value = value,
                    onValueChanged,
                    isError,
                    onGuessClicked,
                    gameState,
                    wordIndex,
                )
                Space()
                CustomButton(
                    onClick = { onGuessClicked(gameState.data[wordIndex].foreignWord) },
                    buttonText = stringResource(R.string.submit_answer)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (gameState.data.isEmpty()) {
                EmptyWordMessage()
            } else if (gameState.data.size < 2) {
                AtLeastTwoWordMessage()
            } else {
                ForeignWord(
                    modifier = Modifier.weight(1f),
                    word = gameState.data[wordIndex].foreignWord,
                    isOrientPortrait = false
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnswerField(
                        value = value,
                        onValueChanged = onValueChanged,
                        isError = isError,
                        onGuessClicked = onGuessClicked,
                        gameState = gameState,
                        wordIndex = wordIndex,
                    )
                    Space()
                    CustomButton(
                        onClick = { onGuessClicked(gameState.data[wordIndex].foreignWord) },
                        buttonText = stringResource(R.string.submit_answer)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerField(
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    onGuessClicked: (String) -> Unit,
    gameState: GameState.Success,
    wordIndex: Int,
) {
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        value = value,
        onValueChange = { onValueChanged(it) },
        labelText = stringResource(R.string.user_guess),
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onGuessClicked(gameState.data[wordIndex].foreignWord) }),
        errorMessage = stringResource(R.string.text_field_error)
    )
}

@Composable
private fun EmptyWordMessage() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        text = stringResource(R.string.at_least_two_word_warning),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AtLeastTwoWordMessage() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        text = stringResource(R.string.at_least_two_word_warning),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ForeignWord(modifier: Modifier = Modifier, word: String, isOrientPortrait: Boolean) {
    ElevatedCard(
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
        shape = RoundedCornerShape(10)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(modifier = Modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun Space(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(32.dp))
}