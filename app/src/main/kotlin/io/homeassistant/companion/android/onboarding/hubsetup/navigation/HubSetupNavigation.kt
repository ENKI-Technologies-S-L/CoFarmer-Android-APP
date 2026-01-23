package io.homeassistant.companion.android.onboarding.hubsetup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.homeassistant.companion.android.onboarding.hubsetup.HubSetupInstructionsScreen
import io.homeassistant.companion.android.onboarding.hubsetup.HubSetupScreen
import io.homeassistant.companion.android.onboarding.hubsetup.NoHubScreen
import kotlinx.serialization.Serializable

@Serializable
object HubSetupRoute

@Serializable
object HubSetupInstructionsRoute

@Serializable
object NoHubRoute

fun NavController.navigateToHubSetup(navOptions: NavOptions? = null) {
    navigate(HubSetupRoute, navOptions)
}

fun NavController.navigateToHubSetupInstructions(navOptions: NavOptions? = null) {
    navigate(HubSetupInstructionsRoute, navOptions)
}

fun NavController.navigateToNoHub(navOptions: NavOptions? = null) {
    navigate(NoHubRoute, navOptions)
}

fun NavGraphBuilder.hubSetupScreen(
    onYesHaveHub: () -> Unit,
    onNoHub: () -> Unit,
) {
    composable<HubSetupRoute> {
        HubSetupScreen(
            onYesHaveHub = onYesHaveHub,
            onNoHub = onNoHub,
        )
    }
}

fun NavGraphBuilder.hubSetupInstructionsScreen(
    onBackClick: () -> Unit,
    onReadyClick: () -> Unit,
) {
    composable<HubSetupInstructionsRoute> {
        HubSetupInstructionsScreen(
            onBackClick = onBackClick,
            onReadyClick = onReadyClick,
        )
    }
}

fun NavGraphBuilder.noHubScreen(
    onBackClick: () -> Unit,
    onVisitWebsiteClick: () -> Unit,
) {
    composable<NoHubRoute> {
        NoHubScreen(
            onBackClick = onBackClick,
            onVisitWebsiteClick = onVisitWebsiteClick,
        )
    }
}
