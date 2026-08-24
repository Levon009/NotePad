package com.lionsgates.notes.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lionsgates.notes.data.model.InvalidNoteException
import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.domain.use_case.NotesUseCases
import com.lionsgates.notes.presentation.add_edit_note.components.AddEditNoteEvents
import com.lionsgates.notes.presentation.add_edit_note.components.NoteTextFieldState
import com.lionsgates.notes.presentation.add_edit_note.components.ui.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UIEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    notesUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.intValue = note.color
                    }
                }
            }
        }
    }

    fun onEvent(events: AddEditNoteEvents) {
        when(events) {
            is AddEditNoteEvents.ChangeColor -> {
                _noteColor.intValue = events.color
            }
            is AddEditNoteEvents.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = noteContent.value.text.isBlank() && !events.focusState.isFocused
                )
            }
            is AddEditNoteEvents.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = noteTitle.value.text.isBlank() && !events.focusState.isFocused
                )
            }
            is AddEditNoteEvents.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = events.value
                )
            }
            is AddEditNoteEvents.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = events.value
                )
            }
            AddEditNoteEvents.SaveNote -> {
                viewModelScope.launch {
                    try {
                        notesUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                isOptionalRevealed = false
                            )
                        )
                        _eventFlow.emit(UIEvents.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UIEvents.ShowSnackBar(
                                message = e.message ?: "Note couldn't be saved!"
                            )
                        )
                    }
                }
            }
        }
    }
}