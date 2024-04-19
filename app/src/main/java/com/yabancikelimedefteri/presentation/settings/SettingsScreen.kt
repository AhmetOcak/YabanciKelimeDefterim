package com.yabancikelimedefteri.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.core.navigation.HomeSections
import com.yabancikelimedefteri.core.ui.component.MyVocabularyNavigationBar
import com.yabancikelimedefteri.core.ui.theme.color_schemes.CustomColorScheme
import com.yabancikelimedefteri.core.ui.theme.color_schemes.NatureColorScheme
import com.yabancikelimedefteri.presentation.settings.models.ColorSchemesSettingItem
import com.yabancikelimedefteri.presentation.settings.models.SettingItem
import com.yabancikelimedefteri.presentation.settings.models.SettingSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToRoute: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.settings))
            })
        },
        bottomBar = {
            MyVocabularyNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.SETTINGS.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        SettingsScreenContent(
            modifier = Modifier.padding(paddingValues),
            isDarkThemeChecked = false,
            onDarkThemeCheckedChange = {},
            isDynamicColorChecked = false,
            onDynamicColorCheckedChange = {},
            onColorSchemeClick = {},
            currentScheme = NatureColorScheme,
            isThinListTypeChecked = false,
            onThinListTypeCheckedChange = {},
            onRateAppClick = {},
            onShareAppClick = {}
        )
    }
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
    isDarkThemeChecked: Boolean,
    onDarkThemeCheckedChange: (Boolean) -> Unit,
    isDynamicColorChecked: Boolean,
    onDynamicColorCheckedChange: (Boolean) -> Unit,
    onColorSchemeClick: (CustomColorScheme) -> Unit,
    currentScheme: CustomColorScheme,
    isThinListTypeChecked: Boolean,
    onThinListTypeCheckedChange: (Boolean) -> Unit,
    onRateAppClick: () -> Unit,
    onShareAppClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingSection(title = stringResource(id = R.string.themes)) {
            SettingItem(
                nameId = Settings.DARK_THEME.nameId,
                icon = Settings.DARK_THEME.icon,
                checked = isDarkThemeChecked,
                onCheckedChange = onDarkThemeCheckedChange
            )
            ColorSchemesSettingItem(
                nameId = Settings.COLOR_THEMES.nameId,
                icon = Settings.COLOR_THEMES.icon,
                onClick = onColorSchemeClick,
                currentScheme = currentScheme,
                isDarkTheme = isDarkThemeChecked
            )
            SettingItem(
                nameId = Settings.DYNAMIC_COLOR.nameId,
                icon = Settings.DYNAMIC_COLOR.icon,
                checked = isDynamicColorChecked,
                onCheckedChange = onDynamicColorCheckedChange
            )
        }
        SettingSection(title = stringResource(id = R.string.word_lists)) {
            SettingItem(
                nameId = Settings.LIST_TYPE.nameId,
                icon = Settings.LIST_TYPE.icon,
                checked = isThinListTypeChecked,
                onCheckedChange = onThinListTypeCheckedChange
            )
        }
        SettingSection(title = stringResource(id = R.string.app_name)) {
            SettingItem(
                nameId = Settings.RATE_APP.nameId,
                icon = Settings.RATE_APP.icon,
                onClick = onRateAppClick
            )
            SettingItem(
                nameId = Settings.SHARE_APP.nameId,
                icon = Settings.SHARE_APP.icon,
                onClick = onShareAppClick
            )
        }
    }
}

