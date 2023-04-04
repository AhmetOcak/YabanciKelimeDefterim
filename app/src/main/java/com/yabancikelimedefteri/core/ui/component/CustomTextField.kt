package com.yabancikelimedefteri.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yabancikelimedefteri.R

@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = labelText) },
        maxLines = 1,
        shape = RoundedCornerShape(20),
        isError = isError,
        singleLine = true,
        trailingIcon = if (isError) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_error),
                    contentDescription = "Bu alanı boş bırakamazsın",
                    tint = MaterialTheme.colors.error
                )
            }
        } else null,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
    if (isError) {
        Text(
            modifier = modifier.padding(top = 4.dp, start = 16.dp),
            text = "Bu alanı boş bırakamazsın",
            color = MaterialTheme.colors.error,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}