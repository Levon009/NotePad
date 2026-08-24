package com.lionsgates.notes.core.sb.data

data class SBAction(
    val name: String,
    val action: () -> Unit
)