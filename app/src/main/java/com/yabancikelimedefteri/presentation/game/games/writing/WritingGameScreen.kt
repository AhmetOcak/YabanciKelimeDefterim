package com.yabancikelimedefteri.presentation.game.games.writing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.presentation.game.models.GameWordItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingGameScreen(upPress: () -> Unit, viewModel: WritingGameViewModel = hiltViewModel()) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.word_writing))
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        WritingGameScreenContent(
            modifier = Modifier.padding(paddingValues),
            question = "this is a question",
            answerValue = viewModel.answerValue,
            onAnswerValueChange = viewModel::updateAnswerValue,
            onSubmitClick = {}
        )
    }
}

@Composable
private fun WritingGameScreenContent(
    modifier: Modifier,
    question: String,
    answerValue: String,
    onAnswerValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = LocalConfiguration.current.screenHeightDp.dp / 2),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        GameWordItem(word = question)
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = answerValue,
                onValueChange = onAnswerValueChange,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSubmitClick, enabled = answerValue.isNotBlank()) {
                Text(text = stringResource(id = R.string.submit_answer))
            }
        }
    }
}