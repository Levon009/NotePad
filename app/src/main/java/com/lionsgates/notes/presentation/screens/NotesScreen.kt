package com.lionsgates.notes.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lionsgates.notes.R
import com.lionsgates.notes.core.sb.ObserveSB
import com.lionsgates.notes.core.sb.controller.SBController
import com.lionsgates.notes.core.sb.data.SBAction
import com.lionsgates.notes.core.sb.data.SBEvent
import com.lionsgates.notes.core.swipe_components.SwipeToDeleteContent
import com.lionsgates.notes.core.swipe_components.swipeActions.SwipeItemWithActions
import com.lionsgates.notes.core.swipe_components.swipeActions.components.ActionIcon
import com.lionsgates.notes.core.util.ScrollToTopButton
import com.lionsgates.notes.core.util.TestTag
import com.lionsgates.notes.core.util.shareText
import com.lionsgates.notes.navigation.Routes
import com.lionsgates.notes.presentation.notes_list.NotesViewModel
import com.lionsgates.notes.presentation.notes_list.components.NotesEvents
import com.lionsgates.notes.presentation.notes_list.components.ui.NoteItem
import com.lionsgates.notes.presentation.notes_list.components.ui.OrderSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navController: NavHostController) {
    val fontFamily = FontFamily(Font(R.font.gothica1_black, FontWeight.Thin))
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val notesViewModel = hiltViewModel<NotesViewModel>()
    val state = notesViewModel.state.value

    ObserveSB(snackBarHostState = snackBarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(start = 18.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.AddEditNote())
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add note"
                    )
                }
                ScrollToTopButton(state = lazyListState)
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            ) {
                Text(
                    text = "Your note",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(
                    onClick = {
                        notesViewModel.onEvents(NotesEvents.ToggleOrderSection)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 100),
                    shrinkTowards = Alignment.Top
                ) + fadeOut()
            ) {
                OrderSection(
                    fontFamily = fontFamily,
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        notesViewModel.onEvents(NotesEvents.Order(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTag.ORDER_SECTION)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = state.notes,
                    key = { _, note ->
                        note.hashCode()
                    }
                ) { index, note ->
                    SwipeToDeleteContent(
                        note = note,
                        onRemoved = {
                            notesViewModel.onEvents(NotesEvents.DeleteNote(note))
                            deleteNoteSnackBar(
                                scope = scope,
                                notesViewModel = notesViewModel
                            )
                            navController.navigate(Routes.Notes)
                        }
                    ) {
                        SwipeItemWithActions(
                            isRevealed = note.isOptionalRevealed,
                            onExpended = {
                                state.notes.toMutableStateList()[index] = note.copy(isOptionalRevealed = true)
                            },
                            onCollapsed = {
                                state.notes.toMutableStateList()[index] = note.copy(isOptionalRevealed = false)
                            },
                            actions = {
                                ActionIcon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete note",
                                    backgroundColor = Color.Red,
                                    tint = Color.White,
                                    onClick = {
                                        notesViewModel.onEvents(NotesEvents.DeleteNote(note))
                                        deleteNoteSnackBar(
                                            scope = scope,
                                            notesViewModel = notesViewModel
                                        )
                                        navController.navigate(Routes.Notes)
                                    }
                                )
                                ActionIcon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    backgroundColor = Color.Yellow,
                                    tint = Color.Black,
                                    onClick = {
                                        shareText(
                                            context = context,
                                            text = note.content
                                        )
                                    }
                                )
                            }
                        ) {
                            NoteItem(
                                note = note,
                                fontFamily = fontFamily,
                                onDeleteClick = {
                                    notesViewModel.onEvents(NotesEvents.DeleteNote(note))
                                    deleteNoteSnackBar(
                                        scope = scope,
                                        notesViewModel = notesViewModel
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Routes.AddEditNote(
                                            notId = note.id!!,
                                            noteColor = note.color
                                        ))
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}

private fun deleteNoteSnackBar(
    scope: CoroutineScope,
    notesViewModel: NotesViewModel
) {
    scope.launch {
        SBController.sendEvent(
            event = SBEvent(
                message = "Note deleted!",
                action = SBAction(
                    name = "Undo",
                    action = {
                        notesViewModel.onEvents(NotesEvents.RestoreNote)
                    }
                )
            )
        )
    }
}