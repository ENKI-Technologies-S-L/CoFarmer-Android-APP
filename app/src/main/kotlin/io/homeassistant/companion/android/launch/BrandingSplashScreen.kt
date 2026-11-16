package io.homeassistant.companion.android.launch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview
import io.homeassistant.companion.android.common.compose.theme.SoraFontFamily

private val CoFarmerAccent = Color(0xFF0F97AF)
private val WordmarkLight = Color(0xFF333333)
private val BackgroundLight = Color(0xFFFAFAFA)
private val BackgroundDark = Color(0xFF1A1A1A)

/**
 * CoFarmer Assistant branding splash screen shown during app initialization.
 * Displays the CO link logo, the "CoFarmer Assistant" wordmark (Sora), the tagline and the "by ENKITEK" footer.
 */
@Composable
fun BrandingSplashScreen(modifier: Modifier = Modifier, isDarkTheme: Boolean = isSystemInDarkTheme()) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "splash_alpha",
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val wordmarkColor = if (isDarkTheme) Color.White else WordmarkLight
    val subtitleColor = if (isDarkTheme) Color(0xFFB0B0B0) else Color(0xFF666666)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(alphaAnim),
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo_cofarmer),
                contentDescription = stringResource(commonR.string.splash_logo_description),
                modifier = Modifier.size(120.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(commonR.string.splash_brand_name),
                    fontFamily = SoraFontFamily,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = wordmarkColor,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(commonR.string.splash_brand_suffix),
                    fontFamily = SoraFontFamily,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CoFarmerAccent,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(commonR.string.splash_tagline),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = subtitleColor,
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        Text(
            text = stringResource(commonR.string.splash_by_enkitek),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = subtitleColor,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(alphaAnim)
                .then(Modifier.height(80.dp)),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandingSplashScreenPreview() {
    HAThemeForPreview {
        BrandingSplashScreen(isDarkTheme = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandingSplashScreenDarkPreview() {
    HAThemeForPreview {
        BrandingSplashScreen(isDarkTheme = true)
    }
}
