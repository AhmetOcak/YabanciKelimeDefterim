package com.yabancikelimedefteri.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@Composable
fun WordCard(modifier: Modifier, foreignWord: String, meaning: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp / 2),
        shape = RoundedCornerShape(10),
        elevation = 4.dp
    ) {
        DeleteWord(modifier = modifier, onClick = {})
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Word(modifier = modifier, word = foreignWord)
            CompareIcon(modifier = modifier)
            Word(modifier = modifier, word = meaning)
        }
    }
}

@Composable
private fun DeleteWord(modifier: Modifier, onClick: () -> Unit) {
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

@Composable
private fun CompareIcon(modifier: Modifier) {
    Icon(
        modifier = modifier.rotate(90f),
        painter = painterResource(id = R.drawable.ic_baseline_compare_arrows),
        contentDescription = "compare word"
    )
}