package io.homeassistant.companion.android.launch

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.homeassistant.companion.android.HiltComponentActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class BrandingSplashScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Test
    fun `splash shows CoFarmer Assistant wordmark, tagline and logo`() {
        composeTestRule.setContent { BrandingSplashScreen() }

        composeTestRule.onNodeWithText("CoFarmer").assertExists()
        composeTestRule.onNodeWithText("Assistant").assertExists()
        composeTestRule.onNodeWithText("Control your farm from anywhere").assertExists()
        composeTestRule.onNodeWithText("by ENKITEK").assertExists()
        composeTestRule.onNodeWithContentDescription("CoFarmer Assistant logo").assertExists()
    }

    @Test
    fun `splash no longer shows the old uppercase wordmark`() {
        composeTestRule.setContent { BrandingSplashScreen() }

        composeTestRule.onNodeWithText("COFARMER").assertDoesNotExist()
    }

    @Test
    @Config(qualifiers = "es")
    fun `splash tagline is translated to Spanish`() {
        composeTestRule.setContent { BrandingSplashScreen() }

        composeTestRule.onNodeWithText("Controla tu granja desde cualquier lugar").assertExists()
        composeTestRule.onNodeWithContentDescription("Logo de CoFarmer Assistant").assertExists()
    }
}
