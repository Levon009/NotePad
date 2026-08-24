package com.lionsgates.notes.di

import android.content.Context
import com.lionsgates.notes.data.data_source.NotesDatabase
import com.lionsgates.notes.data.repository.NotesRepositoryImpl
import com.lionsgates.notes.domain.repository.NotesRepository
import com.lionsgates.notes.domain.use_case.AddNote
import com.lionsgates.notes.domain.use_case.DeleteNote
import com.lionsgates.notes.domain.use_case.GetNote
import com.lionsgates.notes.domain.use_case.GetNotes
import com.lionsgates.notes.domain.use_case.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : NotesDatabase {
        return NotesDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providesRepository(db: NotesDatabase) : NotesRepository {
        return NotesRepositoryImpl(db.notesDao)
    }

    @Provides
    @Singleton
    fun providesUseCases(repository: NotesRepository) : NotesUseCases {
        return NotesUseCases(
            addNote = AddNote(repository),
            deleteNote = DeleteNote(repository),
            getNote = GetNote(repository),
            getNotes = GetNotes(repository)
        )
    }
}