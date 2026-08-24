package com.lionsgates.notes.domain.use_case

import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.repository.NotesRepository

class DeleteNote(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}