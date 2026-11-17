# CoFarmer Assistant Rebrand Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rename the app to "CoFarmer Assistant" and apply the new CoFarmer brand (CO link logo, gradient palette, Sora for titles, robot "DEV" debug icon) to the native Android app screens.

**Architecture:** Resource-level rebrand. Strings in `common` (+ translations), colors in `common`/`app`/`wear` resources and the Compose palette in `HAColors.kt`, launcher icons as adaptive icons (vector foreground + legacy PNG mipmaps), a Sora `FontFamily` used only by headline styles, and a reworked `BrandingSplashScreen`. No behavior changes.

**Tech Stack:** Android resources (XML, VectorDrawable, adaptive icons), Jetpack Compose, Robolectric unit tests, ImageMagick 7 (`magick`) for raster generation, Gradle (JDK = Android Studio JBR).

**Spec:** `docs/superpowers/specs/2026-11-16-cofarmer-assistant-rebrand-design.md`

## Global Constraints

- Branch: `feature/cofarmer-assistant-rebrand` (from `release/2026.12`). Never commit to `main`.
- App name: exactly `CoFarmer Assistant`.
- Rename rule: "CoFarmer" → "CoFarmer Assistant" only where the string refers **to the app**. Never change "CoFarmer Hub", "CoFarmer Cloud", "CoFarmer URL", server/connection/certificate errors, Documentation/Forum/Discord/GitHub, CoFarmer zones/dashboards.
- Colors: primary/gradient_start `#00C389`, secondary/gradient_end `#1E6BD6`, accent `#0F97AF`, preference suggestion `#1F00C389`. Old values `#04D288`, `#0066CC`, `#03B799` must not remain anywhere under `app/src`, `common/src`, `wear/src`, `automotive/src`.
- Sora (SemiBold/Bold, OFL) only on headings and brand moments; body text stays Roboto.
- Debug icon (robot "DEV") only under `app/src/debug/res`; the existing DevPlayground icon is untouched.
- `minSdk` 23: no gradient VectorDrawable may be inflated by the platform on API 23 (adaptive icons are 26+, Compose `painterResource`/`vectorResource` are fine).
- Build env: `JAVA_HOME="/c/Program Files/Android/Android Studio/jbr"`, `ANDROID_HOME="C:\Users\victo\AppData\Local\Android\Sdk"`. `app/google-services.json` and `wear/google-services.json` exist locally (gitignored).
- Translations to update when a key exists there: `values-es, -fr, -de, -pt, -ar, -zh, -hi, -ko, -ja` under `common/src/main/res`.

## Review Focus

1. **Launcher label truncation** (18 chars) on MIUI → name must be fully readable or the user is consulted. Checked in Task 7 on the Redmi.
2. **Icon art clipped by circle/squircle masks** (production link mark and the "DEV" badge) → whole art visible. Checked in Tasks 3–4 with mask previews.
3. **API 23 launch window** referencing a gradient vector → crash on Android 6. Pinned by Task 3 step (grep of launch-window drawables + `minSdk` check).
4. **Mixed naming on one screen** (e.g. welcome title "CoFarmer Assistant" but body "Allow CoFarmer…") → pinned by Task 1 tests on representative keys in en and es.
5. **Dark mode splash** wordmark invisible on dark background → pinned by Task 6 dark-mode screenshot on device.

---

### Task 1: App name and app-referring strings

**Files:**
- Modify: `common/src/main/res/values/strings.xml` and `common/src/main/res/values-{es,fr,de,pt,ar,zh,hi,ko,ja}/strings.xml`
- Modify: `fastlane/metadata/android/en-US/title.txt` → `CoFarmer Assistant`
- Test: `app/src/test/kotlin/io/homeassistant/companion/android/branding/BrandingStringsTest.kt`

**Interfaces:**
- Produces: `commonR.string.app_name` = "CoFarmer Assistant" (used by app/wear manifests unchanged).

- [ ] **Step 1: Write the failing test** — Robolectric (`@RunWith(RobolectricTestRunner::class)`, `@Config(application = HiltTestApplication::class)` as in `ConnectionErrorScreenTest`), reading via `ApplicationProvider.getApplicationContext<Context>().getString(...)`:

