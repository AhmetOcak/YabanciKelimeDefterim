package com.yabancikelimedefteri.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yabancikelimedefteri.R
import com.yabancikelimedefteri.domain.model.DialogType
import com.yabancikelimedefteri.domain.model.ImportanceLevel
import com.yabancikelimedefteri.presentation.word.ImportanceLevelPicker

@Composable
fun AddOrUpdateWordDialog(
    foreignWordValue: String,
    importanceLevel: Int,
    onForeignWordValueChange: (String) -> Unit,
    meaningWordValue: String,
    onMeaningWordValueChange: (String) -> Unit,
    onApply: () -> Unit,
    onDismissRequest: () -> Unit,
    onImportanceLevelClick: (Int) -> Unit,
    dialogType: DialogType = DialogType.Add
) {
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onApply,
                enabled = foreignWordValue.isNotBlank() && meaningWordValue.isNotBlank()
            ) {
                Text(text = stringResource(id = if (dialogType == DialogType.Add) R.string.add else R.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(id = if (dialogType == DialogType.Add) R.string.add_word else R.string.update_word),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = foreignWordValue,
                    onValueChange = onForeignWordValueChange,
                    label = {
                        Text(text = stringResource(id = R.string.foreign_word))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = meaningWordValue,
                    onValueChange = onMeaningWordValueChange,
                    label = {
                        Text(text = stringResource(id = R.string.meaning))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = if (foreignWordValue.isNotBlank() && meaningWordValue.isNotBlank()) {
                            { onApply() }
                        } else null
                    ),
                    supportingText = {
                        Text(text = stringResource(id = R.string.add_word_info))
                    }
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Column(modifier = Modifier.selectableGroup()) {
                    Text(
                        text = stringResource(id = R.string.word_importance_level),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    ImportanceLevelPicker(
                        isSelected = importanceLevel == ImportanceLevel.Green.ordinal,
                        text = stringResource(id = R.string.low),
                        onClick = remember { { onImportanceLevelClick(ImportanceLevel.Green.ordinal) } }
                    )
                    ImportanceLevelPicker(
                        isSelected = importanceLevel == ImportanceLevel.Yellow.ordinal,
                        text = stringResource(id = R.string.medium),
                        onClick = remember { { onImportanceLevelClick(ImportanceLevel.Yellow.ordinal) } }
                    )
                    ImportanceLevelPicker(
                        isSelected = importanceLevel == ImportanceLevel.Red.ordinal,
                        text = stringResource(id = R.string.high),
                        onClick = remember { { onImportanceLevelClick(ImportanceLevel.Red.ordinal) } }
                    )
                }
            }
        }
    )
}