package io.homeassistant.companion.android.onboarding.serverdiscovery

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.composable.HAAccentButton
import io.homeassistant.companion.android.common.compose.composable.HALoading
import io.homeassistant.companion.android.common.compose.composable.HAModalBottomSheet
import io.homeassistant.companion.android.common.compose.composable.HAPlainButton
import io.homeassistant.companion.android.common.compose.composable.HATopBar
import io.homeassistant.companion.android.common.compose.composable.alpha
import io.homeassistant.companion.android.common.compose.theme.HABorderWidth
import io.homeassistant.companion.android.common.compose.theme.HABrandColors
import io.homeassistant.companion.android.common.compose.theme.HADimens
import io.homeassistant.companion.android.common.compose.theme.HARadius
import io.homeassistant.companion.android.common.compose.theme.HASize
import io.homeassistant.companion.android.common.compose.theme.HATextStyle
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview
import io.homeassistant.companion.android.common.compose.theme.LocalHAColorScheme
import io.homeassistant.companion.android.common.compose.theme.MaxButtonWidth
import io.homeassistant.companion.android.common.data.HomeAssistantVersion
import io.homeassistant.companion.android.util.compose.HAPreviews
import java.net.URL
import kotlinx.coroutines.launch

private val ICON_SIZE = 64.dp
private val MaxContentWidth = MaxButtonWidth

@Composable
internal fun ServerDiscoveryScreen(
    onBackClick: () -> Unit,
    onConnectClick: (server: URL) -> Unit,
    onHelpClick: () -> Unit,
    onQrScanClick: () -> Unit,
    onManualSetupClick: () -> Unit,
    viewModel: ServerDiscoveryViewModel,
    modifier: Modifier = Modifier,
) {
    val discoveryState by viewModel.discoveryFlow.collectAsStateWithLifecycle(Started)

    ServerDiscoveryScreen(
        discoveryState = discoveryState,
        onBackClick = onBackClick,
        onConnectClick = onConnectClick,
        onDismissOneServerFound = viewModel::onDismissOneServerFound,
        onHelpClick = onHelpClick,
        onQrScanClick = onQrScanClick,
        onManualSetupClick = onManualSetupClick,
        modifier = modifier,
    )
}

@Composable
internal fun ServerDiscoveryScreen(
    discoveryState: DiscoveryState,
    onBackClick: () -> Unit,
    onConnectClick: (server: URL) -> Unit,
    onDismissOneServerFound: () -> Unit,
    onHelpClick: () -> Unit,
    onQrScanClick: () -> Unit,
    onManualSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { HATopBar(onBackClick = onBackClick, onHelpClick = onHelpClick) },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { contentPadding ->
        ScreenContent(
            contentPadding = contentPadding,
            discoveryState = discoveryState,
            onConnectClick = onConnectClick,
            onQrScanClick = onQrScanClick,
            onManualSetupClick = onManualSetupClick,
        )

        OneServerFound(
            onConnectClick = onConnectClick,
            onDismiss = onDismissOneServerFound,
            discoveryState = discoveryState,
        )
    }
}

