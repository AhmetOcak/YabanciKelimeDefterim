package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(
    modifier: Modifier,
    categoryName: String,
    categoryId: Int,
    onDeleteClick: (Int) -> Unit,
    height: Dp,
    width: Dp = 0.dp,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    onEditClick: (Int) -> Unit
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
            onClick = { onCategoryCardClick(categoryId) },
            elevation = 4.dp
        ) {
            CardFeatures(
                modifier = modifier,
                onDeleteClick = {
                    onDeleteClick(categoryId)
                    state.targetState = false
                },
                onEditClick = {
                    onEditClick(categoryId)
                }
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Word(modifier = modifier, word = categoryName)
            }
        }
    }

    if (state.isIdle && !state.currentState) {
        getCategories()
    }
}

/**
 * Thin version of the Category Card.
 * Use this component when user choose thin ui mode.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(
    modifier: Modifier,
    categoryName: String,
    categoryId: Int,
    onDeleteClick: (Int) -> Unit,
    width: Dp = 0.dp,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit,
    onEditClick: (Int) -> Unit
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
            onClick = { onCategoryCardClick(categoryId) },
            elevation = 4.dp
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                CardFeatures(
                    modifier = modifier,
                    onDeleteClick = {
                        onDeleteClick(categoryId)
                        state.targetState = false
                    },
                    onEditClick = {
                        onEditClick(categoryId)
                    },
                    isCatCardThin = true
                )
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Word(modifier = modifier, word = categoryName)
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
    modifier: Modifier,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    isCatCardThin: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        EditCategory(onClick = onEditClick, isCatCardThin = isCatCardThin)
        if (isCatCardThin) {
            Box(modifier = modifier.size(width = 8.dp, height = 0.dp))
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
                tint = MaterialTheme.colors.secondary
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_forever),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
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
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun Word(modifier: Modifier, word: String) {
    Text(modifier = modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
}