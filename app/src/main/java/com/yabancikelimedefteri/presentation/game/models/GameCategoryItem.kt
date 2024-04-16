package com.yabancikelimedefteri.presentation.game.models

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameCategoryItem(
    categoryName: String,
    categoryId: Int,
    onClick: (Int) -> Unit
) {
    var clicked by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.size(96.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (clicked) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.surface
        ),
        onClick = {
            onClick(categoryId)
            clicked = !clicked
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}