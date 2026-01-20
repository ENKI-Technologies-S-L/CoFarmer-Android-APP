# CoFarmer Android App - Fork Documentation

This document describes all modifications made to the original [Home Assistant Companion for Android](https://github.com/home-assistant/android) to create the **Enkitek CoFarmer** branded application.

## Fork Overview

| Property | Original | CoFarmer |
|----------|----------|----------|
| **App Name** | Home Assistant | CoFarmer |
| **Package ID** | `io.homeassistant.companion.android` | `com.enkitek.cofarmer` |
| **Brand Owner** | Home Assistant / Open Home Foundation | Enkitek |
| **Target Users** | Smart home enthusiasts | Agricultural operators, farm technicians |
| **Primary Use Case** | Home automation | IoT platform for agricultural environments |

---

## Summary of Changes

### 1. Package & Application ID Rebrand

**Files Modified:**
- `build-logic/convention/src/main/kotlin/AndroidApplicationConventionPlugin.kt`

**Changes:**
- Application ID changed from `io.homeassistant.companion.android` to `com.enkitek.cofarmer`
- Debug suffix remains `.debug` for development builds

---

### 2. Visual Identity - App Icons

**Files Modified:**
- `app/src/main/res/drawable/ic_launcher_foreground.xml`
- `app/src/main/res/drawable/ic_launcher_foreground_round.xml`
- `app/src/main/res/drawable/ic_launcher_monochrome.xml`
- `app/src/main/res/drawable/ic_launcher_monochrome_round.xml`
- `app/src/debug/res/mipmap-anydpi-v26/ic_launcher.xml`
- `app/src/debug/res/mipmap-anydpi-v26/ic_launcher_round.xml`
- `wear/src/main/res/drawable/ic_launcher_foreground.xml`
- `wear/src/main/res/drawable/ic_launcher_foreground_round.xml`

**Changes:**
- Replaced Home Assistant logo with Enkitek "E" logo (gear + magnifying glass icon)
- Applied to all app variants: main, debug, and Wear OS
- Monochrome icons updated for Android 13+ themed icons

---

### 3. Visual Identity - Splash Screen

**Files Modified:**
- `app/src/main/res/drawable/app_icon_launch.xml`
- `app/src/main/res/drawable-v31/app_icon_launch_screen.xml`

**Changes:**
- Replaced Home Assistant splash icon with Enkitek "E" logo
- Optimized for Android 12+ splash screen API (centered, 288dp)

---

### 4. Visual Identity - Brand Colors

**Files Modified:**
- `app/src/main/res/values/colors.xml`
- `common/src/main/res/values/colors.xml`
- `common/src/main/res/values-night/colors.xml`
- `wear/src/main/res/values/colors.xml`
- `common/src/main/kotlin/io/homeassistant/companion/android/common/compose/theme/HAColors.kt`
- `wear/src/main/kotlin/io/homeassistant/companion/android/theme/Color.kt`

**CoFarmer Brand Palette:**
| Color | Hex Code | Usage |
|-------|----------|-------|
| Primary (Green) | `#04D288` | Main brand color, buttons, accents |
| Secondary (Blue) | `#0066CC` | Links, secondary actions |
| Accent (Teal) | `#03B799` | Highlights, gradients |
| Background | `#FFFFFF` / `#1A1A1A` | Light/Dark mode backgrounds |

**Changes:**
- Updated `HABrandColors` object with CoFarmer palette
- Added `Primary`, `Secondary`, `Accent` color properties
- Updated light/dark color schemes throughout the app

---

### 5. Visual Identity - Scanning Animation

**Files Modified:**
- `app/src/main/res/drawable/dots.xml`
- `app/src/main/res/drawable-v24/dots.xml`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/serverdiscovery/ServerDiscoveryScreen.kt`

**Changes:**
- Replaced Home Assistant blue (#18BCF2) with CoFarmer teal (#03B799)
- Added gradient animation (green → blue) in scanning icon
- Updated animated icon center to use `Brush.linearGradient`

---

### 6. Visual Identity - Notification Icons

**Files Modified:**
- `common/src/main/res/drawable/ic_stat_ic_notification.xml`
- `common/src/main/res/drawable/ic_stat_ic_notification_blue.xml`

**Changes:**
- Replaced Home Assistant logo with Enkitek "E" icon in notification bar

---

### 7. Visual Identity - Branding Icon

**Files Modified:**
- `app/src/main/res/drawable/ic_home_assistant_branding.xml`

**Changes:**
- Replaced Home Assistant branding graphic with Enkitek logo
- Used in server discovery list and various UI elements

---

### 8. String Replacements - Terminology

**Files Modified:**
- `common/src/main/res/values/strings.xml`

**Terminology Changes:**
| Original | CoFarmer |
|----------|----------|
| "Home Assistant" | "CoFarmer" |
| "home network" | "local network" |
| "at home" | "on your farm" |
| "away from home" | "away from your farm" |
| "homeassistant.local" | "cofarmer.local" |

**Strings Updated (~50+):**
- Onboarding screens
- Error messages
- Settings descriptions
- Permission explanations
- Connection status messages

---

### 9. New Feature - QR Code Scanner

**Files Created:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/qrscanner/QrScannerScreen.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/qrscanner/navigation/QrScannerNavigation.kt`

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/OnboardingNavigation.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/serverdiscovery/ServerDiscoveryScreen.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/serverdiscovery/navigation/ServerDiscoveryNavigation.kt`

**Feature Description:**
- New QR code scanning option in server discovery flow
- Supports URLs: `http://`, `https://`, and custom `cofarmer://connect?url=` scheme
- Custom animated viewfinder with CoFarmer brand colors (green/blue corners)
- Graceful camera permission handling with fallback to manual entry
- Positioned as primary alternative to auto-discovery (before manual entry)

**New Strings Added:**
- `qr_scanner_button`
- `qr_scanner_title`
- `qr_scanner_subtitle`
- `qr_scanner_help`
- `qr_scanner_error_invalid_url`
- `qr_scanner_permission_*` (multiple permission-related strings)

---

### 10. Branding Assets

**Files Added:**
- `branding-assets/logos/` - 20 Enkitek logo variants (SVG)
- `branding-assets/fonts/Lato/` - Lato font family (pending integration)

---

## Files NOT Modified (Intentionally Preserved)

The following remain unchanged to maintain compatibility with upstream:

- Core WebSocket/REST API communication (`common/src/main/kotlin/.../data/`)
- Sensor collection logic
- Widget implementations
- Wear OS app functionality (except icons/colors)
- Build configuration (Gradle, dependencies)
- Database schemas
- Notification handling logic

---

## Pending Work

### High Priority
- [ ] Update remaining "Home Assistant" references in help URLs
- [ ] Replace "homeassistant.io" documentation links with CoFarmer docs
- [ ] Integrate Lato font family
- [ ] Update README.md for CoFarmer

### Medium Priority
- [ ] Review and update Wear OS specific strings
- [ ] Create CoFarmer-specific onboarding illustrations
- [ ] Update app store metadata (descriptions, screenshots)

### Low Priority
- [ ] Custom error pages
- [ ] About screen with Enkitek branding
- [ ] Changelog updates

---

## Upstream Synchronization

See [UPSTREAM_SYNC.md](UPSTREAM_SYNC.md) for detailed instructions on how to merge updates from the original Home Assistant Android repository.

---

## Version History

| Date | Version | Changes |
|------|---------|---------|
| 2026-01-20 | 1.0.0-alpha | Initial rebrand: package ID, icons, colors, strings, QR scanner |

---

## License

This fork maintains the same license as the original Home Assistant Companion for Android.
See [LICENSE.md](LICENSE.md) for details.
