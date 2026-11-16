package io.homeassistant.companion.android.branding

import android.app.Application
import android.content.Context
import androidx.annotation.ColorRes
import androidx.test.core.app.ApplicationProvider
import io.homeassistant.companion.android.common.R as commonR
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Brand palette taken from the CoFarmer logo gradient (#00C389 → #1E6BD6).
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class BrandingColorsTest {

    private fun color(@ColorRes id: Int): Int = ApplicationProvider.getApplicationContext<Context>().getColor(id)

    @Test
    fun `primary is logo green`() {
        assertEquals(0xFF00C389.toInt(), color(commonR.color.cofarmer_primary))
    }

    @Test
    fun `secondary is logo blue`() {
        assertEquals(0xFF1E6BD6.toInt(), color(commonR.color.cofarmer_secondary))
    }

    @Test
    fun `accent is gradient midpoint`() {
        assertEquals(0xFF0F97AF.toInt(), color(commonR.color.cofarmer_accent))
    }

    @Test
    fun `preference suggestion is primary at 12 percent`() {
        assertEquals(0x1F00C389, color(commonR.color.colorPreferenceSuggestion))
    }

    @Test
    @Config(qualifiers = "night")
    fun `primary is logo green in dark mode`() {
        assertEquals(0xFF00C389.toInt(), color(commonR.color.cofarmer_primary))
    }
}
