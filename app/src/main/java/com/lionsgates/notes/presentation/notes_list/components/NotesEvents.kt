package com.lionsgates.notes.presentation.notes_list.components

import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.util.NoteOrder

sealed class NotesEvents {

    data class Order(val noteOrder: NoteOrder) : NotesEvents()

    data class DeleteNote(val note: Note) : NotesEvents()

    data object RestoreNote : NotesEvents()

    data object ToggleOrderSection : NotesEvents()
}