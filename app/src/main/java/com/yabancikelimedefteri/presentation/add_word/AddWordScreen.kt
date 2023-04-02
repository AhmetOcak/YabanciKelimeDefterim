package com.yabancikelimedefteri.presentation.add_word

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast

@Composable
fun AddWordScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: AddWordViewModel = hiltViewModel()

    val addWordState by viewModel.addWordState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    AddWordScreenContent(
        modifier = modifier,
        addWordOnClick = { viewModel.addForeignWord() },
        addWordState = addWordState,
        resetAddWordState = { viewModel.resetAddWordState() },
        onForeignWordChanged = { viewModel.updateForeignWord(it) },
        foreignWordVal = viewModel.foreignWord,
        onMeaningChanged = { viewModel.updateMeaning(it) },
        meaningVal = viewModel.meaning,
        foreignWordFieldError = viewModel.foreignWordFieldError,
        meaningFieldError = viewModel.meaningFieldError
    )
}

// Todo: Loading çok hızlı geçiyor. UI takılıyor etkisi yaratıyor
@Composable
private fun AddWordScreenContent(
    modifier: Modifier,
    addWordOnClick: () -> Unit,
    addWordState: AddWordState,
    resetAddWordState : () -> Unit,
    onForeignWordChanged: (String) -> Unit,
    foreignWordVal: String,
    onMeaningChanged: (String) -> Unit,
    meaningVal: String,
    foreignWordFieldError: Boolean,
    meaningFieldError: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(addWordState) {
            is AddWordState.Nothing -> {
                ContentImage(modifier = modifier)
                Space(modifier = modifier, spaceHeight = 64.dp)
                CustomTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = foreignWordVal,
                    onValueChange = { onForeignWordChanged(it) },
                    labelText = "Yabancı kelime",
                    isError = foreignWordFieldError
                )
                Space(modifier = modifier)
                CustomTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = meaningVal,
                    onValueChange = { onMeaningChanged(it) },
                    labelText = "Kelimenin anlamı",
                    isError = meaningFieldError
                )
                Space(modifier = modifier, spaceHeight = 32.dp)
                CustomButton(
                    modifier = modifier,
                    onClick = addWordOnClick,
                    buttonText = "Kelimeyi ekle"
                )
            }
            is AddWordState.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AddWordState.Success -> {
                CustomToast(
                    context = LocalContext.current,
                    message = "Kelime defterine yeni bir kelime ekledin"
                )
                resetAddWordState()
            }
            is AddWordState.Error -> {
                CustomToast(context = LocalContext.current, message = addWordState.message)
                resetAddWordState()
            }
        }
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