package com.lionsgates.notes.presentation.screens

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lionsgates.notes.R
import com.lionsgates.notes.core.sb.ObserveSB
import com.lionsgates.notes.core.sb.controller.SBController
import com.lionsgates.notes.core.sb.data.SBEvent
import com.lionsgates.notes.core.util.TestTag
import com.lionsgates.notes.data.model.Note
import com.lionsgates.notes.presentation.add_edit_note.AddEditNoteViewModel
import com.lionsgates.notes.presentation.add_edit_note.components.AddEditNoteEvents
import com.lionsgates.notes.presentation.add_edit_note.components.ui.TransparentHintTextFields
import com.lionsgates.notes.presentation.add_edit_note.components.ui.UIEvents
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavHostController,
    noteColor: Int
) {
    val fontFamily = FontFamily(Font(R.font.gothica1_black, FontWeight.Thin))
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val viewModel = hiltViewModel<AddEditNoteViewModel>()
    val noteTitle = viewModel.noteTitle.value
    val noteContent = viewModel.noteContent.value
    val noteBackgroundColor = remember {
        Animatable(
            initialValue = Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UIEvents.ShowSnackBar -> {
                    SBController.sendEvent(
                        event = SBEvent(
                            message = event.message
                        )
                    )
                }
                is UIEvents.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    ObserveSB(snackBarHostState = snackBarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .padding(start = 10.dp)
                    .padding(horizontal = 30.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home"
                    )
                }
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditNoteEvents.SaveNote)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save note"
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(noteBackgroundColor.value)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(elevation = 15.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundColor.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvents.ChangeColor(colorInt))
                            }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            TransparentHintTextFields(
                text = noteTitle.text,
                hint = noteTitle.hint,
                isHintVisible = noteTitle.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium,
                fontFamily = fontFamily,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvents.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvents.ChangeTitleFocus(it))
                },
                textTags = TestTag.TITLE_TEXT_FIELD
            )
            Spacer(modifier = Modifier.height(14.dp))
            TransparentHintTextFields(
                text = noteContent.text,
                hint = noteContent.hint,
                isHintVisible = noteContent.isHintVisible,
                singleLine = false,
                fontFamily = fontFamily,
                textStyle = LocalTextStyle.current.copy(
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    fontFamily = FontFamily.Serif
                ),
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvents.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvents.ChangeContentFocus(it))
                },
                textTags = TestTag.CONTENT_TEXT_FIELD,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}