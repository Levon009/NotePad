package com.lionsgates.notes.core.swipe_components.swipeActions

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeItemWithActions(
    isRevealed: Boolean,
    onExpended: () -> Unit,
    onCollapsed: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    var job by remember {
        mutableStateOf<Job?>(null)
    }
    var contentWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }

    DisposableEffect(Unit) {
        onDispose {
            job?.cancel()
        }
    }

    LaunchedEffect(key1 = isRevealed, key2 = contentWidth) {
        if (isRevealed) {
            offset.animateTo(contentWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.onSizeChanged {
                contentWidth = it.width.toFloat()
            }
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 70.dp)
                .offset {
                    IntOffset(offset.value.roundToInt(), 0)
                }
                .pointerInput(key1 = contentWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            job = scope.launch {
                                val newOffset = (offset.value + dragAmount).coerceIn(0f, contentWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            job = when {
                                offset.value > contentWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contentWidth)
                                        onExpended()
                                    }
                                }
                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}