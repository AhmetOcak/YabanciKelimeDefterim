package com.yabancikelimedefteri.presentation.add_word

import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun AddWordScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit, resources: Resources) {

    val viewModel: AddWordViewModel = hiltViewModel()

    val addWordState by viewModel.addWordState.collectAsState()

    val focusManager = LocalFocusManager.current

    var isStateLoading by remember { mutableStateOf(false) }

    BackHandler {
        onNavigateBack()
    }

    when (addWordState) {
        is CreateWordState.Loading -> {
            isStateLoading = true
        }
        is CreateWordState.Success -> {
            CustomToast(
                context = LocalContext.current,
                message = resources.getString(R.string.add_word_success)
            )
            viewModel.resetAddWordState()
            isStateLoading = false
        }
        is CreateWordState.Error -> {
            CustomToast(
                context = LocalContext.current,
                message = resources.getString(R.string.error)
            )
            viewModel.resetAddWordState()
        }
        else -> {}
    }

    AddWordScreenContent(
        modifier = modifier,
        addWordOnClick = {
            viewModel.addForeignWord()
            focusManager.clearFocus()
        },
        onForeignWordChanged = { viewModel.updateForeignWord(it) },
        foreignWordVal = viewModel.foreignWord,
        onMeaningChanged = { viewModel.updateMeaning(it) },
        meaningVal = viewModel.meaning,
        foreignWordFieldError = viewModel.foreignWordFieldError,
        meaningFieldError = viewModel.meaningFieldError,
        focusManager = focusManager,
        isStateLoading = isStateLoading,
        foreignWordLabel = resources.getString(R.string.foreign_word),
        meaningLabel = resources.getString(R.string.meaning),
        buttonText = resources.getString(R.string.add_word),
        textFieldErrorText = resources.getString(R.string.text_field_error)
    )
}

@Composable
private fun AddWordScreenContent(
    modifier: Modifier,
    addWordOnClick: () -> Unit,
    onForeignWordChanged: (String) -> Unit,
    foreignWordVal: String,
    onMeaningChanged: (String) -> Unit,
    meaningVal: String,
    foreignWordFieldError: Boolean,
    meaningFieldError: Boolean,
    focusManager: FocusManager,
    isStateLoading: Boolean,
    foreignWordLabel: String,
    meaningLabel: String,
    buttonText: String,
    textFieldErrorText: String
) {
    ResponsiveContent(
        modifier = modifier,
        foreignWordVal = foreignWordVal,
        onForeignWordChanged = onForeignWordChanged,
        foreignWordFieldError = foreignWordFieldError,
        meaningVal = meaningVal,
        onMeaningChanged = onMeaningChanged,
        meaningFieldError = meaningFieldError,
        addWordOnClick = addWordOnClick,
        focusManager = focusManager,
        isStateLoading = isStateLoading,
        foreignWordLabel = foreignWordLabel,
        meaningLabel = meaningLabel,
        buttonText = buttonText,
        textFieldErrorText = textFieldErrorText
    )
}

@Composable
private fun ResponsiveContent(
    modifier: Modifier,
    foreignWordVal: String,
    onForeignWordChanged: (String) -> Unit,
    foreignWordFieldError: Boolean,
    meaningVal: String,
    onMeaningChanged: (String) -> Unit,
    meaningFieldError: Boolean,
    addWordOnClick: () -> Unit,
    focusManager: FocusManager,
    isStateLoading: Boolean,
    foreignWordLabel: String,
    meaningLabel: String,
    buttonText: String,
    textFieldErrorText: String
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ContentImage(modifier = modifier)
            Space(modifier = modifier, spaceHeight = 64.dp)
            CustomTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = foreignWordVal,
                onValueChange = { onForeignWordChanged(it) },
                labelText = foreignWordLabel,
                isError = foreignWordFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                errorMessage = textFieldErrorText
            )
            Space(modifier = modifier)
            CustomTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = meaningVal,
                onValueChange = { onMeaningChanged(it) },
                labelText = meaningLabel,
                isError = meaningFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        addWordOnClick()
                    }
                ),
                errorMessage = textFieldErrorText
            )
            Space(modifier = modifier, spaceHeight = 32.dp)
            CustomButton(
                modifier = modifier,
                onClick = addWordOnClick,
                buttonText = buttonText,
                enabled = !isStateLoading
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContentImage(modifier = modifier.weight(1f))
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = foreignWordVal,
                    onValueChange = { onForeignWordChanged(it) },
                    labelText = foreignWordLabel,
                    isError = foreignWordFieldError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    errorMessage = textFieldErrorText
                )
                Space(modifier = modifier)
                CustomTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = meaningVal,
                    onValueChange = { onMeaningChanged(it) },
                    labelText = meaningLabel,
                    isError = meaningFieldError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            addWordOnClick()
                        }
                    ),
                    errorMessage = textFieldErrorText
                )
                Space(modifier = modifier, spaceHeight = 32.dp)
                CustomButton(
                    modifier = modifier,
                    onClick = addWordOnClick,
                    buttonText = buttonText,
                    enabled = !isStateLoading
                )
            }
        }
    }
}

@Composable
private fun ContentImage(modifier: Modifier, isOrientationPortrait: Boolean = true) {
    Image(
        modifier = if (isOrientationPortrait) {
            modifier
                .fillMaxWidth()
                .padding(horizontal = 80.dp)
        } else {
            modifier.padding(horizontal = 80.dp)
        },
        painter = painterResource(id = R.drawable.ic_undraw_books_re_8gea),
        contentDescription = "kelime ekleme görseli",
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun Space(modifier: Modifier, spaceHeight: Dp = 16.dp) {
    Spacer(modifier = modifier.height(spaceHeight))
}