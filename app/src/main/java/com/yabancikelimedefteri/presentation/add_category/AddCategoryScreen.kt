package com.yabancikelimedefteri.presentation.add_category

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {

    val viewModel: AddCategoryViewModel = hiltViewModel()

    var isStateLoading by remember { mutableStateOf(false) }

    val createCategoryState by viewModel.createCategoryState.collectAsState()

    val focusManager = LocalFocusManager.current

    BackHandler {
        onNavigateBack()
    }

    when (createCategoryState) {
        is CreateCategoryState.Loading -> {
            isStateLoading = true
        }
        is CreateCategoryState.Success -> {
            CustomToast(
                context = LocalContext.current,
                message = stringResource(R.string.add_category_success)
            )
            viewModel.resetCategoryState()
            isStateLoading = false
        }
        is CreateCategoryState.Error -> {
            CustomToast(
                context = LocalContext.current,
                message = stringResource(R.string.error)
            )
            viewModel.resetCategoryState()
        }
        else -> {}
    }

    AddCategoryScreenContent(
        modifier = modifier,
        categoryNameVal = viewModel.categoryNameVal,
        onCategoryChanged = { viewModel.updateCategoryName(it) },
        categoryFieldError = viewModel.categoryFieldError,
        createCategoryOnClick = {
            viewModel.createCategory()
            focusManager.clearFocus()
        },
        isStateLoading = isStateLoading,
        textFieldLabel = stringResource(R.string.category_name),
        buttonText = stringResource(R.string.create_category),
        textFieldErrorText = stringResource(R.string.text_field_error)
    )
}

@Composable
private fun AddCategoryScreenContent(
    modifier: Modifier,
    categoryNameVal: String,
    onCategoryChanged: (String) -> Unit,
    categoryFieldError: Boolean,
    createCategoryOnClick: () -> Unit,
    isStateLoading: Boolean,
    textFieldLabel: String,
    buttonText: String,
    textFieldErrorText: String
) {
    ResponsiveContent(
        modifier = modifier,
        categoryNameVal = categoryNameVal,
        onCategoryChanged = onCategoryChanged,
        categoryFieldError = categoryFieldError,
        createCategoryOnClick = createCategoryOnClick,
        isStateLoading = isStateLoading,
        textFieldLabel = textFieldLabel,
        buttonText = buttonText,
        textFieldErrorText = textFieldErrorText
    )
}

@Composable
private fun ResponsiveContent(
    modifier: Modifier,
    categoryNameVal: String,
    onCategoryChanged: (String) -> Unit,
    categoryFieldError: Boolean,
    createCategoryOnClick: () -> Unit,
    isStateLoading: Boolean,
    textFieldLabel: String,
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
            Spacer(modifier = modifier.height(64.dp))
            CustomTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = categoryNameVal,
                onValueChange = { onCategoryChanged(it) },
                labelText = textFieldLabel,
                isError = categoryFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        createCategoryOnClick()
                    }
                ),
                errorMessage = textFieldErrorText
            )
            Spacer(modifier = modifier.height(32.dp))
            CustomButton(
                modifier = modifier,
                onClick = createCategoryOnClick,
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
                    value = categoryNameVal,
                    onValueChange = { onCategoryChanged(it) },
                    labelText = textFieldLabel,
                    isError = categoryFieldError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            createCategoryOnClick()
                        }
                    ),
                    errorMessage = textFieldErrorText
                )
                Spacer(modifier = modifier.height(32.dp))
                CustomButton(
                    modifier = modifier,
                    onClick = createCategoryOnClick,
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
        painter = painterResource(id = R.drawable.ic_undraw_bookshelves_re_lxoy),
        contentDescription = "kelime ekleme görseli",
        contentScale = ContentScale.Fit
    )
}
