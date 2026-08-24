package com.lionsgates.notes.domain.use_case

data class NotesUseCases(
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val getNote: GetNote,
    val getNotes: GetNotes
)
