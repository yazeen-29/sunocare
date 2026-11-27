package com.sunocare.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoleNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun patientCanOpenDemoAndReturnToWelcome() {
        compose.onNodeWithText("I am a Patient").performClick()
        compose.onNodeWithText("Patient Mode").assertIsDisplayed()
        compose.onNodeWithText("Demo mode").assertIsDisplayed()
        compose.onNodeWithText("Monitoring is off").assertIsDisplayed()
        compose.onNodeWithText("Back to welcome").performClick()
        compose.onNodeWithText("I am a Patient").assertIsDisplayed()
    }

    @Test
    fun caregiverCanInspectDemoRequestAndReturnToWelcome() {
        compose.onNodeWithText("I am a Caregiver").performClick()
        compose.onNodeWithText("Caregiver Mode").assertIsDisplayed()
        compose.onNodeWithText("View water request").performClick()
        compose.onNodeWithText("Demo mode").assertIsDisplayed()
        compose.onNodeWithText("Could I have some water?").assertIsDisplayed()
        compose.onNodeWithText("Back to requests").performClick()
        compose.onNodeWithText("Caregiver Mode").assertIsDisplayed()
        compose.onNodeWithText("Back to welcome").performClick()
        compose.onNodeWithText("I am a Caregiver").assertIsDisplayed()
    }
}
