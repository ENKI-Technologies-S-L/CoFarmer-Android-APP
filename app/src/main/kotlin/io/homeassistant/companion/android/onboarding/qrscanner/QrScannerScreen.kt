package io.homeassistant.companion.android.onboarding.qrscanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.composable.HAAccentButton
import io.homeassistant.companion.android.common.compose.composable.HAPlainButton
import io.homeassistant.companion.android.common.compose.composable.HATopBar
import io.homeassistant.companion.android.common.compose.theme.HABrandColors
import io.homeassistant.companion.android.common.compose.theme.HADimens
import io.homeassistant.companion.android.common.compose.theme.HARadius
import io.homeassistant.companion.android.common.compose.theme.HATextStyle
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview
import io.homeassistant.companion.android.common.compose.theme.LocalHAColorScheme
import io.homeassistant.companion.android.util.compose.HAPreviews
import java.net.URL

private val SCANNER_SIZE = 280.dp
private val SCANNER_BORDER_WIDTH = 3.dp

/**
 * Screen for scanning QR codes to connect to a CoFarmer hub.
 * The QR code should contain a valid URL (http:// or https://).
 *
 * @param onBackClick Callback when user presses back
 * @param onUrlScanned Callback when a valid URL is successfully scanned
 * @param onManualSetupClick Callback when user chooses to enter URL manually
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun QrScannerScreen(
    onBackClick: () -> Unit,
    onUrlScanned: (URL) -> Unit,
    onManualSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Scaffold(
        modifier = modifier,
        topBar = { HATopBar(onBackClick = onBackClick) },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { contentPadding ->
        when {
            cameraPermissionState.status.isGranted -> {
                QrScannerContent(
                    onUrlScanned = onUrlScanned,
                    onManualSetupClick = onManualSetupClick,
                    modifier = Modifier.padding(contentPadding),
                )
            }
            cameraPermissionState.status.shouldShowRationale -> {
                CameraPermissionRationale(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    onManualSetupClick = onManualSetupClick,
                    modifier = Modifier.padding(contentPadding),
                )
            }
            else -> {
                CameraPermissionRequest(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    onManualSetupClick = onManualSetupClick,
                    modifier = Modifier.padding(contentPadding),
                )
            }
        }
    }

    // Request permission on first launch
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
private fun QrScannerContent(
    onUrlScanned: (URL) -> Unit,
    onManualSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(commonR.string.qr_scanner_title),
            style = HATextStyle.Headline,
            modifier = Modifier.padding(top = HADimens.SPACE6),
        )

        Text(
            text = stringResource(commonR.string.qr_scanner_subtitle),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = HADimens.SPACE2,
                start = HADimens.SPACE4,
                end = HADimens.SPACE4,
            ),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        // QR Scanner viewfinder with animated border
        QrScannerViewfinder(
            onResult = { result ->
                val scannedText = result.trim()
                try {
                    // Validate it's a proper URL
                    if (scannedText.startsWith("http://") || scannedText.startsWith("https://")) {
                        val url = URL(scannedText)
                        onUrlScanned(url)
                    } else if (scannedText.startsWith("cofarmer://")) {
                        // Support custom scheme: cofarmer://connect?url=http://...
                        val uri = Uri.parse(scannedText)
                        val urlParam = uri.getQueryParameter("url")
                        if (urlParam != null) {
                            onUrlScanned(URL(urlParam))
                        } else {
                            errorMessage = context.getString(commonR.string.qr_scanner_error_invalid_url)
                        }
                    } else {
                        errorMessage = context.getString(commonR.string.qr_scanner_error_invalid_url)
                    }
                } catch (e: Exception) {
                    errorMessage = context.getString(commonR.string.qr_scanner_error_invalid_url)
                }
            },
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE4))

        // Error message
        errorMessage?.let { error ->
            Text(
                text = error,
                style = HATextStyle.Body,
                color = LocalHAColorScheme.current.colorOnDangerQuiet,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = HADimens.SPACE4),
            )
            Spacer(modifier = Modifier.height(HADimens.SPACE2))
        }

        Text(
            text = stringResource(commonR.string.qr_scanner_help),
            style = HATextStyle.BodyMedium,
            textAlign = TextAlign.Center,
            color = LocalHAColorScheme.current.colorTextSecondary,
            modifier = Modifier.padding(horizontal = HADimens.SPACE6),
        )

        Spacer(modifier = Modifier.weight(1f))

        HAPlainButton(
            text = stringResource(commonR.string.manual_setup),
            onClick = onManualSetupClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HADimens.SPACE6),
        )
    }
}

@Composable
private fun QrScannerViewfinder(
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var barcodeView by remember { mutableStateOf<DecoratedBarcodeView?>(null) }

    // Handle lifecycle for camera
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> barcodeView?.resume()
                Lifecycle.Event.ON_PAUSE -> barcodeView?.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            barcodeView?.pause()
        }
    }

    // Modern pulse animation (expanding rings from center)
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_animation")
    
    // Primary pulse ring
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_scale_1",
    )
    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_alpha_1",
    )
    
    // Secondary pulse ring (delayed)
    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, delayMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_scale_2",
    )
    val pulseAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, delayMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_alpha_2",
    )

    // Pulsing corners
    val cornerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "corner_alpha",
    )

    Box(
        modifier = modifier.size(SCANNER_SIZE),
        contentAlignment = Alignment.Center,
    ) {
        // Camera preview (without the default red line)
        AndroidView(
            factory = { ctx ->
                DecoratedBarcodeView(ctx).apply {
                    // Hide the default viewfinder (red line)
                    viewFinder.visibility = android.view.View.INVISIBLE

                    decodeContinuous(object : BarcodeCallback {
                        override fun barcodeResult(result: BarcodeResult?) {
                            result?.text?.let { text ->
                                onResult(text)
                            }
                        }
                    })

                    barcodeView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(HARadius.XL)),
        )

        // Custom scanning overlay with animated corners and pulse effect
        ScannerOverlay(
            pulseScale1 = pulseScale1,
            pulseAlpha1 = pulseAlpha1,
            pulseScale2 = pulseScale2,
            pulseAlpha2 = pulseAlpha2,
            cornerAlpha = cornerAlpha,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

/**
 * Custom scanner overlay with animated corners and modern pulse effect.
 */
