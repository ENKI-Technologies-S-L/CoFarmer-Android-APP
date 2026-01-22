package io.homeassistant.companion.android.themes

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import io.homeassistant.companion.android.common.data.prefs.NightModeTheme
import io.homeassistant.companion.android.common.data.prefs.NightModeTheme.ANDROID
import io.homeassistant.companion.android.common.data.prefs.NightModeTheme.DARK
import io.homeassistant.companion.android.common.data.prefs.NightModeTheme.SYSTEM
import io.homeassistant.companion.android.common.data.prefs.PrefsRepository
import io.homeassistant.companion.android.util.CoFarmerFeatures
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Manages the night mode theme for the application based on the user selection from the settings.
 *
 * This class is responsible for retrieving, saving, and applying the night mode theme.
 * It interacts with [PrefsRepository] to persist the selected theme.
 *
 * When [CoFarmerFeatures.THEME_PICKER_ENABLED] is false, the theme always follows the system,
 * allowing the WebView to display Home Assistant themes (like Graphite Auto) correctly.
 *
 * @property prefsRepository The repository for accessing and storing application preferences.
 */
class NightModeManager @Inject constructor(private val prefsRepository: PrefsRepository) {

    suspend fun getCurrentNightMode(): NightModeTheme {
        // When theme picker is disabled, always follow system
        if (!CoFarmerFeatures.THEME_PICKER_ENABLED) {
            return SYSTEM
        }

        val nightMode = prefsRepository.getCurrentNightModeTheme()
        return if (nightMode == null) {
            val nightModeThemeToSet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                SYSTEM
            } else {
                SYSTEM // Also default to SYSTEM for consistency
            }
            prefsRepository.saveNightModeTheme(nightModeThemeToSet)
            nightModeThemeToSet
        } else {
            nightMode
        }
    }

    suspend fun saveNightMode(nightModeTheme: NightModeTheme?) {
        // When theme picker is disabled, don't allow changing the theme
        if (!CoFarmerFeatures.THEME_PICKER_ENABLED) {
            Timber.d("Theme picker disabled, forcing FOLLOW_SYSTEM")
            SYSTEM.setAsDefaultNightMode()
            return
        }

        if (nightModeTheme !== null) {
            val currentNightMode = getCurrentNightMode()
            if (currentNightMode != nightModeTheme) {
                prefsRepository.saveNightModeTheme(nightModeTheme)
                nightModeTheme.setAsDefaultNightMode()
            }
        } else {
            Timber.i("Skipping saving night mode theme since nightModeTheme is null")
        }
    }

    suspend fun applyCurrentNightMode() {
        val nightMode = getCurrentNightMode()
        nightMode.setAsDefaultNightMode()
    }
}

private suspend fun NightModeTheme.setAsDefaultNightMode() {
    withContext(Dispatchers.Main) {
        when (this@setAsDefaultNightMode) {
            DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            ANDROID, SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
