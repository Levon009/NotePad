package com.lionsgates.notes.core.util

import android.content.Context
import android.content.Intent

fun shareText(
    context: Context,
    text: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    if (intent.resolveActivity(context.applicationContext.packageManager) != null) {
        context.startActivity(Intent.createChooser(intent, "Choose app"))
    }
}