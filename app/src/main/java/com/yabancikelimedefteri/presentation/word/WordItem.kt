package com.yabancikelimedefteri.presentation.word

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CardFeatures
import com.yabancikelimedefteri.core.ui.component.DeleteWarning
import com.yabancikelimedefteri.domain.utils.setImportanceLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WordCard(
    foreignWord: String,
    meaning: String,
    onDeleteClick: (Int) -> Unit,
    wordId: Int,
    isWordListTypeThin: Boolean,
    importanceLevel: Int,
    onEditClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }
    var showDeleteWarning by remember { mutableStateOf(false) }

    if (showDeleteWarning) {
        DeleteWarning(
            title = stringResource(id = R.string.delete_word),
            onDismissRequest = { showDeleteWarning = false },
            onConfirm = remember {
                {
                    scope.launch {
                        showDeleteWarning = false
                        visible = false
                        delay(1000)
                        onDeleteClick(wordId)
                    }
                }
            }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(importanceLevel.setImportanceLevel())
                            .clickable(onClick = {})
                    )
                    CardFeatures(
                        onDeleteClick = remember { { showDeleteWarning = true } },
                        onEditClick = remember { { onEditClick(wordId) } }
                    )
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