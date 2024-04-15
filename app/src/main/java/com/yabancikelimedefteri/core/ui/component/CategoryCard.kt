package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    categoryName: String,
    categoryId: Int,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    onEditClick: (Int) -> Unit,
    isCatCardThin: Boolean
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = state,
        enter = EnterTransition.None,
        exit = slideOutHorizontally()
    ) {
        ElevatedCard(
            modifier = modifier,
            shape = RoundedCornerShape(10),
            onClick = { onCategoryCardClick(categoryId) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                CardFeatures(
                    onDeleteClick = {
                        onDeleteClick(categoryId)
                        state.targetState = false
                    },
                    onEditClick = {
                        onEditClick(categoryId)
                    },
                    isCatCardThin = isCatCardThin
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Word(word = categoryName)
                }
            }
        }
    }

    if (state.isIdle && !state.currentState) {
        getCategories()
    }
}


@Composable
private fun CardFeatures(
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    isCatCardThin: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        EditCategory(onClick = onEditClick, isCatCardThin = isCatCardThin)
        if (isCatCardThin) {
            Box(modifier = Modifier.size(width = 8.dp, height = 0.dp))
        }
        DeleteCategory(onClick = onDeleteClick, isCatCardThin = isCatCardThin)
    }
}

@Composable
private fun DeleteCategory(
    onClick: () -> Unit,
    isCatCardThin: Boolean
) {
    IconButton(
        modifier = Modifier.size(if (isCatCardThin) 24.dp else 48.dp),
        onClick = onClick
    ) {
        if (isCatCardThin) {
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

@Composable
private fun EditCategory(onClick: () -> Unit, isCatCardThin: Boolean) {
    IconButton(
        modifier = Modifier.size(if (isCatCardThin) 24.dp else 48.dp),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun Word(word: String) {
    Text(modifier = Modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
}