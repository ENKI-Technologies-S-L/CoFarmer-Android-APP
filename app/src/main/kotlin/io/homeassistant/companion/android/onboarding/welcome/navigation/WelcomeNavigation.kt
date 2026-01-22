package io.homeassistant.companion.android.onboarding.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.homeassistant.companion.android.onboarding.welcome.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object WelcomeRoute

internal fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    navigate(route = WelcomeRoute, navOptions)
}

/**
 * Navigation setup for the new QR-first Welcome screen.
 *
 * @param onScanQrClick Primary action - navigates to QR scanner
 * @param onAutoDiscoverClick Secondary action - navigates to server discovery
 * @param onManualSetupClick Tertiary action - navigates to manual URL entry
 * @param onLearnMoreClick Opens documentation
 */
internal fun NavGraphBuilder.welcomeScreen(
    onScanQrClick: () -> Unit,
    onAutoDiscoverClick: () -> Unit,
    onManualSetupClick: () -> Unit,
    onLearnMoreClick: () -> Unit,
) {
    composable<WelcomeRoute> {
        WelcomeScreen(
            onScanQrClick = onScanQrClick,
            onAutoDiscoverClick = onAutoDiscoverClick,
            onManualSetupClick = onManualSetupClick,
            onLearnMoreClick = onLearnMoreClick,
        )
    }
}

/**
 * Legacy navigation setup for backward compatibility.
 * All actions route to onConnectClick which goes to ServerDiscovery.
 */
internal fun NavGraphBuilder.welcomeScreen(onConnectClick: () -> Unit, onLearnMoreClick: () -> Unit) {
    composable<WelcomeRoute> {
        WelcomeScreen(onConnectClick = onConnectClick, onLearnMoreClick = onLearnMoreClick)
    }
}
