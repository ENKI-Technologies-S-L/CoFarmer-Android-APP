package io.homeassistant.companion.android.common.compose.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.homeassistant.companion.android.common.R

/**
 * Sora, the CoFarmer brand typeface (SIL Open Font License, see branding-assets/cofarmer/fonts/OFL.txt).
 *
 * Used only for headings and brand moments; body text keeps the platform default (Roboto).
 */
val SoraFontFamily: FontFamily = FontFamily(
    Font(R.font.sora_semibold, FontWeight.SemiBold),
    Font(R.font.sora_bold, FontWeight.Bold),
)
