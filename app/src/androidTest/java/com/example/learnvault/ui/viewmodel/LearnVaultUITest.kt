package com.example.learnvault.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learnvault.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LearnVaultUITest {

    // This rule actually launches your app's MainActivity on the emulator
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchFeature_filtersAndClearsCorrectly() {
        // 1. Verify we are on the Home screen
        composeTestRule.onNodeWithText("Your Learning Journey").assertIsDisplayed()

        // 2. Type into the search bar
        composeTestRule.onNodeWithText("Search topics...").performTextInput("Android Studio")

        // 3. Verify search results are displayed and the normal dashboard vanishes
        composeTestRule.onNodeWithText("Search Results").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your Learning Journey").assertDoesNotExist()

        // 4. Click the clear button (X)
        composeTestRule.onNodeWithContentDescription("Clear search").performClick()

        // 5. Verify the dashboard returned perfectly
        composeTestRule.onNodeWithText("Your Learning Journey").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Results").assertDoesNotExist()
    }

    @Test
    fun settingsScreen_navigatesAndDisplaysOptions() {
        // 1. Click the Settings icon in the Top App Bar
        // (Make sure the gear icon from Sprint 9 has contentDescription = "Settings")
        composeTestRule.onNodeWithContentDescription("Settings", ignoreCase = true).performClick()

        // 2. Verify we are on the Settings Screen by checking for options
        composeTestRule.onNodeWithText("Appearance").assertIsDisplayed()
        composeTestRule.onNodeWithText("System").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dark").assertIsDisplayed()

        composeTestRule.onNodeWithText("Reading").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compact").assertIsDisplayed()

        // 3. Navigate back
        composeTestRule.onNodeWithContentDescription("Navigate back", ignoreCase = true).performClick()

        // 4. Verify we returned home
        composeTestRule.onNodeWithText("Your Learning Journey").assertIsDisplayed()
    }

    @Test
    fun topicDetailScreen_rendersOptionalContent() {
        // 1. Search for the updated "Variables & Mutability" topic
        composeTestRule.onNodeWithText("Search topics...").performTextInput("Variables")

        // 2. Click the NEW short description
        composeTestRule.onNodeWithText("Storing data safely in Kotlin.", substring = true).performClick()

        // 3. Verify the new hierarchy elements are generated in the UI
        // Use assertExists() because they might be scrolled off the bottom of the screen!
        composeTestRule.onNodeWithText("KEY TAKEAWAYS").assertExists()
        composeTestRule.onNodeWithText("EXPLANATION").assertExists()
        composeTestRule.onNodeWithText("EXAMPLE").assertExists()
        composeTestRule.onNodeWithText("My Notes").assertExists()
    }

    @Test
    fun topicDetailScreen_withoutOptionalContent_doesNotCrash() {
        // 1. Search for the simple topic we just added back
        composeTestRule.onNodeWithText("Search topics...").performTextInput("Android Studio")

        // 2. Click its short description
        composeTestRule.onNodeWithText("The official IDE.", substring = true).performClick()

        // 3. Verify it rendered the base content successfully
        composeTestRule.onNodeWithText("EXPLANATION").assertIsDisplayed()

        // 4. Verify it gracefully skipped the missing optional content without crashing
        composeTestRule.onNodeWithText("KEY TAKEAWAYS").assertDoesNotExist()
        composeTestRule.onNodeWithText("EXAMPLE").assertDoesNotExist()
    }
}