package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.GameResultEmote

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameResultTableSection(
    modifier: Modifier,
    correctAnswerCount: Int,
    wrongAnswerCount: Int,
    successRate: String,
    userAnswers: List<Answer>,
    onReturnGamesScreenClick: () -> Unit,
    quizResultEmote: GameResultEmote?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Subtitle(
            title = stringResource(id = R.string.correct_answer),
            contentText = "$correctAnswerCount"
        )
        Subtitle(
            title = stringResource(id = R.string.wrong_answer),
            contentText = "$wrongAnswerCount"
        )
        Subtitle(
            title = stringResource(id = R.string.success_rate),
            contentText = successRate
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stickyHeader {
                RowCellTitle(
                    s1 = stringResource(id = R.string.question),
                    s2 = stringResource(id = R.string.correct_answer),
                    s3 = stringResource(id = R.string.your_answer)
                )
            }
            items(userAnswers) { result ->
                RowCell(s1 = result.question, s2 = result.correctAnswer, s3 = result.userAnswer)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (quizResultEmote != null) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = quizResultEmote.emote, fontSize = 100.sp)
                Text(text = quizResultEmote.message.asString(), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = onReturnGamesScreenClick) {
                Text(text = stringResource(id = R.string.return_games_screen))
            }
        }
    }
}

@Composable
private fun Subtitle(title: String, contentText: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$title: ")
            }
            append(contentText)
        }
    )
}

@Composable
private fun RowCell(
    s1: String,
    s2: String,
    s3: String,
    color: Color? = null
) {
    val isAnswerCorrect = s2 == s3

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, shape = RectangleShape, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = s1
        )
        Text(modifier = Modifier.weight(1f), text = s2)
        Text(
            modifier = Modifier.weight(1f),
            text = s3,
            color = color
                ?: if (isAnswerCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RowCellTitle(
    s1: String,
    s2: String,
    s3: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RectangleShape,
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = s1,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f),
            text = s2,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f),
            text = s3,
            fontWeight = FontWeight.Bold
        )
    }
}