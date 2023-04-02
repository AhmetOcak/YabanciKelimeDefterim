package com.yabancikelimedefteri.presentation.add_word

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField

@Composable
fun AddWordScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    BackHandler {
        onNavigateBack()
    }

    AddWordScreenContent(modifier = modifier)
}

@Composable
private fun AddWordScreenContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ContentImage(modifier = modifier)
        Space(modifier = modifier, spaceHeight = 64.dp)
        CustomTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = "",
            onValueChange = {},
            labelText = "Yabancı kelime"
        )
        Space(modifier = modifier)
        CustomTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = "",
            onValueChange = {},
            labelText = "Kelimenin anlamı"
        )
        Space(modifier = modifier, spaceHeight = 32.dp)
        CustomButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            buttonText = "Kelimeyi ekle"
        )
    }
}

@Composable
private fun ContentImage(modifier: Modifier) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 80.dp),
        painter = painterResource(id = R.drawable.ic_undraw_books_re_8gea),
        contentDescription = "kelime ekleme görseli",
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun Space(modifier: Modifier, spaceHeight: Dp = 16.dp) {
    Spacer(modifier = modifier.height(spaceHeight))
}