```kotlin
@Test fun `app name is CoFarmer Assistant`() = assertEquals("CoFarmer Assistant", getString(commonR.string.app_name))
@Test fun `welcome title uses app name`() = assertEquals("Welcome to CoFarmer Assistant", getString(commonR.string.hub_setup_title))
@Test fun `app-referring permission text renamed`() = assertTrue(getString(commonR.string.notification_permission_summary).startsWith("CoFarmer Assistant does not have permission"))
@Test fun `hub strings unchanged`() {
    assertEquals("Do you have a CoFarmer Hub?", getString(commonR.string.hub_setup_question))
    assertEquals("Unable to connect to CoFarmer.", getString(commonR.string.error_connection_failed))
    assertEquals("Use CoFarmer Cloud", getString(commonR.string.input_cloud))
}
@Config(qualifiers = "es") @Test fun `spanish app-referring text renamed`() =
    assertTrue(getString(commonR.string.suggestion_notifications_summary).contains("CoFarmer Assistant"))
@Config(qualifiers = "es") @Test fun `spanish hub text unchanged`() =
    assertEquals("No se puede conectar con CoFarmer.", getString(commonR.string.error_connection_failed))
```

- [ ] **Step 2: Run, expect FAIL** — `./gradlew :app:testFullDebugUnitTest --tests "*BrandingStringsTest*"` → failures on app name / welcome / permission / es renamed.
- [ ] **Step 3: Rename** — set `app_name`; apply the rename rule to every string in `values/strings.xml` that mentions CoFarmer, and mirror each changed key in the 9 translations where present. Do not touch `translatable="false"` URLs. Keep escapes (`\'`, `\"`, `%1$s`) intact. Set `title.txt`.
- [ ] **Step 4: Run, expect PASS** (same command) and `./gradlew :common:lintRelease` → 0 errors.
- [ ] **Step 5: Commit** — `feat(branding): rename app to CoFarmer Assistant`

### Task 2: Brand colors

**Files:**
- Modify: `common/src/main/res/values/colors.xml`, `common/src/main/res/values-night/colors.xml`, `app/src/main/res/values/colors.xml`, `app/src/main/res/values-night/colors.xml`, `wear/src/main/res/values/colors.xml`
- Modify: every drawable under `app/src/main/res/drawable*` containing an old hex (currently: `app_icon_launch.xml`, `dots.xml`, `ic_location_secure.xml`, `ic_location_tracking.xml`, `launch_screen_background.xml`, `drawable-night/launch_screen_background.xml`, `drawable-v24/dots.xml`, `drawable-v31/app_icon_launch_screen.xml`, plus splash/launcher drawables replaced later in Tasks 3/6)
- Modify: `common/src/main/kotlin/.../common/compose/theme/HAColors.kt:196-220` (CoFarmerGreen*/CoFarmerBlue* tonal palettes), `wear/src/main/kotlin/.../theme/Color.kt`
- Test: `app/src/test/kotlin/io/homeassistant/companion/android/branding/BrandingColorsTest.kt`

**Interfaces:**
- Produces: `commonR.color.cofarmer_primary|secondary|accent|gradient_start|gradient_end`; `HAColors` `CoFarmerGreen50 = Color(0xFF00C389)`, `CoFarmerBlue40 = Color(0xFF1E6BD6)` (tone names unchanged).

- [ ] **Step 1: Failing test** — Robolectric, `context.getColor(...)`:
  `cofarmer_primary == 0xFF00C389.toInt()`, `cofarmer_secondary == 0xFF1E6BD6.toInt()`, `cofarmer_accent == 0xFF0F97AF.toInt()`, `colorPreferenceSuggestion == 0x1F00C389`; plus `@Config(qualifiers = "night")` variant of the primary assertion.
- [ ] **Step 2: Run, expect FAIL** — `./gradlew :app:testFullDebugUnitTest --tests "*BrandingColorsTest*"`.
- [ ] **Step 3: Replace colors** — XML values per Global Constraints. In `HAColors.kt`, regenerate both tonal palettes so the base tone equals the new color and the other tones keep their current lightness steps (mix toward black/white with the same ratios the existing palette implies). Update the comment lines to the new base hex.
- [ ] **Step 4: Run, expect PASS**, then `git grep -inE "04D288|0066CC|03B799" -- app/src common/src wear/src automotive/src` → no output (splash/launcher files may remain until Tasks 3/6; list them in the commit message if so).
- [ ] **Step 5: Commit** — `feat(branding): apply CoFarmer gradient palette`

### Task 3: Production launcher icon, welcome logo, brand assets