@VisibleForTesting
internal const val ONE_SERVER_FOUND_MODAL_TAG = "OneServerFoundModal"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OneServerFound(
    onConnectClick: (serverUrl: URL) -> Unit,
    onDismiss: () -> Unit,
    discoveryState: DiscoveryState,
) {
    val bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    // Use a cached state to be able to use the animation from the modal otherwise if we simply use if(visible)
    // the animation is not played correctly.
    var serverDiscoveredCached by remember { mutableStateOf(discoveryState as? ServerDiscovered) }

    // If we get the ServerDiscovered we display the modal and we keep it even if the state change so that
    // the user is in control of it.
    if (discoveryState is ServerDiscovered) {
        serverDiscoveredCached = discoveryState
    }

    val coroutineScope = rememberCoroutineScope()

    serverDiscoveredCached?.let { serverDiscovered ->
        HAModalBottomSheet(
            bottomSheetState = bottomSheetState,
            onDismissRequest = {
                serverDiscoveredCached = null
                onDismiss()
            },
            modifier = Modifier.testTag(ONE_SERVER_FOUND_MODAL_TAG),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = HADimens.SPACE6)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HADimens.SPACE6),
            ) {
                Text(
                    text = serverDiscovered.name,
                    style = HATextStyle.HeadlineMedium,
                )
                Icon(
                    imageVector = Icons.Default.Storage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(ICON_SIZE),
                    tint = LocalHAColorScheme.current.colorFillPrimaryLoudResting,
                )
                Text(
                    text = serverDiscovered.url.toString(),
                    style = HATextStyle.Body,
                    modifier = Modifier.padding(vertical = HADimens.SPACE3),
                )
                HAAccentButton(
                    text = stringResource(commonR.string.server_discovery_connect),
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                            onConnectClick(serverDiscovered.url)
                        }
                    },
                    modifier = Modifier.padding(bottom = HADimens.SPACE6).fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    contentPadding: PaddingValues,
    discoveryState: DiscoveryState,
    onConnectClick: (URL) -> Unit,
    onQrScanClick: () -> Unit,
    onManualSetupClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title
        Text(
            text = stringResource(commonR.string.searching_home_network),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = HADimens.SPACE4),
        )

        // Local access warning chip
        LocalAccessWarningChip(
            modifier = Modifier.padding(top = HADimens.SPACE3),
        )

        // Animated scanning icon - centered and flexible
        when (discoveryState) {
            is Started, NoServerFound, is ServerDiscovered -> CompactScanningContent(discoveryState)
            is ServersDiscovered -> CompactServersFound(discoveryState, onConnectClick)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom buttons
        HAAccentButton(
            text = stringResource(commonR.string.qr_scanner_button),
            onClick = onQrScanClick,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE2))

        HAPlainButton(
            text = stringResource(commonR.string.manual_setup),
            onClick = onManualSetupClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HADimens.SPACE4),
        )
    }
}

@Composable
private fun ColumnScope.CompactServersFound(state: ServersDiscovered, onConnectClick: (URL) -> Unit) {
    Spacer(modifier = Modifier.height(HADimens.SPACE4))
    
    // Show found servers in a compact card list
    state.servers.take(3).forEach { server ->
        ServerItemContent(server, onConnectClick)
    }
    
    if (state.servers.size > 3) {
        Text(
            text = "+${state.servers.size - 3} more",
            style = HATextStyle.BodyMedium,
            color = LocalHAColorScheme.current.colorTextSecondary,
            modifier = Modifier.padding(top = HADimens.SPACE2),
        )
    }
    
    // Show scanning indicator
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = HADimens.SPACE4),
    ) {
        HALoading()
        Spacer(modifier = Modifier.padding(start = HADimens.SPACE2))
        Text(
            text = "Still searching...",
            style = HATextStyle.BodyMedium,
            color = LocalHAColorScheme.current.colorTextSecondary,
        )
    }
}

@Composable
private fun ColumnScope.CompactScanningContent(discoveryState: DiscoveryState) {
    Spacer(modifier = Modifier.height(HADimens.SPACE6))
    
    // Compact animated icon
    CompactAnimatedIcon()
    
    Spacer(modifier = Modifier.height(HADimens.SPACE4))

    // Status text
    val statusText = when (discoveryState) {
        NoServerFound -> stringResource(commonR.string.server_discovery_no_server_info)
        else -> stringResource(commonR.string.loading_content_description)
    }
    
    val currentAlpha: Float by animateFloatAsState(
        targetValue = if (discoveryState == NoServerFound) 1f else 0.6f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    Text(
        text = statusText,
        style = HATextStyle.Body,
        textAlign = TextAlign.Center,
        color = LocalHAColorScheme.current.colorTextSecondary,
        modifier = Modifier
            .padding(horizontal = HADimens.SPACE4)
            .alpha(currentAlpha)
            .widthIn(max = MaxContentWidth),
    )
}

@Composable
private fun CompactAnimatedIcon() {
    Box(
        modifier = Modifier.size(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        val rotation by rememberInfiniteTransition(label = "dots_rotation").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "dots_rotation_value",
        )
        val pulse by rememberInfiniteTransition(label = "icon_pulse").animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "icon_pulse_value",
        )
        
        // Rotating dots
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.dots),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .rotate(rotation),
        )
        
        // Center icon with gradient
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(pulse)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(HABrandColors.Primary, HABrandColors.Secondary)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(commonR.drawable.ic_stat_ic_notification_blue),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = HABrandColors.Background,
            )
        }
    }
}

