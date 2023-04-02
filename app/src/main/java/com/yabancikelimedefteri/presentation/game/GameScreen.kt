package com.yabancikelimedefteri.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField

@Composable
fun GameScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    BackHandler {
        onNavigateBack()
    }

    GameScreenContent(modifier = modifier)
}

@Composable
private fun GameScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ForeignWord(modifier = modifier, word = "yabancı kelime")
        Space(modifier = modifier)
        CustomTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = "",
            onValueChange = {},
            labelText = "Tahminin"
        )
        Space(modifier = modifier)
        CustomButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            buttonText = "Tahmin et"
        )
    }
}

@Composable
private fun ForeignWord(modifier: Modifier, word: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp / 2),
        shape = RoundedCornerShape(10),
        elevation = 8.dp
    ) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(modifier = modifier.fillMaxWidth(), text = word, textAlign = TextAlign.Center)
        }
    }
}


@Composable
private fun Space(modifier: Modifier) {
    Spacer(modifier = modifier.height(32.dp))
}