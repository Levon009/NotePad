package com.lionsgates.notes.core.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ScrollToTopButton(
    state: LazyListState,
    isEnabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var job by remember {
        mutableStateOf<Job?>(null)
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            job?.cancel()
        }
    }
    val isScrollToTopButtonShown by remember(key1 = isEnabled) {
        derivedStateOf {
            state.firstVisibleItemIndex >= 3 && isEnabled
        }
    }

    if (isScrollToTopButtonShown) {
        FloatingActionButton(
            onClick = {
                job = scope.launch {
                    state.animateScrollToItem(0)
                }
            },
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll to top"
            )
        }
    }
}