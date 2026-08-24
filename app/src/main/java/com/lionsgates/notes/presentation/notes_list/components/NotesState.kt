package com.lionsgates.notes.presentation.notes_list.components

import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.util.NoteOrder
import com.lionsgates.notes.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
