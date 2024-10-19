package com.yabancikelimedefteri.presentation.word_categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.ui.component.CardFeatures
import com.yabancikelimedefteri.core.ui.component.DeleteWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CategoryCard(
    categoryName: String,
    categoryId: Int,
    onDeleteClick: (Int) -> Unit,
    onCategoryCardClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }
    var showDeleteWarning by remember { mutableStateOf(false) }

    if (showDeleteWarning) {
        DeleteWarning(
            onDismissRequest = { showDeleteWarning = false },
            onConfirm = remember { {
                scope.launch {
                    showDeleteWarning = false
                    visible = false
                    delay(1000)
                    onDeleteClick(categoryId)
                }
            } }
        )
    }

    AnimatedVisibility(
        visible = visible,
        exit = scaleOut()
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            onClick = remember { { onCategoryCardClick(categoryId) } }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CardFeatures(
                    onDeleteClick = remember { { showDeleteWarning = true } },
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