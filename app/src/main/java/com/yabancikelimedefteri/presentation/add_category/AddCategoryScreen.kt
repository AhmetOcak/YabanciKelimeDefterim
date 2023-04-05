package com.yabancikelimedefteri.presentation.add_category

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.ui.component.CustomButton
import com.yabancikelimedefteri.core.ui.component.CustomTextField
import com.yabancikelimedefteri.core.ui.component.CustomToast
import com.yabancikelimedefteri.presentation.main.OrientationState

@Composable
fun AddCategoryScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {

    val viewModel: AddCategoryViewModel = hiltViewModel()

    BackHandler {
        onNavigateBack()
    }

    val createCategoryState by viewModel.createCategoryState.collectAsState()

    AddCategoryScreenContent(
        modifier = modifier,
        createCategoryState = createCategoryState,
        resetCategoryState = { viewModel.resetCategoryState() },
        categoryNameVal = viewModel.categoryNameVal,
        onCategoryChanged = { viewModel.updateCategoryName(it) },
        categoryFieldError = viewModel.categoryFieldError,
        createCategoryOnClick = { viewModel.createCategory() }
    )
}

@Composable
private fun AddCategoryScreenContent(
    modifier: Modifier,
    createCategoryState: CreateCategoryState,
    resetCategoryState: () -> Unit,
    categoryNameVal: String,
    onCategoryChanged: (String) -> Unit,
    categoryFieldError: Boolean,
    createCategoryOnClick: () -> Unit
) {
    when (createCategoryState) {
        is CreateCategoryState.Nothing -> {
            ResponsiveContent(
                modifier = modifier,
                categoryNameVal = categoryNameVal,
                onCategoryChanged = onCategoryChanged,
                categoryFieldError = categoryFieldError,
                createCategoryOnClick = createCategoryOnClick
            )
        }
        is CreateCategoryState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is CreateCategoryState.Success -> {
            CustomToast(
                context = LocalContext.current,
                message = "Kelime defterinize başarılı bir şekilde bir kategori eklediniz."
            )
            resetCategoryState()
        }
        is CreateCategoryState.Error -> {
            CustomToast(
                context = LocalContext.current,
                message = "Hay aksi!! Bir şeyler ters gitti. Lütfen daha sonra tekrar dene."
            )
            resetCategoryState()
        }
    }
}

@Composable
private fun ResponsiveContent(
    modifier: Modifier,
    categoryNameVal: String,
    onCategoryChanged: (String) -> Unit,
    categoryFieldError: Boolean,
    createCategoryOnClick: () -> Unit
) {
    if (OrientationState.orientation.value == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
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
                labelText = "Kategori ismi",
                isError = categoryFieldError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        createCategoryOnClick()
                    }
                )
            )
            Spacer(modifier = modifier.height(32.dp))
            CustomButton(
                modifier = modifier,
                onClick = createCategoryOnClick,
                buttonText = "Kategoriyi oluştur"
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
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
                    labelText = "Kategori ismi",
                    isError = categoryFieldError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            createCategoryOnClick()
                        }
                    )
                )
                Spacer(modifier = modifier.height(32.dp))
                CustomButton(
                    modifier = modifier,
                    onClick = createCategoryOnClick,
                    buttonText = "Kategoriyi oluştur"
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