**Files:**
- Move: `Branding_CoFarmer/*` → `branding-assets/cofarmer/` (keep logos, SVG, robot PNG, mockup, og image; replace `Sora_Font.zip` with `branding-assets/cofarmer/fonts/OFL.txt` only)
- Replace content: `app/src/main/res/drawable/ic_launcher_foreground.xml`, `ic_launcher_foreground_round.xml`, `ic_launcher_monochrome.xml`, `ic_launcher_monochrome_round.xml`, `ic_assist_launcher_foreground.xml`, `ic_launcher_background.xml` (white), `ic_home_assistant_branding.xml` (welcome logo, file name kept to avoid churn), `splash_logo_cofarmer.xml`
- Replace: `app/src/main/res/mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/ic_launcher*.{png,webp}` (keep existing names/formats)
- Replace: `wear/src/main/res/drawable/ic_launcher_foreground.xml`, `wear/src/main/res/drawable-round/*launcher*`, `wear/src/main/res/mipmap-anydpi/*` targets as needed
- Set: `colorLauncherBackground` = `#FFFFFF` in `common/src/main/res/values/colors.xml`

**Interfaces:**
- Consumes: Task 2 colors (gradient `#00C389` → `#1E6BD6`, horizontal, as in the SVG).
- Produces: `R.drawable.splash_logo_cofarmer` and `R.drawable.ic_home_assistant_branding` = the CO link mark with gradient (used by Task 6).

- [ ] **Step 1: Convert the SVG** — `branding-assets/cofarmer/logo-cofarmer-vector.svg` is one `<path>` with `fill="url(#grad)"`, viewBox 1024. Emit VectorDrawables reusing its `d` as `pathData`, gradient via `<aapt:attr name="android:fillColor"><gradient android:type="linear" .../>`. Launcher foreground: viewport 108×108 dp, mark scaled into the centered 66 dp safe circle. Monochrome: same path, solid `#FFFFFF`. Splash/welcome vectors: tight viewport around the mark.
- [ ] **Step 2: Legacy mipmaps** — rasterize with `magick` (white rounded-square/circle background + mark) at 48/72/96/144/192 px; round variants with circular mask.
- [ ] **Step 3: API 23 check** — `git grep -n "splash_logo_cofarmer\|ic_launcher_foreground\|ic_home_assistant_branding" -- app/src/main/res` must show no reference from a `values*/styles.xml` window background or `layer-list` used on API < 24. Fix by pointing such references at a mipmap PNG.
- [ ] **Step 4: Mask preview** — render the foreground over white with circle and squircle masks (`magick`) into the scratchpad; confirm visually no clipping; show the user.
- [ ] **Step 5: Build** — `./gradlew :app:assembleFullDebug :wear:assembleDebug` → BUILD SUCCESSFUL.
- [ ] **Step 6: Commit** — `feat(branding): CO link launcher icon and logo` (includes the asset move).

### Task 4: Robot "DEV" debug launcher icon

**Files:**
- Replace: `app/src/debug/res/mipmap-anydpi-v26/ic_launcher.xml`, `ic_launcher_round.xml` (background `@color/ic_launcher_debug_background`, foreground `@mipmap/ic_launcher_debug_foreground`, monochrome = production monochrome)
- Create: `app/src/debug/res/mipmap-{mdpi..xxxhdpi}/ic_launcher_debug_foreground.webp`, `app/src/debug/res/values/ic_launcher_debug_background.xml` (color sampled from the robot PNG corner)
- Replace: `app/src/debug/res/mipmap-{mdpi..xxxhdpi}/ic_launcher.png`, `ic_launcher_round.png`
- Leave untouched: all `ic_launcher_dev_playground*` files

- [ ] **Step 1: Foreground** — from `branding-assets/cofarmer/Robot codificador de bucles con DEV.png`: trim to non-background content, scale so its bounding-box diagonal ≤ 66/108 of the canvas, center on a 108-unit canvas filled transparent; export WebP at 108/162/216/324/432 px.
- [ ] **Step 2: Legacy PNGs** — full robot art on its background, 48…192 px, round variants circle-masked.
- [ ] **Step 3: Mask preview** (circle + squircle) into scratchpad; "DEV" badge fully visible.
- [ ] **Step 4: Build + install** — `./gradlew :app:assembleFullDebug`, `adb install -r app/build/outputs/apk/full/debug/app-full-debug.apk`; screenshot launcher (`adb exec-out screencap -p`), confirm robot icon.
- [ ] **Step 5: Commit** — `feat(branding): robot DEV icon for debug builds`

### Task 5: Sora for headings

