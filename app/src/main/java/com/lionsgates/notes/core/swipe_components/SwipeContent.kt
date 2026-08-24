package com.lionsgates.notes.core.swipe_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.lionsgates.notes.data.model.Note
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SwipeToDeleteContent(
    note: Note,
    animDuration: Int = 500,
    onRemoved: (Note) -> Unit,
    content: @Composable (Note) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { distance ->
            distance * 0.5f
        }
    )

    LaunchedEffect(key1 = dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            isRemoved = true
        }
    }
    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animDuration.milliseconds)
            onRemoved(note)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(animDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = false,
            state = dismissState,
            backgroundContent = {
                SwipeToDeleteBG(dismissState = dismissState)
            },
            content = {
                content(note)
            }
        )
    }
}