package com.lionsgates.notes.core.util

import android.content.Context
import android.content.Intent

fun sendEmail(
    context: Context,
    receiver: Array<String>,
    subject: String,
    content: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, receiver)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, content)
    }
    if (intent.resolveActivity(context.applicationContext.packageManager) != null) {
        context.startActivity(intent)
    }
}