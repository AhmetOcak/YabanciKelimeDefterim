package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@Composable
fun WordCard(
    modifier: Modifier,
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    height: Dp,
    width: Dp = 0.dp,
    wordId: Int,
    getWords: () -> Unit
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = state,
        enter = EnterTransition.None,
        exit = slideOutHorizontally()
    ) {
        Card(
            modifier = if (width == 0.dp) {
                modifier
                    .fillMaxWidth()
                    .height(height)
            } else {
                modifier
                    .width(width)
                    .height(height)
            },
            shape = RoundedCornerShape(10),
            elevation = 4.dp
        ) {
            DeleteWord(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    onDeleteClick(wordId)
                    state.targetState = false
                }
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Word(modifier = modifier.fillMaxWidth(), word = foreignWord)
                CompareIcon(modifier = modifier.rotate(90f))
                Word(modifier = modifier.fillMaxWidth(), word = meaning)
            }
        }
    }

    if (state.isIdle && !state.currentState) {
        getWords()
    }
}

/**
 * Thin version of the Word Card.
 * Use this component when user choose thin ui mode.
 */
@Composable
fun WordCard(
    modifier: Modifier,
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    width: Dp = 0.dp,
    wordId: Int,
    getWords: () -> Unit
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = state,
        enter = EnterTransition.None,
        exit = slideOutHorizontally()
    ) {
        Card(
            modifier = if (width == 0.dp) {
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            } else {
                modifier
                    .width(width)
                    .wrapContentHeight()
            },
            shape = RoundedCornerShape(10),
            elevation = 4.dp
        ) {
            Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
                DeleteWord(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        onDeleteClick(wordId)
                        state.targetState = false
                    },
                    isWordCardThin = true
                )
                Row(
                    modifier = modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Word(
                        modifier = modifier
                            .fillMaxSize()
                            .weight(5f),
                        word = foreignWord
                    )
                    CompareIcon(modifier = modifier.weight(1f))
                    Word(
                        modifier = modifier
                            .fillMaxHeight()
                            .weight(5f),
                        word = meaning
                    )
                }
            }
        }
    }

    if (state.isIdle && !state.currentState) {
        getWords()
    }
}

@Composable
private fun DeleteWord(modifier: Modifier, onClick: () -> Unit, isWordCardThin: Boolean = false) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(modifier = Modifier.size(if (isWordCardThin) 24.dp else 48.dp), onClick = onClick) {
            if (isWordCardThin) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Kelimeyi sil",
                    tint = MaterialTheme.colors.secondary
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_delete_forever),
                    contentDescription = "Kelimeyi sil",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun Word(modifier: Modifier, word: String) {
    Text(
        modifier = modifier,
        text = word,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CompareIcon(modifier: Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_baseline_compare_arrows),
        contentDescription = "compare word"
    )
}