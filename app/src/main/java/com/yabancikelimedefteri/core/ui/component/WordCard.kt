package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    wordId: Int,
    getWords: () -> Unit
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = state,
        enter = EnterTransition.None,
        exit = slideOutHorizontally()
    ) {
        ElevatedCard(
            modifier = modifier,
            shape = RoundedCornerShape(10)
        ) {
            DeleteWord(
                onClick = {
                    onDeleteClick(wordId)
                    state.targetState = false
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Word(word = foreignWord)
                CompareIcon()
                Word(word = meaning)
            }
        }
    }

    if (state.isIdle && !state.currentState) {
        getWords()
    }
}

@Composable
private fun DeleteWord(onClick: () -> Unit, isWordCardThin: Boolean = false) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            modifier = Modifier.size(if (isWordCardThin) 24.dp else 48.dp),
            onClick = onClick
        ) {
            if (isWordCardThin) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_delete_forever),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun Word(word: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = word,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CompareIcon() {
    Icon(
        modifier = Modifier.rotate(90f),
        painter = painterResource(id = R.drawable.ic_baseline_compare_arrows),
        contentDescription = "compare word"
    )
}