**Files:**
- Create: `common/src/main/res/font/sora_semibold.ttf`, `common/src/main/res/font/sora_bold.ttf` (from `Sora_Font.zip` `static/`)
- Create: `common/src/main/kotlin/.../common/compose/theme/HAFontFamily.kt` → `val SoraFontFamily: FontFamily` (`Font(R.font.sora_semibold, FontWeight.SemiBold)`, `Font(R.font.sora_bold, FontWeight.Bold)`)
- Modify: `HATextStyle.kt` — `Headline` and `HeadlineMedium` use `fontFamily = SoraFontFamily`, `fontWeight = FontWeight.SemiBold`; `Body`, `BodyMedium`, `UserInput`, `Button` unchanged
- Modify: `app/src/main/res/values/styles.xml` — toolbar title text appearance and preference category title use `@font/sora_semibold`
- Test: `common/src/test/kotlin/.../common/compose/theme/HATextStyleFontTest.kt` (Compose + Robolectric; if `common` has no Compose test setup, place it in `app/src/test/.../branding/`)

**Interfaces:**
- Produces: `SoraFontFamily` (consumed by Task 6).

- [ ] **Step 1: Failing test** — capture styles inside `setContent {}`: `assertEquals(SoraFontFamily, HATextStyle.Headline.fontFamily)`, same for `HeadlineMedium`; `assertNotEquals(SoraFontFamily, HATextStyle.Body.fontFamily)`; `assertNotNull(ResourcesCompat.getFont(context, commonR.font.sora_bold))`.
- [ ] **Step 2: Run, expect FAIL** (unresolved `SoraFontFamily`).
- [ ] **Step 3: Implement** as listed in Files.
- [ ] **Step 4: Run, expect PASS**; `./gradlew :app:assembleFullDebug`.
- [ ] **Step 5: Commit** — `feat(branding): Sora for headings`

### Task 6: Splash screen

**Files:**
- Modify: `app/src/main/kotlin/io/homeassistant/companion/android/launch/BrandingSplashScreen.kt`
- Modify: `common/src/main/res/values/strings.xml` (+ `values-es`): add `splash_brand_name` = "CoFarmer" (`translatable="false"`), `splash_brand_suffix` = "Assistant" (`translatable="false"`), `splash_tagline` = "Control your farm from anywhere" / es "Controla tu granja desde cualquier lugar", `splash_by_enkitek` = "by ENKITEK" (`translatable="false"`), `splash_logo_description` = "CoFarmer Assistant logo" / es "Logo de CoFarmer Assistant"
- Delete if unreferenced afterwards: `app/src/main/res/drawable/splash_text_cofarmer.xml`, `splash_tagline.xml`
- Test: `app/src/test/kotlin/io/homeassistant/companion/android/launch/BrandingSplashScreenTest.kt`

**Interfaces:**
- Consumes: `R.drawable.splash_logo_cofarmer` (Task 3), `SoraFontFamily` (Task 5), `CoFarmerGreen`/accent `#0F97AF` (Task 2).

Layout (from spec): logo 120 dp → "CoFarmer" (Sora Bold, `#333333`, white in dark mode) + "Assistant" (Sora SemiBold, `#0F97AF`) on one line → tagline (Roboto, existing subtitle color) → "by ENKITEK" at bottom. Remove the private `CoFarmerGreen = Color(0xFF04D288)`.

- [ ] **Step 1: Failing test** — Compose test: `onNodeWithText("CoFarmer").assertExists()`, `onNodeWithText("Assistant").assertExists()`, `onNodeWithText("Control your farm from anywhere").assertExists()`, `onNodeWithContentDescription("CoFarmer Assistant logo").assertExists()`; `onNodeWithText("COFARMER").assertDoesNotExist()`.
- [ ] **Step 2: Run, expect FAIL.**
- [ ] **Step 3: Implement.**
- [ ] **Step 4: Run, expect PASS**; `git grep -inE "04D288|0066CC|03B799" -- app/src common/src wear/src automotive/src` → no output.
- [ ] **Step 5: Commit** — `feat(branding): CoFarmer Assistant splash`

### Task 7: Full verification on device

- [ ] **Step 1:** `./gradlew :app:assembleFullDebug :wear:assembleDebug :common:lintRelease :app:testFullDebugUnitTest` → BUILD SUCCESSFUL, lint 0 errors, tests green.
- [ ] **Step 2:** `./gradlew :app:updateFullDebugScreenshotTest`; review changed reference PNGs (only branding-related diffs expected); commit them with `test: update screenshot references for rebrand`.
- [ ] **Step 3:** Install on the Redmi Note 8 Pro; capture launcher (label not truncated badly — if it is, stop and ask the user), splash (light + dark), welcome, settings, About — with the phone in English and in Spanish. Show captures to the user.
- [ ] **Step 4:** Update `COFARMER_FORK.md` (rebrand section + pending: contrast of white on `#00C389`, Play listing name change by user) and commit `docs: record CoFarmer Assistant rebrand`.
- [ ] **Step 5:** Push branch `feature/cofarmer-assistant-rebrand` only after the user confirms.
