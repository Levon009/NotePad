package com.lionsgates.notes.domain.use_case

import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.repository.NotesRepository

class GetNote(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Int) : Note? {
        return repository.getNoteById(id)
    }
}