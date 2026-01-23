package io.homeassistant.companion.android.onboarding.hubsetup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.composable.ButtonVariant
import io.homeassistant.companion.android.common.compose.composable.HAAccentButton
import io.homeassistant.companion.android.common.compose.composable.HAFilledButton
import io.homeassistant.companion.android.common.compose.theme.HADimens
import io.homeassistant.companion.android.common.compose.theme.HATextStyle
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview
import io.homeassistant.companion.android.common.compose.theme.LocalHAColorScheme
import io.homeassistant.companion.android.common.compose.theme.MaxButtonWidth
import io.homeassistant.companion.android.util.compose.HAPreviews

private val LOGO_SIZE = 64.dp
private val ICON_SIZE = 32.dp
private val STEP_ICON_BACKGROUND_SIZE = 56.dp
private val MaxContentWidth = MaxButtonWidth

/**
 * Initial hub setup screen asking if the user has a CoFarmer Hub.
 */
@Composable
internal fun HubSetupScreen(
    onYesHaveHub: () -> Unit,
    onNoHub: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = LocalHAColorScheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = HADimens.SPACE4)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_assistant_branding),
            contentDescription = null,
            modifier = Modifier.size(LOGO_SIZE),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        // Title
        Text(
            text = stringResource(commonR.string.hub_setup_title),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE4))

        // Question
        Text(
            text = stringResource(commonR.string.hub_setup_question),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            color = colorScheme.colorTextSecondary,
            modifier = Modifier.widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE8))

        // Yes button - primary action
        HAAccentButton(
            text = stringResource(commonR.string.hub_setup_yes_have_hub),
            onClick = onYesHaveHub,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE3))

        // No button - secondary action
        HAFilledButton(
            text = stringResource(commonR.string.hub_setup_no_hub),
            onClick = onNoHub,
            variant = ButtonVariant.NEUTRAL,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Hub setup instructions screen with modern step-by-step walkthrough.
 */
@Composable
internal fun HubSetupInstructionsScreen(
    onBackClick: () -> Unit,
    onReadyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = LocalHAColorScheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HADimens.SPACE2),
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(commonR.string.back),
                )
            }
        }

        Spacer(modifier = Modifier.height(HADimens.SPACE2))

        // Title
        Text(
            text = stringResource(commonR.string.hub_setup_instructions_title),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = HADimens.SPACE4),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        // Modern step-by-step cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HADimens.SPACE4),
            verticalArrangement = Arrangement.spacedBy(HADimens.SPACE3),
        ) {
            SetupStepCard(
                stepNumber = 1,
                icon = Icons.Outlined.Power,
                text = stringResource(commonR.string.hub_setup_step_1),
            )
            SetupStepCard(
                stepNumber = 2,
                icon = Icons.Outlined.Cable,
                text = stringResource(commonR.string.hub_setup_step_2),
            )
            SetupStepCard(
                stepNumber = 3,
                icon = Icons.Outlined.Lightbulb,
                text = stringResource(commonR.string.hub_setup_step_3),
            )
            SetupStepCard(
                stepNumber = 4,
                icon = Icons.Outlined.Timer,
                text = stringResource(commonR.string.hub_setup_step_4),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Ready button
        HAAccentButton(
            text = stringResource(commonR.string.hub_setup_ready),
            onClick = onReadyClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HADimens.SPACE4)
                .widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))
    }
}

/**
 * Screen shown when user doesn't have a CoFarmer Hub.
 */
@Composable
internal fun NoHubScreen(
    onBackClick: () -> Unit,
    onVisitWebsiteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = LocalHAColorScheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = HADimens.SPACE4)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(commonR.string.back),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_assistant_branding),
            contentDescription = null,
            modifier = Modifier.size(LOGO_SIZE),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        // Title
        Text(
            text = stringResource(commonR.string.hub_setup_no_hub_title),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE4))

        // Description
        Text(
            text = stringResource(commonR.string.hub_setup_no_hub_description),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            color = colorScheme.colorTextSecondary,
            modifier = Modifier.widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.weight(1f))

        // Visit website button
        HAAccentButton(
            text = stringResource(commonR.string.hub_setup_visit_website),
            onClick = onVisitWebsiteClick,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))
    }
}

/**
 * Modern step card with icon, number badge, and description.
 */
@Composable
private fun SetupStepCard(
    stepNumber: Int,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    val colorScheme = LocalHAColorScheme.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.colorSurfaceDefault,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HADimens.SPACE4),
            horizontalArrangement = Arrangement.spacedBy(HADimens.SPACE4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon with colored background
            Box(
                modifier = Modifier
                    .size(STEP_ICON_BACKGROUND_SIZE)
                    .clip(CircleShape)
                    .background(colorScheme.colorOnPrimaryNormal.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(ICON_SIZE),
                    tint = colorScheme.colorOnPrimaryNormal,
                )
            }

            // Text content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Step number badge
                Text(
                    text = "Step $stepNumber",
                    style = HATextStyle.BodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start,
                    ),
                    color = colorScheme.colorOnPrimaryNormal,
                )
                // Description
                Text(
                    text = text,
                    style = HATextStyle.Body.copy(textAlign = TextAlign.Start),
                    color = colorScheme.colorTextSecondary,
                )
            }
        }
    }
}

@HAPreviews
@Composable
private fun HubSetupScreenPreview() {
    HAThemeForPreview {
        HubSetupScreen(
            onYesHaveHub = {},
            onNoHub = {},
        )
    }
}

@HAPreviews
@Composable
private fun HubSetupInstructionsScreenPreview() {
    HAThemeForPreview {
        HubSetupInstructionsScreen(
            onBackClick = {},
            onReadyClick = {},
        )
    }
}

@HAPreviews
@Composable
private fun NoHubScreenPreview() {
    HAThemeForPreview {
        NoHubScreen(
            onBackClick = {},
            onVisitWebsiteClick = {},
        )
    }
}
