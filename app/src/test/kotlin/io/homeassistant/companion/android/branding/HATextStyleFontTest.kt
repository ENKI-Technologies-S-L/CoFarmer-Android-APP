package io.homeassistant.companion.android.branding

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.homeassistant.companion.android.HiltComponentActivity
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.theme.HATextStyle
import io.homeassistant.companion.android.common.compose.theme.SoraFontFamily
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Brand typography: Sora on headings only, body text keeps the default (Roboto).
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class HATextStyleFontTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private fun capture(block: @androidx.compose.runtime.Composable () -> TextStyle): TextStyle {
        lateinit var style: TextStyle
        composeTestRule.setContent { style = block() }
        composeTestRule.waitForIdle()
        return style
    }

    @Test
    fun `headline uses Sora`() {
        assertEquals(SoraFontFamily, capture { HATextStyle.Headline }.fontFamily)
    }

    @Test
    fun `headline medium uses Sora`() {
        assertEquals(SoraFontFamily, capture { HATextStyle.HeadlineMedium }.fontFamily)
    }

    @Test
    fun `body does not use Sora`() {
        assertNotEquals(SoraFontFamily, capture { HATextStyle.Body }.fontFamily)
    }

    @Test
    fun `Sora font resources load`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertNotNull(ResourcesCompat.getFont(context, commonR.font.sora_semibold))
        assertNotNull(ResourcesCompat.getFont(context, commonR.font.sora_bold))
    }
}
