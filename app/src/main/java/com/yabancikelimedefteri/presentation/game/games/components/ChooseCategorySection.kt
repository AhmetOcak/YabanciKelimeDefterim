package com.yabancikelimedefteri.presentation.game.games.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.word.CategoryWithId
import com.yabancikelimedefteri.presentation.game.models.GameCategoryItem

const val ALL_CATEGORY_ID = -1

@Composable
fun ChooseWordCategorySection(
    modifier: Modifier,
    categories: List<CategoryWithId>,
    isGameReadyToLaunch: Boolean,
    launchTheGame: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    isCategorySelected: (Int) -> Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.select_category),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        LazyVerticalGrid(
            modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 2),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GameCategoryItem(
                    categoryName = stringResource(R.string.category_all),
                    categoryId = ALL_CATEGORY_ID,
                    onClick = onCategoryClick,
                    isSelected = isCategorySelected(ALL_CATEGORY_ID)
                )
            }
            items(categories) {
                GameCategoryItem(
                    categoryName = it.categoryName,
                    categoryId = it.categoryId,
                    onClick = onCategoryClick,
                    isSelected = isCategorySelected(it.categoryId)
                )
            }
        }
        Button(onClick = launchTheGame, enabled = isGameReadyToLaunch) {
            Text(text = stringResource(R.string.start_the_game))
        }
    }
}