@Composable
private fun ColumnScope.ServersDiscoveredContent(state: ServersDiscovered, onConnectClick: (URL) -> Unit) {
    Spacer(modifier = Modifier.height(HADimens.SPACE8))
    // We are not using a LazyColumn here to avoid dealing with nested scroll, it is not an issue since
    // `servers` should remains quite small.
    state.servers.forEach { server ->
        ServerItemContent(server, onConnectClick)
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(HADimens.SPACE8),
    ) {
        HALoading()
    }
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
private fun ServerItemContent(server: ServerDiscovered, onConnectClick: (URL) -> Unit, modifier: Modifier = Modifier) {
    val rowShape = RoundedCornerShape(size = HARadius.XL)
    Row(
        modifier = modifier
            .widthIn(max = MaxContentWidth)
            .fillMaxWidth()
            .padding(vertical = HADimens.SPACE2)
            .border(
                border = BorderStroke(HABorderWidth.S, LocalHAColorScheme.current.colorBorderNeutralQuiet),
                shape = rowShape,
            )
            .clip(rowShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = LocalHAColorScheme.current.colorFillPrimaryLoudHover),
                onClick = {
                    onConnectClick(server.url)
                },
                role = Role.Button,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_assistant_branding),
            contentDescription = null,
            modifier = Modifier
                .size(ICON_SIZE)
                .padding(vertical = HADimens.SPACE2, horizontal = HADimens.SPACE4),
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = HADimens.SPACE2)
                .padding(end = HADimens.SPACE4),
        ) {
            Text(
                text = server.name,
                style = HATextStyle.Body.copy(textAlign = TextAlign.Start),
            )
            Text(
                text = server.url.toString(),
                style = HATextStyle.BodyMedium.copy(textAlign = TextAlign.Start),
            )
        }
    }
}

@Composable
private fun ColumnScope.ScanningForServer(discoveryState: DiscoveryState) {
    // We use spacer to position the image where we want when there is remaining space in the column using percentage
    val positionPercentage = 0.2f
    Spacer(modifier = Modifier.weight(positionPercentage))
    AnimatedIcon()
    Spacer(modifier = Modifier.weight(positionPercentage))

    val currentAlpha: Float by animateFloatAsState(
        targetValue = if (discoveryState == NoServerFound) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing,
        ),
    )

    Text(
        text = stringResource(commonR.string.server_discovery_no_server_info),
        style = HATextStyle.Body,
        modifier = Modifier
            .padding(
                vertical = HADimens.SPACE3,
                horizontal = HADimens.SPACE4,
            )
            .alpha(currentAlpha)
            .semantics {
                alpha = currentAlpha
            }
            .widthIn(max = MaxContentWidth),
    )

    Spacer(modifier = Modifier.weight(1f - 2f * positionPercentage))
}

@Composable
private fun AnimatedIcon() {
    Box(modifier = Modifier.padding(HADimens.SPACE3)) {
        val rotation by rememberInfiniteTransition(label = "dots_rotation").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "dots_rotation_value",
        )
        val pulse by rememberInfiniteTransition(label = "icon_pulse").animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "icon_pulse_value",
        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.dots),
            contentDescription = null,
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.Center)
                .rotate(rotation),
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(pulse)
                .align(Alignment.Center)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(HABrandColors.Primary, HABrandColors.Secondary)
                    ),
                    shape = CircleShape
                ),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(commonR.drawable.ic_stat_ic_notification_blue),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(HASize.X5L)
                    .scale(pulse),
                tint = HABrandColors.Background,
            )
        }
    }
}

/**
 * Warning card explaining local network discovery limitations.
 * Informs users that discovered servers only work on local network (inside the farm)
 * and suggests using QR code from Quick Start Guide for remote access.
 */
