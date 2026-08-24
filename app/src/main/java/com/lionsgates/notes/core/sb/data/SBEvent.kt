package com.lionsgates.notes.core.sb.data

import androidx.compose.material3.SnackbarDuration

data class SBEvent(
    val message: String,
    val action: SBAction? = null,
    val duration: SnackbarDuration = SnackbarDuration.Long
)