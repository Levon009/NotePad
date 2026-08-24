package com.lionsgates.notes.domain.use_case

import com.lionsgates.notes.data.model.InvalidNoteException
import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.repository.NotesRepository

class AddNote(
    private val repository: NotesRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty!")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty!")
        }
        repository.insertNote(note)
    }
}