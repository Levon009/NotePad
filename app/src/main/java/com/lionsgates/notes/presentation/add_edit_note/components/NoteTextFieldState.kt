package com.lionsgates.notes.presentation.add_edit_note.components

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = false
)
