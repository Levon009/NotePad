package com.lionsgates.notes.domain.useCases

import com.google.common.truth.Truth.assertThat
import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.data.repository.TestNotesRepositoryImpl
import com.lionsgates.notes.domain.use_case.GetNotes
import com.lionsgates.notes.domain.util.NoteOrder
import com.lionsgates.notes.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotesTest {

    private lateinit var getNotes: GetNotes

    private lateinit var testNotesRepositoryImpl: TestNotesRepositoryImpl

    @Before
    fun setUp() {
        testNotesRepositoryImpl = TestNotesRepositoryImpl()
        getNotes = GetNotes(testNotesRepositoryImpl)

        val notes = mutableListOf<Note>()

        ('a'..'z').forEachIndexed { index, ch ->
            notes.add(
                Note(
                    title = ch.toString(),
                    content = ch.toString(),
                    timestamp = index.toLong(),
                    color = index,
                    isOptionalRevealed = false
                )
            )
        }

        notes.shuffle()
        runBlocking {
            notes.forEach { note ->
                testNotesRepositoryImpl.insertNote(note)
            }
        }
    }

    @Test
    fun order_notes_by_title_ascending() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i + 1].title)
        }
    }

    @Test
    fun order_notes_by_title_descending() = runBlocking {
        val notes=  getNotes(NoteOrder.Title(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i + 1].title)
        }
    }

    @Test
    fun order_notes_by_date_ascending() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun order_notes_by_date_descending() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun order_notes_by_color_ascending() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i + 1].color)
        }
    }

    @Test
    fun order_notes_by_color_descending() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i + 1].color)
        }
    }
}