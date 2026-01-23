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
- [x] Update remaining "Home Assistant" references in help URLs
- [x] Replace "homeassistant.io" documentation links with CoFarmer docs
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

## Day 5 Changes - Settings UX & Localization

### 11. Settings Terminology Update

**Files Modified:**
- `common/src/main/res/values/strings.xml`

**Changes:**
| Original | CoFarmer |
|----------|----------|
| "Servers & devices" | "Farm Sites" |
| "Other settings" | "App Settings" |
| "Add server" | "Add farm" |
| "server" (in various contexts) | "Farm" |

---

### 12. Multilanguage Support

**Files Created:**
- `common/src/main/res/values-es/strings.xml` (~85 Spanish strings)
- `common/src/main/res/values-fr/strings.xml` (~85 French strings)
- `common/src/main/res/values-de/strings.xml` (~85 German strings)

**Files Modified:**
- `common/src/main/res/xml/locales_config.xml` (added es, fr, de locales)

**Languages Added:**
- Spanish (es) - "Instalaciones", "Configuración de la aplicación"
- French (fr) - "Sites agricoles", "Paramètres de l'application"
- German (de) - "Betriebsstandorte", "App-Einstellungen"

---

### 13. URL Rebranding

**Files Modified:**
- `common/src/main/res/values/strings.xml`
- `app/src/main/res/xml/preferences.xml`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/connection/ConnectionErrorScreen.kt`

**URLs Updated:**
| Original | CoFarmer |
|----------|----------|
| `home-assistant.io/privacy` | `privacy.enkitek.eu` |
| `github.com/home-assistant/android/releases` | `github.com/Enkitek/CoFarmer-Android-APP/releases` |
| `community.home-assistant.io/*` | `docs.cofarmer.enkitek.eu/support` |
| `github.com/home-assistant/android/issues` | `github.com/Enkitek/CoFarmer-Android-APP/issues` |
| Discord HA channel | `docs.cofarmer.enkitek.eu/community` |

---

### 14. QR Code Primary CTA

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/serverdiscovery/ServerDiscoveryScreen.kt`

**Changes:**
- Reordered buttons: QR Scanner now primary (HAAccentButton)
- Network scanning with animation in middle
- Manual setup as secondary option at bottom

---

### 15. Feature Documentation

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/util/CoFarmerFeatures.kt`

**Enhanced Documentation:**
- Crash Reporting (Sentry): behavior, opt-out, enterprise deployment
- Home App / Launcher Mode: kiosk deployments for farm tablets
- Pinch-to-Zoom: industrial tablet considerations
- All display/UI settings documented for farm use cases

---

### 16. Local Network Warning Chip

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/connection/ConnectionErrorScreen.kt`
- `app/src/test/kotlin/io/homeassistant/companion/android/onboarding/connection/ConnectionErrorScreenTest.kt`

**Feature Description:**
- Yellow warning chip displayed when connection errors occur
- Shows "Only works on local network" message with info icon
- Provides user-friendly explanation of network requirements
- Includes unit tests for warning chip rendering

---

### 17. QR Scanner as Direct Entry Point

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/qrscanner/navigation/QrScannerNavigation.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/qrscanner/QrScannerScreen.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/OnboardingNavigation.kt`

**Changes:**
- QR Scanner can now be navigated to directly from ServerDiscovery
- Added `onNavigateToManualServer` callback for "Enter manually" button
- Improved navigation flow with proper back stack handling
- Users can go directly to manual entry from QR scanner if needed

---

### 18. Custom Branding Splash Screen

**Files Created:**
- `app/src/main/kotlin/io/homeassistant/companion/android/launch/BrandingSplashScreen.kt`
- `app/src/main/res/drawable/splash_logo_cofarmer.xml`
- `app/src/main/res/drawable/splash_text_cofarmer.xml`
- `app/src/main/res/drawable/splash_tagline.xml`
- `app/src/main/res/drawable/splash_by_enkitek.xml`
- `app/src/main/res/drawable-night/splash_tagline_night.xml`
- `app/src/main/res/drawable-night/splash_by_enkitek_night.xml`
- `app/src/main/res/drawable-night/launch_screen_background.xml`

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/launch/LaunchActivity.kt`
- `app/src/main/res/drawable/launch_screen_background.xml`

**Feature Description:**
- Custom Compose splash screen with full CoFarmer branding
- Displays for 2.5 seconds before transitioning to next screen
- Contains:
  - Enkitek "E" logo (gradient green → blue)
  - "COFARMER" text (600 weight, custom styling)
  - "IoT for agriculture" tagline
  - "by ENKITEK" credit at bottom
- Supports light/dark mode with appropriate backgrounds
- Native Android 12+ splash screen shows white background (no icon)

---

### 19. App Icon Complete Rebrand

**Files Created:**
- `app/src/main/res/drawable/ic_launcher_background.xml` (gradient background)
- `branding-assets/icons/ic_launcher_full.svg` (master SVG source)
- `branding-assets/icons/ic_launcher_foreground.svg`
- `branding-assets/icons/ic_launcher_background.svg`

**Files Modified:**
- `app/src/main/res/drawable/ic_launcher_foreground.xml` (white E with evenOdd cutout)
- `app/src/main/res/drawable/ic_launcher_monochrome.xml`
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`
- `app/src/main/res/mipmap-mdpi/ic_launcher.png`
- `app/src/main/res/mipmap-mdpi/ic_launcher_round.png`
- `app/src/main/res/mipmap-hdpi/ic_launcher.png`
- `app/src/main/res/mipmap-hdpi/ic_launcher_round.png`
- `app/src/main/res/mipmap-xhdpi/ic_launcher.png`
- `app/src/main/res/mipmap-xhdpi/ic_launcher_round.png`
- `app/src/main/res/mipmap-xxhdpi/ic_launcher.png`
- `app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png`
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png`
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png`
- `wear/src/main/res/drawable/ic_launcher_foreground.xml`
- `app/src/main/res/drawable-v31/app_icon_launch_screen.xml`
- `app/src/main/res/values-v31/styles.xml`

**Icon Design:**
- **Background**: Linear gradient from #04D288 (green) to #0066CC (blue)
- **Foreground**: White Enkitek "E" logo with characteristic inner cutout
- **Technical**: Uses `fillType="evenOdd"` for proper hole rendering
- **PNG Fallbacks**: Generated from SVG for all density buckets (mdpi through xxxhdpi)
- **Monochrome**: Simplified E outline for Android 13+ themed icons
- **Wear OS**: Consistent E logo on watch app

---

### 20. StrictMode OEM Rules

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/util/IgnoreViolationRules.kt`

**OEM-Specific Rules Added (40+):**
- Samsung: Knox, GameSDK, SmartClip, SemEmergency
- Xiaomi/MIUI: Security, CloudService, Passport
- OnePlus: OplusOS, Camera, NFC managers
- Oppo/Realme/ColorOS: Athena, SysUIProvider
- Vivo: SuperPower, Permission handlers
- Huawei/Honor: HwAppManager, HwPartPowerOffice
- Lenovo: Device policy managers
- Meizu: Flyme services
- ZTE: System services
- Generic Chinese OEMs: Various system services

**Purpose:**
- Prevents false-positive StrictMode crashes in debug builds
- Allows app to run on diverse Android devices without OEM-specific issues
- Each rule documented with OEM source and violation type

---

## Upstream Synchronization

See [UPSTREAM_SYNC.md](UPSTREAM_SYNC.md) for detailed instructions on how to merge updates from the original Home Assistant Android repository.

---

## Version History

| Date | Version | Changes |
|------|---------|---------|
| 2026-01-20 | 1.0.0-alpha | Initial rebrand: package ID, icons, colors, strings, QR scanner |
| 2026-01-21 | 1.0.0-alpha.5 | Settings UX: Farm Sites, multilang (ES/FR/DE), URL rebrand, QR primary CTA |
| 2026-01-22 | 1.0.0-alpha.6 | Complete app icon rebrand, custom branding splash screen, local network warning, 40+ OEM StrictMode rules |
| 2026-01-23 | 1.0.0-alpha.7 | Settings improvements, onboarding enhancements, confirmation popups |

---

### 21. Remove "Assist from Anywhere" Popup

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/settings/SettingsPresenter.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/settings/SettingsPresenterImpl.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/settings/SettingsFragment.kt`

**Changes:**
- Removed `showAssistFromAnywhere()` method from SettingsPresenter interface
- Removed implementation that showed the Assist dialog popup
- CoFarmer doesn't use voice assistant features, so this popup was unnecessary

---

### 22. Fix Privacy Policy URL

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/settings/SettingsFragment.kt`

**Changes:**
- Changed hardcoded URL from `https://www.home-assistant.io/privacy/` to use string resource
- Now correctly points to `https://privacy.enkitek.eu`

---

### 23. Remove Share Button from Farm Settings

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/settings/server/ServerSettingsFragment.kt`

**Changes:**
- Removed share menu option from toolbar
- `onCreateOptionsMenu()` now creates empty menu
- `onOptionsItemSelected()` simplified to only handle navigation

---

### 24. Distinctive Colors for Menu Icons

**Files Modified:**
- `common/src/main/res/values/colors.xml`
- `app/src/main/res/xml/preferences.xml`

**New Colors Added:**
| Color | Hex Code | Usage |
|-------|----------|-------|
| `iconTintEnkitekVerde` | `#04D288` | Primary menu items (Farm Sites, Companion App) |
| `iconTintEnkitekAzul` | `#0066CC` | Secondary items (Sensors, Notifications, Shortcuts) |
| `iconTintEnkitekNegro` | `#0A0A0A` | Utility items (Privacy, Acknowledgments) |

**Icons Updated:**
- Farm Sites → Verde
- Companion App → Verde
- Manage sensors → Azul
- Notifications → Azul
- Shortcuts → Azul
- Privacy Policy → Negro
- Acknowledgments → Negro
- Developer Settings → Negro

---

### 25. Hub Setup Onboarding Screen

**Files Created:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/hubsetup/HubSetupScreen.kt`
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/hubsetup/navigation/HubSetupNavigation.kt`

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/OnboardingNavigation.kt`
- `common/src/main/res/values/strings.xml`

**Feature Description:**
Modern step-by-step hub setup instructions with:
- Visual step cards with circular icon backgrounds
- Icons: Power (plug), Cable (ethernet), Lightbulb (LED), Timer (wait)
- Each step has title and descriptive subtitle
- Material3 Card elevation for visual hierarchy
- "Continue" button to proceed to server discovery

**New Strings:**
- `hub_setup_title` → "Set up your CoFarmer Hub"
- `hub_setup_subtitle` → "Follow these steps to connect your hub"
- `hub_setup_step1_*` → Power connection instructions
- `hub_setup_step2_*` → Ethernet connection instructions
- `hub_setup_step3_*` → LED indicator guidance
- `hub_setup_step4_*` → Wait time instructions
- `hub_setup_continue` → "Continue"

---

### 26. Local Connection Confirmation Popup

**Files Modified:**
- `app/src/main/kotlin/io/homeassistant/companion/android/onboarding/serverdiscovery/ServerDiscoveryScreen.kt`
- `common/src/main/kotlin/io/homeassistant/companion/android/common/compose/theme/HAColors.kt`
- `common/src/main/res/values/strings.xml`

**Feature Description:**
When user clicks "Connect" on a locally-discovered server:
1. Shows confirmation AlertDialog with warning icon (amber)
2. Explains that local connection only works on same Wi-Fi network
3. Recommends using QR code for remote access
4. Two buttons:
   - "I understand, connect anyway" → Proceeds with local connection
   - "Use QR code instead" → Navigates to QR scanner

**New Color:**
- `HABrandColors.Warning` → `#FF9800` (Amber/Orange for warnings)

**New Strings:**
- `local_connection_confirm_title` → "Local connection only"
- `local_connection_confirm_message` → Warning about Wi-Fi requirement and QR recommendation
- `local_connection_confirm_understand` → "I understand, connect anyway"
- `local_connection_confirm_use_qr` → "Use QR code instead"

**Components Created:**
- `LocalConnectionConfirmationDialog` composable
- Integrates with `OneServerFound` bottom sheet

---

### 27. Language Consistency Review

**Files Modified:**
- `common/src/main/res/values/strings.xml`

**Strings Reviewed and Updated (~15):**
- Ensured consistent "CoFarmer" branding throughout
- "farm" terminology instead of "home"
- "Hub" for server references in user-facing text
- Agricultural context in descriptions

---

## Pending Work

### High Priority
- [x] ~~Update remaining "Home Assistant" references in help URLs~~
- [x] ~~Replace "homeassistant.io" documentation links with CoFarmer docs~~
- [x] ~~Fix privacy URL hardcoded in SettingsFragment~~
- [ ] Integrate Lato font family
- [ ] Update README.md for CoFarmer

### Medium Priority
- [ ] Review and update Wear OS specific strings
- [ ] Create CoFarmer-specific onboarding illustrations
- [ ] Update app store metadata (descriptions, screenshots)
- [ ] Add Spanish translations for new Day 5 strings

### Low Priority
- [ ] Custom error pages
- [ ] About screen with Enkitek branding
- [ ] Changelog updates

---

## License

This fork maintains the same license as the original Home Assistant Companion for Android.
See [LICENSE.md](LICENSE.md) for details.
