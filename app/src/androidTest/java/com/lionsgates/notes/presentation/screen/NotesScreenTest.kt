package com.lionsgates.notes.presentation.screen

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.lionsgates.notes.MainActivity
import com.lionsgates.notes.core.util.TestTag
import com.lionsgates.notes.di.AppModule
import com.lionsgates.notes.navigation.Routes
import com.lionsgates.notes.presentation.screens.NotesScreen
import com.lionsgates.notes.ui.theme.NotesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    val context: Context = ApplicationProvider.getApplicationContext()

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
                }
            }
        }
    }

    @Test
    fun click_toggle_order_section_is_visible() {
        composeRule.onNodeWithTag(TestTag.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTag.ORDER_SECTION).assertIsDisplayed()
    }
}