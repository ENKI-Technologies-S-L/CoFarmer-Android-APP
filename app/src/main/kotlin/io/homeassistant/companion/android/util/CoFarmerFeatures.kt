package io.homeassistant.companion.android.util

/**
 * Feature flags for CoFarmer industrial application.
 *
 * These flags control which features are visible and active in the app.
 * Features are hidden (not deleted) to maintain compatibility with upstream
 * Home Assistant Companion updates.
 *
 * Philosophy:
 * - Phone is a REMOTE CONTROL, not a sensor platform
 * - Farm sensors are industrial devices connected to HA, not smartphone accelerometers
 * - Minimize background services to avoid OEM battery killers (Xiaomi, Samsung, etc.)
 * - Reduce configuration surface = fewer bugs, fewer support tickets
 */
object CoFarmerFeatures {

    // ==================== DISABLED FEATURES ====================
    // These features are hidden in UI and their background services don't start

    /**
     * Phone-based sensors (GPS, accelerometer, battery, steps, etc.)
     * DISABLED: Farm sensors are industrial devices, not phones.
     * Risk if enabled: OEM crashes, battery drain, privacy concerns, permission fatigue.
     */
    const val SENSORS_ENABLED = false

    /**
     * Sensor update frequency settings.
     * DISABLED: Depends on SENSORS_ENABLED.
     */
    const val SENSOR_FREQUENCY_ENABLED = false

    /**
     * Home screen widgets.
     * DISABLED: OEM launcher compatibility issues, battery drain from updates.
     * Farm managers use dedicated tablets with app open, not phone home screens.
     */
    const val WIDGETS_ENABLED = false

    /**
     * Quick Settings tiles.
     * DISABLED: OEM customizes quick settings, requires background service.
     * Dashboard provides same functionality without OEM risk.
     */
    const val TILES_ENABLED = false

    /**
     * App shortcuts on home screen.
     * DISABLED: Minimal value over opening app directly. Reduces complexity.
     */
    const val SHORTCUTS_ENABLED = false

    /**
     * NFC tag scanning.
     * DISABLED: Nice-to-have but not critical. Industrial equipment tracking
     * uses dedicated RFID/barcode systems. Adds foreground service complexity.
     */
    const val NFC_ENABLED = false

    /**
     * Voice assistant integration.
     * DISABLED: OEM background restrictions kill always-on services.
     * Field connectivity is unreliable. Farm managers use physical controls.
     */
    const val VOICE_ASSIST_ENABLED = false

    /**
     * Android Auto integration.
     * DISABLED: Niche use case, complex Google certification requirements.
     * Farm managers use rugged tablets, not car infotainment.
     */
    const val ANDROID_AUTO_ENABLED = false

    /**
     * Device Controls (Android 13+ power menu).
     * DISABLED: Requires background service, OEM compatibility varies.
     */
    const val DEVICE_CONTROLS_ENABLED = false

    /**
     * Gesture navigation (edge swipes).
     * DISABLED: Conflicts with Android 10+ system gestures. Confuses users.
     */
    const val GESTURES_ENABLED = false

    /**
     * Page zoom setting.
     * DISABLED: Use system accessibility settings instead.
     */
    const val PAGE_ZOOM_ENABLED = false

    /**
     * Autoplay video setting.
     * DISABLED: HA dashboard handles video autoplay. App setting is redundant.
     */
    const val AUTOPLAY_VIDEO_ENABLED = false

    /**
     * Theme picker.
     * DISABLED: App follows system theme (FOLLOW_SYSTEM) which allows
     * the WebView to display HA themes like Graphite Auto correctly.
     * When disabled, NightModeManager forces FOLLOW_SYSTEM mode.
     */
    const val THEME_PICKER_ENABLED = false

    /**
     * Changelog features (popup, view changelog).
     * DISABLED: Zero operational value for farm managers.
     * If critical changes, notify via push notification.
     */
    const val CHANGELOG_ENABLED = false

    /**
     * Notification rate limiting setting.
     * DISABLED: Dangerous if misconfigured - can cause missed critical alerts.
     * Server-side should handle deduplication.
     */
    const val NOTIFICATION_RATE_LIMIT_ENABLED = false

    /**
     * Background access / battery optimization setting.
     * DISABLED: Only needed for phone sensors (which are disabled).
     * Push notifications use FCM which is OEM-optimized.
     */
    const val BACKGROUND_ACCESS_ENABLED = false

    // ==================== ENABLED FEATURES ====================
    // Core functionality for professional farm operations

    /**
     * Multi-site server management.
     * ENABLED: Core functionality. Farm managers have multiple sites.
     */
    const val SERVERS_ENABLED = true

    /**
     * Notification channels.
     * DISABLED: De momento ocultado hasta que se defina el flujo de notificaciones.
     */
    const val NOTIFICATIONS_ENABLED = false

    /**
     * Notification history.
     * DISABLED: Depende de NOTIFICATIONS_ENABLED.
     */
    const val NOTIFICATION_HISTORY_ENABLED = false

    /**
     * Screen orientation lock.
     * ENABLED: Essential for mounted tablets in tractors/control rooms.
     */
    const val SCREEN_ORIENTATION_ENABLED = true

    /**
     * Keep screen on.
     * ENABLED: Control room displays must stay visible 24/7.
     */
    const val KEEP_SCREEN_ON_ENABLED = true

    /**
     * Fullscreen mode.
     * ENABLED: Valuable for mounted tablets, maximizes dashboard visibility.
     */
    const val FULLSCREEN_ENABLED = true

    /**
     * Language selection.
     * ENABLED: Multilingual farm staff need localized interface.
     */
    const val LANGUAGE_ENABLED = true

    /**
     * Kiosk mode (allow home app).
     * ENABLED: Essential for dedicated monitoring stations.
     */
    const val KIOSK_MODE_ENABLED = true

    /**
     * Troubleshooting / developer tools.
     * ENABLED: Essential for field support and diagnostics.
     */
    const val TROUBLESHOOTING_ENABLED = true

    /**
     * Crash reporting (opt-in).
     * ENABLED: Important for stability, but opt-in only.
     */
    const val CRASH_REPORTING_ENABLED = true

    /**
     * Pinch-to-zoom.
     * ENABLED: Essential for farm maps and diagrams. Always on, not configurable.
     */
    const val PINCH_TO_ZOOM_ENABLED = true

    /**
     * Always show dashboard on app start.
     * ENABLED: Hardcoded ON - emergency access to main dashboard.
     */
    const val ALWAYS_SHOW_DASHBOARD_ENABLED = true
}