@Composable
private fun ScannerOverlay(
    pulseScale1: Float,
    pulseAlpha1: Float,
    pulseScale2: Float,
    pulseAlpha2: Float,
    cornerAlpha: Float,
    modifier: Modifier = Modifier,
) {
    val primaryColor = HABrandColors.Primary
    val secondaryColor = HABrandColors.Secondary
    val cornerLength = 40.dp
    val cornerStrokeWidth = 4.dp

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val cornerLengthPx = cornerLength.toPx()
        val strokeWidthPx = cornerStrokeWidth.toPx()
        val cornerRadius = HARadius.XL.toPx()

        // Corner colors with alpha
        val topLeftColor = primaryColor.copy(alpha = cornerAlpha)
        val topRightColor = secondaryColor.copy(alpha = cornerAlpha)
        val bottomLeftColor = secondaryColor.copy(alpha = cornerAlpha)
        val bottomRightColor = primaryColor.copy(alpha = cornerAlpha)

        // Top-left corner
        drawLine(
            color = topLeftColor,
            start = Offset(0f, cornerLengthPx),
            end = Offset(0f, cornerRadius),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = topLeftColor,
            start = Offset(cornerRadius, 0f),
            end = Offset(cornerLengthPx, 0f),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )

        // Top-right corner
        drawLine(
            color = topRightColor,
            start = Offset(width - cornerLengthPx, 0f),
            end = Offset(width - cornerRadius, 0f),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = topRightColor,
            start = Offset(width, cornerRadius),
            end = Offset(width, cornerLengthPx),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )

        // Bottom-left corner
        drawLine(
            color = bottomLeftColor,
            start = Offset(0f, height - cornerLengthPx),
            end = Offset(0f, height - cornerRadius),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = bottomLeftColor,
            start = Offset(cornerRadius, height),
            end = Offset(cornerLengthPx, height),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )

        // Bottom-right corner
        drawLine(
            color = bottomRightColor,
            start = Offset(width - cornerLengthPx, height),
            end = Offset(width - cornerRadius, height),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = bottomRightColor,
            start = Offset(width, height - cornerLengthPx),
            end = Offset(width, height - cornerRadius),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round,
        )

        // Modern pulse effect - expanding rings from center
        val centerX = width / 2
        val centerY = height / 2
        val maxRadius = minOf(width, height) / 2 * 0.7f

        // First pulse ring
        drawCircle(
            color = primaryColor.copy(alpha = pulseAlpha1),
            radius = maxRadius * pulseScale1,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2.dp.toPx()),
        )

        // Second pulse ring (delayed)
        drawCircle(
            color = secondaryColor.copy(alpha = pulseAlpha2),
            radius = maxRadius * pulseScale2,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2.dp.toPx()),
        )

        // Center crosshair for alignment (subtle)
        val crosshairSize = 20.dp.toPx()
        val crosshairAlpha = 0.4f
        drawLine(
            color = primaryColor.copy(alpha = crosshairAlpha),
            start = Offset(centerX - crosshairSize, centerY),
            end = Offset(centerX + crosshairSize, centerY),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = primaryColor.copy(alpha = crosshairAlpha),
            start = Offset(centerX, centerY - crosshairSize),
            end = Offset(centerX, centerY + crosshairSize),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun CameraPermissionRequest(
    onRequestPermission: () -> Unit,
    onManualSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.QrCodeScanner,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = HABrandColors.Primary,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        Text(
            text = stringResource(commonR.string.qr_scanner_permission_title),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE3))

        Text(
            text = stringResource(commonR.string.qr_scanner_permission_content),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = HADimens.SPACE4),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        HAAccentButton(
            text = stringResource(commonR.string.qr_scanner_permission_button),
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE3))

        HAPlainButton(
            text = stringResource(commonR.string.manual_setup),
            onClick = onManualSetupClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CameraPermissionRationale(
    onRequestPermission: () -> Unit,
    onManualSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = LocalHAColorScheme.current.colorTextSecondary,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        Text(
            text = stringResource(commonR.string.qr_scanner_permission_denied_title),
            style = HATextStyle.Headline,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE3))

        Text(
            text = stringResource(commonR.string.qr_scanner_permission_denied_content),
            style = HATextStyle.Body,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = HADimens.SPACE4),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE6))

        HAAccentButton(
            text = stringResource(commonR.string.qr_scanner_open_settings),
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(HADimens.SPACE3))

        HAPlainButton(
            text = stringResource(commonR.string.manual_setup),
            onClick = onManualSetupClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@HAPreviews
@Composable
private fun QrScannerScreenPreview() {
    HAThemeForPreview {
        CameraPermissionRequest(
            onRequestPermission = {},
            onManualSetupClick = {},
        )
    }
}

@HAPreviews
@Composable
private fun QrScannerPermissionDeniedPreview() {
    HAThemeForPreview {
        CameraPermissionRationale(
            onRequestPermission = {},
            onManualSetupClick = {},
        )
    }
}
