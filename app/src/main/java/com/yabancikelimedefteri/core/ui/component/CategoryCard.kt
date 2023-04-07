package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    height: Dp = LocalConfiguration.current.screenWidthDp.dp / 2,
    width: Dp = 0.dp,
    onCategoryCardClick: (Int) -> Unit,
    getCategories: () -> Unit
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
            DeleteCategory(
                modifier = modifier,
                onClick = {
                    onDeleteClick(categoryId)
                    state.targetState = false
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

@Composable
private fun DeleteCategory(modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_forever),
                contentDescription = "Kelimeyi sil",
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun Word(modifier: Modifier, word: String) {
    Text(modifier = modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
}