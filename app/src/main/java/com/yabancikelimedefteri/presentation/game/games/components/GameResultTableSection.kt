package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.helpers.Answer
import com.yabancikelimedefteri.core.helpers.GameResultEmote
import com.yabancikelimedefteri.core.ui.theme.successGreen

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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(modifier = Modifier.weight(1f)) {
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
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stickyHeader {
                RowCellTitle(
                    s1 = stringResource(id = R.string.question),
                    s2 = stringResource(id = R.string.correct_answer),
                    s3 = stringResource(id = R.string.your_answer),
                    backgroundColor = MaterialTheme.colorScheme.background
                )
            }
            items(userAnswers) { result ->
                RowCell(s1 = result.question, s2 = result.correctAnswer, s3 = result.userAnswer)
            }
        }
        Column(
            modifier = Modifier
                .weight(3f)
                .padding(top = 16.dp)
        ) {
            if (quizResultEmote != null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                            .aspectRatio(1f),
                        painter = painterResource(id = quizResultEmote.emoteId),
                        contentDescription = null
                    )
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
    val isAnswerCorrect = s2.trim() == s3.trim()

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
        Text(modifier = Modifier.weight(1.5f), text = s2)
        Text(
            modifier = Modifier.weight(1.5f),
            text = s3,
            color = color ?: if (isAnswerCorrect) successGreen else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RowCellTitle(
    s1: String,
    s2: String,
    s3: String,
    backgroundColor: Color = Color.Transparent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RectangleShape,
                color = MaterialTheme.colorScheme.primary
            )
            .background(backgroundColor, RectangleShape)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = s1,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1.5f),
            text = s2,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1.5f),
            text = s3,
            fontWeight = FontWeight.Bold
        )
    }
}