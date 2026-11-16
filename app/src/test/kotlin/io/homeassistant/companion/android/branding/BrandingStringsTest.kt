package io.homeassistant.companion.android.branding

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import io.homeassistant.companion.android.common.R as commonR
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The app is branded "CoFarmer Assistant", while the hub, cloud and server keep the "CoFarmer" name.
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class BrandingStringsTest {

    private fun getString(@StringRes id: Int): String =
        ApplicationProvider.getApplicationContext<Context>().getString(id)

    @Test
    fun `app name is CoFarmer Assistant`() {
        assertEquals("CoFarmer Assistant", getString(commonR.string.app_name))
    }

    @Test
    fun `welcome title uses app name`() {
        assertEquals("Welcome to CoFarmer Assistant", getString(commonR.string.hub_setup_title))
    }

    @Test
    fun `app-referring permission text is renamed`() {
        assertTrue(
            getString(commonR.string.notification_permission_summary)
                .startsWith("CoFarmer Assistant does not have permission"),
        )
    }

    @Test
    fun `hub and server strings keep the CoFarmer name`() {
        assertEquals("Do you have a CoFarmer Hub?", getString(commonR.string.hub_setup_question))
        assertEquals("Unable to connect to CoFarmer.", getString(commonR.string.error_connection_failed))
        assertEquals("Use CoFarmer Cloud", getString(commonR.string.input_cloud))
    }

    @Test
    @Config(qualifiers = "es")
    fun `spanish app-referring text is renamed`() {
        assertTrue(getString(commonR.string.suggestion_notifications_summary).contains("CoFarmer Assistant"))
    }

    @Test
    @Config(qualifiers = "es")
    fun `spanish hub text keeps the CoFarmer name`() {
        assertEquals("No se puede conectar con CoFarmer.", getString(commonR.string.error_connection_failed))
    }
}
