package com.lionsgates.notes.presentation.add_edit_note.components.ui

sealed class UIEvents {

    data class ShowSnackBar(val message: String) : UIEvents()

    data object SaveNote : UIEvents()
}