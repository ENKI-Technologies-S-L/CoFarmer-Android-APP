package io.homeassistant.companion.android.onboarding.qrscanner.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import io.homeassistant.companion.android.onboarding.qrscanner.QrScannerScreen
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Navigation route for the QR code scanner screen.
 */
@Serializable
object QrScannerRoute

/**
 * Navigates to the QR scanner screen.
 * Uses launchSingleTop to prevent creating duplicate instances when already at QrScannerRoute.
 * Uses popUpTo to clear any intermediate screens from the backstack.
 *
 * @param navOptions Optional navigation options (launchSingleTop is always enabled)
 */
internal fun NavController.navigateToQrScanner(navOptions: NavOptions? = null) {
    val options = navOptions ?: navOptions {
        launchSingleTop = true
        popUpTo(QrScannerRoute) {
            inclusive = false
        }
    }
    navigate(route = QrScannerRoute, options)
}

/**
 * Adds the QR scanner screen to the navigation graph.
 *
 * @param onBackClick Callback when user presses back
 * @param onUrlScanned Callback when a valid URL is scanned from QR code
 * @param onManualSetupClick Callback when user chooses manual URL entry
 * @param onNetworkDiscoveryClick Optional callback when user chooses network discovery
 */
internal fun NavGraphBuilder.qrScannerScreen(
    onBackClick: () -> Unit,
    onUrlScanned: (URL) -> Unit,
    onManualSetupClick: () -> Unit,
    onNetworkDiscoveryClick: (() -> Unit)? = null,
) {
    composable<QrScannerRoute> {
        QrScannerScreen(
            onBackClick = onBackClick,
            onUrlScanned = onUrlScanned,
            onManualSetupClick = onManualSetupClick,
            onNetworkDiscoveryClick = onNetworkDiscoveryClick,
        )
    }
}
