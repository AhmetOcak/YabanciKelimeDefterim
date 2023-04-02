package com.yabancikelimedefteri.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast

@Composable
fun GameScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: GameViewModel = hiltViewModel()

    val gameState by viewModel.gameState.collectAsState()

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
        words = viewModel.words ?: mutableMapOf<String, String>(),
        correctCount = viewModel.correctAnswerCount,
        inCorrectCount = viewModel.inCorrectAnswerCount
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
    words: MutableMap<String, *>,
    correctCount: Int,
    inCorrectCount: Int
) {

    when (gameState) {
        is GameState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is GameState.Success -> {
            if (isGameOver) {
                GameResultSection(
                    modifier = modifier,
                    answers = answers,
                    words = words,
                    correctCount = correctCount,
                    inCorrectCount = inCorrectCount
                )
            } else {
                GameSection(
                    modifier = modifier,
                    gameState = gameState,
                    wordIndex = wordIndex,
                    value = value,
                    onValueChanged = onValueChanged,
                    isError = isError,
                    onGuessClicked = onGuessClicked
                )
            }
        }
        is GameState.Error -> {
            CustomToast(context = LocalContext.current, message = gameState.message)
        }
    }
}

@Composable
private fun GameResultSection(
    modifier: Modifier,
    answers: MutableMap<String, String>,
    words: MutableMap<String, *>,
    correctCount: Int,
    inCorrectCount: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameResultSubTitle(
            modifier = modifier,
            correctCount = correctCount,
            inCorrectCount = inCorrectCount
        )
        InfoAboutResultTable(modifier = modifier)
        GameResultTableHeaders(modifier = modifier)
        GameResultTable(modifier = modifier, answers = answers, words = words)
    }
}

@Composable
private fun GameResultTable(
    modifier: Modifier,
    answers: MutableMap<String, String>,
    words: MutableMap<String, *>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(answers.keys.toList()) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(modifier = modifier, text = it)
                TableCell(
                    modifier = modifier,
                    text = answers[it] ?: "",
                    textColor = if (
                        words[it].toString().uppercase() == (answers[it]?.uppercase() ?: "")
                    ) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
private fun GameResultTableHeaders(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(
            modifier = modifier,
            text = "Doğru Cevap",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false
        )
        TableCell(
            modifier = modifier,
            text = "Senin Cevabın",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
            isTextLowerCase = false
        )
    }
}

@Composable
private fun InfoAboutResultTable(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "Doğru ",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start,
                color = Color.Green
            )
            Text(
                text = "cevaplar yeşil, ",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start
            )
            Text(
                text = "yanlış ",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start,
                color = Color.Red
            )
            Text(
                text = "cevaplar kırmızı renkte ",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start
            )
        }
        Row {
            Text(
                text = "gösterilmiştir.",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun GameResultSubTitle(modifier: Modifier, correctCount: Int, inCorrectCount: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Oyun Sonucun:",
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            modifier = modifier.padding(start = 4.dp),
            text = "$correctCount doğru $inCorrectCount yanlış"
        )
    }
}

@Composable
private fun RowScope.TableCell(
    modifier: Modifier,
    text: String,
    textColor: Color = Color.Black,
    style: TextStyle = MaterialTheme.typography.body2,
    isTextLowerCase: Boolean = true
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .border(1.dp, Color.Black)
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
            ForeignWord(modifier = modifier, word = gameState.data[wordIndex])
            Space(modifier = modifier)
            CustomTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = value,
                onValueChange = { onValueChanged(it) },
                labelText = "Tahminin",
                isError = isError
            )
            Space(modifier = modifier)
            CustomButton(
                modifier = modifier,
                onClick = { onGuessClicked(gameState.data[wordIndex]) },
                buttonText = "Tahmin et"
            )
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
private fun ForeignWord(modifier: Modifier, word: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp / 2),
        shape = RoundedCornerShape(10),
        elevation = 8.dp
    ) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(modifier = modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
        }
    }
}


@Composable
private fun Space(modifier: Modifier) {
    Spacer(modifier = modifier.height(32.dp))
}