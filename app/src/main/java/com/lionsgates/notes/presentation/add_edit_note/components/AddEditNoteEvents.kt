package com.lionsgates.notes.presentation.add_edit_note.components

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvents {

    data class EnteredTitle(val value: String) : AddEditNoteEvents()

    data class EnteredContent(val value: String) : AddEditNoteEvents()

    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvents()

    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvents()

    data class ChangeColor(val color: Int) : AddEditNoteEvents()

    data object SaveNote : AddEditNoteEvents()
}