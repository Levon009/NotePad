package com.lionsgates.notes.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lionsgates.notes.MainActivity
import com.lionsgates.notes.core.util.TestTag
import com.lionsgates.notes.di.AppModule
import com.lionsgates.notes.navigation.Routes
import com.lionsgates.notes.presentation.screens.AddEditNoteScreen
import com.lionsgates.notes.presentation.screens.NotesScreen
import com.lionsgates.notes.ui.theme.NotesTheme
import dagger.Module
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Module
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            NotesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.Notes
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
        }
    }

    @Test
    fun saveNote_addAfterwords() {
        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithTag(TestTag.TITLE_TEXT_FIELD).performTextInput("test-title")
        composeRule.onNodeWithTag(TestTag.CONTENT_TEXT_FIELD).performTextInput("test-content")
        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        composeRule.onNodeWithText("test-title").performClick()

        composeRule.onNodeWithTag(TestTag.TITLE_TEXT_FIELD).assertTextEquals("test-title")
        composeRule.onNodeWithTag(TestTag.CONTENT_TEXT_FIELD).assertTextEquals("test-content")
        composeRule.onNodeWithTag(TestTag.TITLE_TEXT_FIELD).performTextInput("2")
        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("test_title2").assertIsDisplayed()
    }

    @Test
    fun saveNote_orderByTitleDescending() {
        for (i in 1..3) {
            composeRule.onNodeWithContentDescription("Add").performClick()

            composeRule.onNodeWithTag(TestTag.TITLE_TEXT_FIELD).performTextInput(i.toString())
            composeRule.onNodeWithTag(TestTag.CONTENT_TEXT_FIELD).performTextInput(i.toString())
            composeRule.onNodeWithContentDescription("Save").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()

        composeRule.onAllNodesWithTag(TestTag.NOTE_ITEM)[0].assertTextEquals("3")
        composeRule.onAllNodesWithTag(TestTag.NOTE_ITEM)[1].assertTextEquals("2")
        composeRule.onAllNodesWithTag(TestTag.NOTE_ITEM)[2].assertTextEquals("1")
    }
}