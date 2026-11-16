package io.homeassistant.companion.android.branding

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.util.GestureActionCategory
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

    private fun getString(@StringRes id: Int): String = ApplicationProvider.getApplicationContext<Context>().getString(id)

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
    fun `hub brand name is CoFarmer and is used for the hub frontend gesture category`() {
        assertEquals("CoFarmer", getString(commonR.string.hub_brand_name))
        assertEquals(commonR.string.hub_brand_name, GestureActionCategory.FRONTEND.description)
    }

    @Test
    fun `location disclosure names the server CoFarmer`() {
        assertTrue(getString(commonR.string.location_sharing_content_plain).contains("your self-hosted CoFarmer server"))
    }

    @Test
    fun `secure connection hint names the company Enkitek`() {
        assertEquals(
            "Your location data stays local and is never shared with Enkitek or third parties.",
            getString(commonR.string.location_secure_connection_hint),
        )
    }

    @Test
    @Config(qualifiers = "es")
    fun `spanish location texts name the server CoFarmer and the company Enkitek`() {
        assertTrue(getString(commonR.string.location_sharing_content_plain).contains("tu propio servidor CoFarmer\n"))
        assertTrue(getString(commonR.string.location_secure_connection_hint).contains("con Enkitek ni con terceros"))
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
