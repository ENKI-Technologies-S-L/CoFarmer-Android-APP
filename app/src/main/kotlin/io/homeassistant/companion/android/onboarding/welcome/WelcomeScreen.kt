package io.homeassistant.companion.android.onboarding.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.theme.HABrandColors
import io.homeassistant.companion.android.common.compose.theme.HADimens
import io.homeassistant.companion.android.common.compose.theme.HATextStyle
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview
import io.homeassistant.companion.android.common.compose.theme.LocalHAColorScheme
import io.homeassistant.companion.android.common.compose.theme.MaxButtonWidth
import io.homeassistant.companion.android.util.compose.HAPreviews

private val LOGO_SIZE = 48.dp
private val QR_BUTTON_SIZE = 96.dp
private val QR_ICON_SIZE = 48.dp
private val MaxContentWidth = MaxButtonWidth

/**
 * Minimalist welcome screen with QR as the only prominent action.
 *
 * Design philosophy: 99% of users will scan a QR code.
 * Everything else is secondary and should not distract.
 */
@Composable
internal fun WelcomeScreen(
    onScanQrClick: () -> Unit,
    onAutoDiscoverClick: () -> Unit,
    onManualSetupClick: () -> Unit,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = LocalHAColorScheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Minimal branding
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_assistant_branding),
            contentDescription = null,
            modifier = Modifier.size(LOGO_SIZE),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        // Simple title
        Text(
            text = stringResource(commonR.string.welcome_scan_qr),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE2))

        // Subtle description
        Text(
            text = stringResource(commonR.string.welcome_scan_qr_description),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            color = colorScheme.colorTextSecondary,
            modifier = Modifier.widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE8))

        // QR Button - the star of the show
        QrScanButton(onClick = onScanQrClick)

        Spacer(modifier = Modifier.weight(1f))

        // Discrete secondary options
        Text(
            text = stringResource(commonR.string.welcome_other_options),
            style = HATextStyle.Body,
            color = colorScheme.colorTextSecondary,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                    role = Role.Button,
                    onClick = onAutoDiscoverClick,
                )
                .padding(HADimens.SPACE3),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))
    }
}

@Composable
private fun QrScanButton(onClick: () -> Unit) {
    val colorScheme = LocalHAColorScheme.current

    // Subtle breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "qr_breathe")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "qr_scale",
    )

    Box(
        modifier = Modifier
            .size(QR_BUTTON_SIZE)
            .scale(scale)
            .clip(CircleShape)
            .background(HABrandColors.Primary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.QrCodeScanner,
            contentDescription = stringResource(commonR.string.welcome_scan_qr),
            modifier = Modifier.size(QR_ICON_SIZE),
            tint = colorScheme.colorOnPrimaryLoud,
        )
    }
}

/**
 * Legacy function for backward compatibility.
 */
@Composable
internal fun WelcomeScreen(
    onConnectClick: () -> Unit,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WelcomeScreen(
        onScanQrClick = onConnectClick,
        onAutoDiscoverClick = onConnectClick,
        onManualSetupClick = onConnectClick,
        onLearnMoreClick = onLearnMoreClick,
        modifier = modifier,
    )
}

@HAPreviews
@Composable
private fun WelcomeScreenPreview() {
    HAThemeForPreview {
        WelcomeScreen(
            onScanQrClick = {},
            onAutoDiscoverClick = {},
            onManualSetupClick = {},
            onLearnMoreClick = {},
        )
    }
}
