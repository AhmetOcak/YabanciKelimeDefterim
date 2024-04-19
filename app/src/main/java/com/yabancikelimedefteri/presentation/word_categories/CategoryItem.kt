package com.yabancikelimedefteri.presentation.word_categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CategoryCard(
    categoryName: String,
    categoryId: Int,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
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
                .wrapContentHeight(),
            onClick = remember { { onCategoryCardClick(categoryId) } }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CardFeatures(
                    onDeleteClick = remember {
                        {
                            onDeleteClick(categoryId)
                            state.targetState = false
                        }
                    },
                    onEditClick = remember { { onEditClick(categoryId) } }
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = categoryName,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}


@Composable
private fun CardFeatures(
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}