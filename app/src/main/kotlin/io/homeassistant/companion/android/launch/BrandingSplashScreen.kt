package io.homeassistant.companion.android.launch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.common.compose.theme.HAThemeForPreview

private val CoFarmerGreen = Color(0xFF04D288)
private val CoFarmerBlue = Color(0xFF0066CC)
private val BackgroundLight = Color(0xFFFAFAFA)
private val BackgroundDark = Color(0xFF1A1A1A)

/**
 * CoFarmer branding splash screen shown during app initialization.
 * Displays the Enkitek logo, "COFARMER" text, tagline, and "by ENKITEK" footer.
 */
@Composable
fun BrandingSplashScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
) {
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
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1A1A1A)
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
            // Enkitek Logo
            Image(
                painter = painterResource(id = R.drawable.splash_logo_cofarmer),
                contentDescription = "CoFarmer Logo",
                modifier = Modifier.size(140.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // COFARMER text with gradient effect simulation
            Text(
                text = "COFARMER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CoFarmerGreen,
                letterSpacing = 4.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Control your farm from anywhere",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = subtitleColor,
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        // "by ENKITEK" at bottom
        Text(
            text = "by ENKITEK",
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
