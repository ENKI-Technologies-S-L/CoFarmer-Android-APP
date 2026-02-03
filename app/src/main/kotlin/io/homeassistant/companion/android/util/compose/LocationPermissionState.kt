package io.homeassistant.companion.android.util.compose

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.composable.HAAccentButton
import io.homeassistant.companion.android.common.compose.composable.HAPlainButton
import io.homeassistant.companion.android.common.compose.theme.HATextStyle

private val foregroundLocationPermissions: List<String> = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    // TODO drop this requirement https://github.com/home-assistant/android/issues/5931
    if (Build.VERSION.SDK_INT >=
        Build.VERSION_CODES.S
    ) {
        Manifest.permission.BLUETOOTH_CONNECT
    } else {
        Manifest.permission.BLUETOOTH
    },
)
val locationPermissions: List<String> = foregroundLocationPermissions.run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this + Manifest.permission.ACCESS_BACKGROUND_LOCATION
    } else {
        this
    }
}

/**
 * Wrapper for MultiplePermissionsState that shows a Google Play compliant disclosure dialog
 * before requesting location permissions.
 *
 * This ensures that the user sees a prominent in-app disclosure about location data collection
 * BEFORE any location permission dialog is shown, as required by Google Play policies.
 */
@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionStateWithDisclosure(
    private val permissionsState: MultiplePermissionsState,
    private val onShowDisclosure: () -> Unit,
    private val onDismissDisclosure: () -> Unit,
    private val skipDisclosure: Boolean = false,
) : MultiplePermissionsState by permissionsState {

    /**
     * Shows the disclosure dialog first, then requests permissions if user accepts.
     * This ensures Google Play compliance by showing disclosure BEFORE permission request.
     *
     * If [skipDisclosure] is true, the disclosure dialog is skipped (useful when the calling
     * screen already has its own prominent disclosure, like LocationSharingScreen).
     */
    override fun launchMultiplePermissionRequest() {
        if (skipDisclosure) {
            permissionsState.launchMultiplePermissionRequest()
        } else {
            onShowDisclosure()
        }
    }

    /**
     * Called after user accepts disclosure to proceed with actual permission request.
     */
    fun proceedWithPermissionRequest() {
        onDismissDisclosure()
        permissionsState.launchMultiplePermissionRequest()
    }

    /**
     * Called when user denies from disclosure dialog.
     */
    fun denyFromDisclosure() {
        onDismissDisclosure()
    }
}

/**
 * Remembers the state of location permissions and handles requesting them.
 *
 * This function manages both foreground (FINE/COARSE) and background location permissions.
 * It ensures that background permission is requested only after foreground permissions are granted,
 * as required by the Android system.
 *
 * IMPORTANT: This function now shows a Google Play compliant disclosure dialog BEFORE
 * requesting any location permissions, ensuring compliance with Google Play policies.
 *
 * @param onPermissionResult A callback function that is invoked with `true` if all requested
 *                           permissions are granted, and `false` otherwise.
 * @param skipDisclosure If true, skips showing the disclosure dialog. Use this ONLY when
 *                       the calling screen already has its own prominent location disclosure
 *                       (e.g., LocationSharingScreen). Default is false.
 * @return A [LocationPermissionStateWithDisclosure] object that can be used to observe and manage the
 *         state of the requested location permissions, with automatic disclosure handling.
 *
 * @see Manifest.permission.ACCESS_FINE_LOCATION
 * @see Manifest.permission.ACCESS_COARSE_LOCATION
 * @see Manifest.permission.ACCESS_BACKGROUND_LOCATION
 * @see MultiplePermissionsState
 * @see rememberPermissionState
 * @see rememberMultiplePermissionsState
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberLocationPermission(
    onPermissionResult: (Boolean) -> Unit,
    skipDisclosure: Boolean = false,
): LocationPermissionStateWithDisclosure {
    var showDisclosureDialog by remember { mutableStateOf(false) }

    val backgroundPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        ) {
            onPermissionResult(it)
        }
    } else {
        null
    }

    val permissionsState = rememberMultiplePermissionsState(
        foregroundLocationPermissions,
        onPermissionsResult = { permissionStatus ->
            if (permissionStatus.all { it.value }) {
                backgroundPermissionState?.launchPermissionRequest() ?: onPermissionResult(true)
            } else {
                onPermissionResult(false)
            }
        },
    )

    val locationPermissionStateWithDisclosure = remember(permissionsState, skipDisclosure) {
        LocationPermissionStateWithDisclosure(
            permissionsState = permissionsState,
            onShowDisclosure = { showDisclosureDialog = true },
            onDismissDisclosure = { showDisclosureDialog = false },
            skipDisclosure = skipDisclosure,
        )
    }

    // Show disclosure dialog when triggered (only if not skipped)
    if (showDisclosureDialog && !skipDisclosure) {
        LocationDisclosureDialog(
            onAccept = {
                locationPermissionStateWithDisclosure.proceedWithPermissionRequest()
            },
            onDeny = {
                locationPermissionStateWithDisclosure.denyFromDisclosure()
                onPermissionResult(false)
            },
        )
    }

    return locationPermissionStateWithDisclosure
}

/**
 * Google Play compliant location disclosure dialog.
 *
 * This dialog follows Google's requirements for prominent in-app disclosure:
 * - Shows BEFORE any location permission request
 * - Clearly describes data collected (precise GPS, background tracking)
 * - Explains how data is used (transmitted to user's server)
 * - Explains data sharing (NOT collected by us, NOT shared with third parties, NOT used for ads)
 * - Requires affirmative user action (tap "I Understand, Continue")
 * - Cannot be dismissed by tapping outside (user must make explicit choice)
 */
@Composable
private fun LocationDisclosureDialog(
    onAccept: () -> Unit,
    onDeny: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Cannot dismiss without explicit choice - Google Play requirement */ },
        title = {
            Text(
                text = stringResource(commonR.string.location_disclosure_dialog_title),
                style = HATextStyle.Headline,
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = stringResource(commonR.string.location_disclosure_dialog_content),
                    style = HATextStyle.Body,
                )
            }
        },
        confirmButton = {
            HAAccentButton(
                text = stringResource(commonR.string.location_disclosure_dialog_accept),
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        dismissButton = {
            HAPlainButton(
                text = stringResource(commonR.string.location_disclosure_dialog_deny),
                onClick = onDeny,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}