@Composable
private fun LocalNetworkWarningCard() {
    val warningShape = RoundedCornerShape(size = HARadius.L)
    Column(
        modifier = Modifier
            .widthIn(max = MaxContentWidth)
            .fillMaxWidth()
            .padding(vertical = HADimens.SPACE3, horizontal = HADimens.SPACE2)
            .background(
                color = LocalHAColorScheme.current.colorFillPrimaryQuietResting,
                shape = warningShape,
            )
            .border(
                border = BorderStroke(HABorderWidth.S, LocalHAColorScheme.current.colorBorderPrimaryNormal),
                shape = warningShape,
            )
            .padding(HADimens.SPACE4),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = HADimens.SPACE2),
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = LocalHAColorScheme.current.colorFillPrimaryLoudResting,
                modifier = Modifier
                    .size(HASize.XL)
                    .padding(end = HADimens.SPACE2),
            )
            Text(
                text = stringResource(commonR.string.local_network_warning_title),
                style = HATextStyle.BodyMedium,
                color = LocalHAColorScheme.current.colorFillPrimaryLoudResting,
            )
        }
        Text(
            text = stringResource(commonR.string.local_network_warning_message),
            style = HATextStyle.Body,
            color = LocalHAColorScheme.current.colorTextSecondary,
        )
    }
}

/**
 * Visual warning chip indicating that servers found are local-only.
 * Features a modern design with icon, colored background, and rounded corners.
 */
@Composable
private fun LocalAccessWarningChip(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = LocalHAColorScheme.current.colorFillDangerQuietResting,
                shape = RoundedCornerShape(HARadius.XL),
            )
            .border(
                width = 1.dp,
                color = LocalHAColorScheme.current.colorFillDangerLoudResting.copy(alpha = 0.3f),
                shape = RoundedCornerShape(HARadius.XL),
            )
            .padding(horizontal = HADimens.SPACE3, vertical = HADimens.SPACE2),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = LocalHAColorScheme.current.colorFillDangerLoudResting,
        )
        Spacer(modifier = Modifier.padding(start = HADimens.SPACE2))
        Text(
            text = stringResource(commonR.string.local_network_access_warning),
            style = HATextStyle.BodyMedium,
            color = LocalHAColorScheme.current.colorOnDangerNormal,
        )
    }
}

@HAPreviews
@Composable
private fun ServerDiscoveryScreenPreview_scanning() {
    HAThemeForPreview {
        ServerDiscoveryScreen(
            discoveryState = Started,
            onConnectClick = {},
            onQrScanClick = {},
            onManualSetupClick = {},
            onHelpClick = {},
            onBackClick = {},
            onDismissOneServerFound = {},
        )
    }
}

@HAPreviews
@Composable
private fun ServerDiscoveryScreenPreview_no_server_found() {
    HAThemeForPreview {
        ServerDiscoveryScreen(
            discoveryState = NoServerFound,
            onConnectClick = {},
            onQrScanClick = {},
            onManualSetupClick = {},
            onHelpClick = {},
            onBackClick = {},
            onDismissOneServerFound = {},
        )
    }
}

@HAPreviews
@Composable
private fun ServerDiscoveryScreenPreview_with_one_server() {
    HAThemeForPreview {
        ServerDiscoveryScreen(
            discoveryState = ServerDiscovered(
                "hello",
                URL("http://my.homeassistant.io"),
                HomeAssistantVersion(2042, 1, 42),
            ),
            onConnectClick = {},
            onQrScanClick = {},
            onManualSetupClick = {},
            onHelpClick = {},
            onBackClick = {},
            onDismissOneServerFound = {},
        )
    }
}

@HAPreviews
@Composable
private fun ServerDiscoveryScreenPreview_with_multiple_servers() {
    HAThemeForPreview {
        ServerDiscoveryScreen(
            discoveryState = ServersDiscovered(
                listOf(
                    ServerDiscovered(
                        "hello",
                        URL("http://my.homeassistant.io"),
                        HomeAssistantVersion(2042, 1, 42),
                    ),
                    ServerDiscovered(
                        "world",
                        URL("http://my.homeassistant.very.long.url.for.testing.with.many.sub.domains.org"),
                        HomeAssistantVersion(2042, 1, 42),
                    ),
                ),
            ),
            onConnectClick = {},
            onQrScanClick = {},
            onManualSetupClick = {},
            onHelpClick = {},
            onBackClick = {},
            onDismissOneServerFound = {},
        )
    }
}
