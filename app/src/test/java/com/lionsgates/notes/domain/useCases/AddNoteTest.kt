package com.lionsgates.notes.domain.useCases

import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.data.repository.TestNotesRepositoryImpl
import com.lionsgates.notes.domain.use_case.AddNote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat



class AddNoteTest {

    private lateinit var addNote: AddNote

    private lateinit var testNotesRepositoryImpl: TestNotesRepositoryImpl

    @Before
    fun setUp() {
        testNotesRepositoryImpl = TestNotesRepositoryImpl()
        addNote = AddNote(testNotesRepositoryImpl)
    }

    @Test
    fun add_note_and_check_if_it_added_correctly() : Unit = runBlocking {
        val note = Note(
            title = "",
            content = "",
            timestamp = System.currentTimeMillis(),
            color = 0,
            isOptionalRevealed = false
        )

        addNote.invoke(note)
        val notes = testNotesRepositoryImpl.getNotes().first()
        assertThat(notes).contains(note)
    }
}