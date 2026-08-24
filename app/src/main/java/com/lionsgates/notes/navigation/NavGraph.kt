package com.lionsgates.notes.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lionsgates.notes.presentation.screens.AddEditNoteScreen
import com.lionsgates.notes.presentation.screens.NotesScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Notes,

        enterTransition = {
            fadeIn(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(300)
            )
        },
        exitTransition = {
            fadeOut(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
            )
        }
    ) {
        composable<Routes.Notes> {
            NotesScreen(navController = navController)
        }

        composable<Routes.AddEditNote> {
            val args = it.toRoute<Routes.AddEditNote>()
            AddEditNoteScreen(
                navController = navController,
                noteColor = args.noteColor
            )
        }
    }
}