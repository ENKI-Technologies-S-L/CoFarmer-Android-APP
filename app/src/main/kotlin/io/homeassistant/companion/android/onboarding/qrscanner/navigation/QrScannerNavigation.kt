package io.homeassistant.companion.android.onboarding.qrscanner.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.homeassistant.companion.android.onboarding.qrscanner.QrScannerScreen
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Navigation route for the QR code scanner screen.
 */
@Serializable
internal object QrScannerRoute

/**
 * Navigates to the QR scanner screen.
 *
 * @param navOptions Optional navigation options
 */
internal fun NavController.navigateToQrScanner(navOptions: NavOptions? = null) {
    navigate(route = QrScannerRoute, navOptions)
}

/**
 * Adds the QR scanner screen to the navigation graph.
 *
 * @param onBackClick Callback when user presses back
 * @param onUrlScanned Callback when a valid URL is scanned from QR code
 * @param onManualSetupClick Callback when user chooses manual URL entry
 */
internal fun NavGraphBuilder.qrScannerScreen(
    onBackClick: () -> Unit,
    onUrlScanned: (URL) -> Unit,
    onManualSetupClick: () -> Unit,
) {
    composable<QrScannerRoute> {
        QrScannerScreen(
            onBackClick = onBackClick,
            onUrlScanned = onUrlScanned,
            onManualSetupClick = onManualSetupClick,
        )
    }
}
