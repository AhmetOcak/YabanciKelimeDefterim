package com.yabancikelimedefteri.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiActionBar(
    upPress: () -> Unit,
    title: String,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onMenuItemClick: (SortType) -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            AnimatedVisibility(visible = showSearch, enter = slideInHorizontally()) {
                SearchField(
                    searchValue = searchValue,
                    onSearchValueChange = onSearchValueChange
                )
            }
            if (!showSearch) {
                Text(text = title)
            }
        },
        navigationIcon = {
            BackButton(onClick = upPress)
        },
        actions = {
            if (!showSearch) {
                IconButton(onClick = { showSearch = true }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            }
            IconButton(onClick = { expandedDropDownMenu = true }) {
                Icon(imageVector = Icons.AutoMirrored.Default.Sort, contentDescription = null)
            }
            SortMenu(
                expanded = expandedDropDownMenu,
                onDismissRequest = {
                    expandedDropDownMenu = false
                },
                onMenuItemClick = { sortType ->
                    expandedDropDownMenu = false
                    onMenuItemClick(sortType)
                }
            )
        }
    )
}

@Composable
private fun SearchField(
    searchValue: String,
    onSearchValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchValue,
        onValueChange = onSearchValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .background(color = TextFieldDefaults.colors().focusedContainerColor)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (searchValue.isBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = TextFieldDefaults.colors().unfocusedPlaceholderColor
                        )
                        Text(
                            text = stringResource(id = R.string.search),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextFieldDefaults.colors().unfocusedPlaceholderColor
                        )
                    }
                }
                innerTextField()
            }
        },
        textStyle = TextStyle(color = TextFieldDefaults.colors().focusedTextColor)
    )
}

@Composable
private fun SortMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onMenuItemClick: (SortType) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.sort_alphabetically))
            },
            onClick = {
                onMenuItemClick(SortType.ALPHABETICALLY)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.sort_importance_level))
            },
            onClick = {
                onMenuItemClick(SortType.IMPORTANCE_LEVEL)
            }
        )
    }
}

enum class SortType {
    ALPHABETICALLY,
    IMPORTANCE_LEVEL
}