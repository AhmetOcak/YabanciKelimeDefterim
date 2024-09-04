package com.yabancikelimedefteri.presentation.word

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.DeleteWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WordCard(
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    wordId: Int,
    isWordListTypeThin: Boolean
) {
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }
    var showDeleteWarning by remember { mutableStateOf(false) }

    if (showDeleteWarning) {
        DeleteWarning(
            title = stringResource(id = R.string.category),
            onDismissRequest = { showDeleteWarning = false },
            onConfirm = remember { {
                scope.launch {
                    showDeleteWarning = false
                    visible = false
                    delay(1000)
                    onDeleteClick(wordId)
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
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = remember { { showDeleteWarning = true } }) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                WordItemContent(
                    isWordListTypeThin = isWordListTypeThin,
                    foreignWord = foreignWord,
                    meaning = meaning
                )
            }
        }
    }
}

@Composable
private fun WordItemContent(isWordListTypeThin: Boolean, foreignWord: String, meaning: String) {
    if (isWordListTypeThin) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = foreignWord,
                textAlign = TextAlign.End
            )
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                imageVector = Icons.AutoMirrored.Default.CompareArrows,
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                meaning.split(",").map { it.trim() }.forEach { s ->
                    Text(
                        text = s,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = foreignWord,
                textAlign = TextAlign.Center
            )
            Icon(
                modifier = Modifier
                    .rotate(90f)
                    .then(Modifier.padding(vertical = 4.dp)),
                imageVector = Icons.AutoMirrored.Default.CompareArrows,
                contentDescription = null
            )
            Text(
                text = meaning,
                textAlign = TextAlign.Center
            )
        }
    }
}