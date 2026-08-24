package com.lionsgates.notes.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.lionsgates.notes.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table WHERE id = :id")
    suspend fun getNoteById(id: Int) : Note?

    @Query("SELECT * FROM notes_table")
    fun getNotes() : Flow<List<Note>>

    companion object {
        const val NOTES_DATABASE = "Note.db"
    }
}