package com.yabancikelimedefteri.core.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

@Composable
fun CustomToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}