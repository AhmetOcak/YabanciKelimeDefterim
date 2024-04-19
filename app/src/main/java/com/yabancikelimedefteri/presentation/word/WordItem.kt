package com.yabancikelimedefteri.presentation.word

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WordCard(
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    wordId: Int
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = state,
        enter = EnterTransition.None,
        exit = slideOutHorizontally()
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = {
                            onDeleteClick(wordId)
                            state.targetState = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = foreignWord,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        modifier = Modifier.rotate(90f).padding(vertical = 4.dp),
                        imageVector = Icons.AutoMirrored.Default.CompareArrows,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = meaning,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}