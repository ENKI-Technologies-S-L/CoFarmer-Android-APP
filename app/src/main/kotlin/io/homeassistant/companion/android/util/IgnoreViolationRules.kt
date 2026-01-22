package io.homeassistant.companion.android.util

import android.os.Build
import android.os.strictmode.DiskReadViolation
import android.os.strictmode.DiskWriteViolation
import android.os.strictmode.IncorrectContextUseViolation
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import io.homeassistant.companion.android.common.util.IgnoreViolationRule

val vmPolicyIgnoredViolationRules = listOf(
    IgnoreChromiumTrichomeWrongContextUsage,
    IgnoreBarcodeScannerRotationListenerWrongContextUsage,
)

val threadPolicyIgnoredViolationRules = listOf(
    IgnoreChangelogDiskRead,
    IgnoreNotificationHistoryFragmentLoadSharedPrefDiskRead,
    IgnoreComposeTextContextMenuDiskRead,
    IgnoreActivityThreadVsyncDiskReadWrite,
    // Samsung
    IgnoreSamsungInputRuneDiskRead,
    IgnoreSamsungKnoxProKioskDiskRead,
    // Android Auto
    IgnoreAndroidAutoServiceConnectionDiskRead,
    IgnoreAndroidAutoRendererServiceDiskRead,
    // Xiaomi/MIUI/HyperOS/Redmi/POCO
    IgnoreMiuiFontSettingsDiskRead,
    IgnoreMiuiTurboSchedMonitorDiskRead,
    IgnoreMiuiInternalsDiskRead,
    IgnoreXiaomiComponentsDiskRead,
    IgnoreHyperOSDiskRead,
    IgnoreRedmiDiskRead,
    IgnorePocoDiskRead,
    IgnoreMiuiInputDiskRead,
    // Motorola/MediaTek
    IgnoreMediaTekBoostFwkDiskRead,
    // Oppo/Realme/OnePlus (BBK Electronics)
    IgnoreOppoDiskRead,
    IgnoreOnePlusDiskRead,
    // Vivo
    IgnoreVivoDiskRead,
    // Huawei/Honor
    IgnoreHuaweiDiskRead,
    IgnoreHonorDiskRead,
    // Transsion (Tecno/Infinix/Itel)
    IgnoreTranssionDiskRead,
    // Other OEMs
    IgnoreAsusRogDiskRead,
    IgnoreSonyDiskRead,
    IgnoreLgDiskRead,
    IgnoreZteDiskRead,
    IgnoreLenovoDiskRead,
    IgnoreMeizuDiskRead,
    IgnoreNothingDiskRead,
    // Japan market
    IgnoreSharpAquosDiskRead,
    IgnoreKyoceraDiskRead,
    IgnoreFujitsuDiskRead,
    IgnoreRakutenDiskRead,
    // Europe market
    IgnoreNokiaHmdDiskRead,
    IgnoreFairphoneDiskRead,
    // Americas/Global budget
    IgnoreTclAlcatelDiskRead,
    IgnoreBluDiskRead,
    // Rugged phones (Agriculture!)
    IgnoreCatPhonesDiskRead,
    // Qualcomm (chipset-level, all regions)
    IgnoreQualcommDiskRead,
)

/**
 * Ignore an [IncorrectContextUseViolation] that can occur
 * in the Chromium WebView client (specifically involving `chromium-TrichromeWebViewGoogle`).
 *
 * This issue typically arises when the application context is incorrectly used during
 * configuration changes (e.g., screen rotation) within the WebView's internal mechanisms.
 *
 * It doesn't seem to be tracked anywhere.
 */
private data object IgnoreChromiumTrichomeWrongContextUsage : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is IncorrectContextUseViolation) return false

        return violation.stackTrace.any {
            it.fileName?.startsWith("chromium-TrichromeWebViewGoogle") == true &&
                it.methodName == "onConfigurationChanged"
        }
    }
}

/**
 * Ignores an IncorrectContextUseViolation specifically caused by the
 * com.journeyapps.barcodescanner.RotationListener using the application context
 * to get the WindowManager, which is incorrect for UI operations.
 *
 * This is a known issue in the zxing-android-embedded library.
 * See:
 * - https://github.com/journeyapps/zxing-android-embedded/issues/762
 * - https://github.com/journeyapps/zxing-android-embedded/blob/d09b7c76c3124fbfbd096a65d60b1997f37ff90f/zxing-android-embedded/src/com/journeyapps/barcodescanner/RotationListener.java#L31
 */
private data object IgnoreBarcodeScannerRotationListenerWrongContextUsage : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is IncorrectContextUseViolation) return false

        return violation.stackTrace.any {
            it.className == "com.journeyapps.barcodescanner.RotationListener" &&
                it.methodName == "listen"
        }
    }
}

/**
 * Ignore a DiskReadViolation inside https://github.com/AppDevNext/ChangeLog while
 * loading default shared preferences.
 */
private data object IgnoreChangelogDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false
        return violation.stackTrace.any {
            it.className == "info.hannes.changelog.ChangeLog"
        }
    }
}

/**
 * Ignore a DiskReadViolation inside [NotificationHistoryFragment] while loading the XML that contains the
 * preferences used to make the UI of the screen. See the class for more details.
 */
private data object IgnoreNotificationHistoryFragmentLoadSharedPrefDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false
        return violation.stackTrace.any {
            it.className == "io.homeassistant.companion.android.settings.notification.NotificationHistoryFragment" &&
                it.methodName == "onCreatePreferences"
        }
    }
}

/**
 * Ignore a DiskReadViolation in Jetpack Compose's text selection context menu implementation.
 * This occurs when using SelectionContainer which enables text selection and shows a context menu.
 */
private data object IgnoreComposeTextContextMenuDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false
        return violation.stackTrace.any {
            it.className ==
                "androidx.compose.foundation.text.contextmenu.internal.AndroidTextContextMenuToolbarProvider"
        }
    }
}

/**
 * Ignore an [DiskWriteViolation] and [DiskReadViolation] in Android's ActivityThread vsync scheduling.
 * This occurs in the framework's internal vsync scheduling mechanism and is beyond
 * application control.
 */
private data object IgnoreActivityThreadVsyncDiskReadWrite : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskWriteViolation && violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "android.app.ActivityThread" &&
                it.methodName == "scheduleVsyncSS"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Samsung's InputRune framework component.
 * This occurs in Samsung's internal input configuration system and is beyond
 * application control.
 */
private data object IgnoreSamsungInputRuneDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "com.samsung.android.rune.InputRune"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Samsung Knox's ProKioskManager.
 * This occurs when Samsung Knox checks the kiosk state and is beyond application control.
 */
private data object IgnoreSamsungKnoxProKioskDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "com.samsung.android.knox.custom.ProKioskManager" &&
                it.methodName == "getProKioskState"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Android Auto/Automotive's ServiceConnectionManager.
 * This occurs when the Android Auto library initializes its service connection and is
 * beyond application control.
 */
private data object IgnoreAndroidAutoServiceConnectionDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "androidx.car.app.activity.ServiceConnectionManager" &&
                it.methodName == "initializeService"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Android Auto/Automotive's IRendererService.
 * This occurs when the Android Auto renderer service handles binder transactions and is
 * beyond application control.
 */
private data object IgnoreAndroidAutoRendererServiceDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "androidx.car.app.activity.renderer.IRendererService\$Stub" &&
                it.methodName == "onTransact"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in MIUI's FontSettings component.
 * This occurs when MIUI ROM checks for custom theme fonts during Activity creation
 * and is beyond application control.
 */
private data object IgnoreMiuiFontSettingsDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "miui.util.font.FontSettings"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in MIUI's TurboSchedMonitor component.
 * This occurs when MIUI's performance scheduler checks file availability during
 * Choreographer frame rendering and is beyond application control.
 */
private data object IgnoreMiuiTurboSchedMonitorDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className == "android.os.TurboSchedMonitorImpl"
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in MediaTek BoostFwk (used by Motorola and other OEMs).
 * This occurs when MediaTek's performance framework checks if an app is a game during
 * touch/scroll events to optimize performance. This is beyond application control.
 *
 * Stack trace typically includes:
 * - com.mediatek.boostfwk.utils.Util.isGameApp
 * - com.mediatek.boostfwk.identify.scroll.ScrollIdentify.checkAppType
 * - com.motorola.perf.MTKBoostFwkAdapter.scrollScenarioCallBySbe
 */
private data object IgnoreMediaTekBoostFwkDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.mediatek.boostfwk.") ||
                it.className.startsWith("com.motorola.perf.")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in MIUI's internal components.
 * This covers various MIUI ROM subsystems that perform disk reads during UI interactions,
 * including input handling, gesture detection, and system services.
 * This is beyond application control.
 *
 * Covers namespaces:
 * - miui.* (core MIUI utilities)
 * - com.miui.* (MIUI apps and services)
 * - android.miui.* (MIUI Android framework extensions)
 * - AppScout* (MIUI app monitoring/optimization)
 */
private data object IgnoreMiuiInternalsDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.miui.") ||
                it.className.startsWith("miui.") ||
                it.className.startsWith("android.miui.") ||
                it.className.contains("AppScout") ||
                it.className.contains("ScoutStateMachine")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Xiaomi-specific components.
 * This covers Xiaomi's performance optimization, input handling, and scroll detection
 * systems that may trigger disk reads during touch events.
 * This is beyond application control.
 *
 * Covers:
 * - com.xiaomi.* (Xiaomi services and utilities)
 * - MiuiInput*, MiuiScroll*, XiaomiScroll* (input-related classes)
 * - com.miui.perf.* (MIUI performance framework)
 * - android.app.Xiaomi* (Xiaomi Android extensions)
 * - android.content.Xiaomi* (Xiaomi content provider extensions)
 */
private data object IgnoreXiaomiComponentsDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.xiaomi.") ||
                it.className.startsWith("com.miui.perf.") ||
                it.className.startsWith("android.app.Xiaomi") ||
                it.className.startsWith("android.content.Xiaomi") ||
                it.className.contains("MiuiInput") ||
                it.className.contains("MiuiScroll") ||
                it.className.contains("XiaomiScroll") ||
                it.className.contains("MiuiBoost") ||
                it.className.contains("MiuiPerf") ||
                it.className.contains("MiuiWindow") ||
                it.className.contains("MiuiActivity") ||
                it.className.contains("Xiaomi")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in HyperOS components (Xiaomi's new OS replacing MIUI).
 * HyperOS was introduced in late 2023 and uses new namespaces while maintaining
 * some MIUI compatibility layers. This is beyond application control.
 *
 * Covers:
 * - com.hyperos.* (new HyperOS namespace)
 * - com.mi.* (Mi services)
 * - android.os.MiuiProcess* (MIUI process management)
 */
private data object IgnoreHyperOSDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.hyperos.") ||
                it.className.startsWith("com.mi.") ||
                it.className.startsWith("android.os.MiuiProcess") ||
                it.className.contains("HyperOS") ||
                it.className.contains("MiuiFreeform")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Redmi-specific components.
 * Redmi is a Xiaomi sub-brand using MIUI/HyperOS with some specific customizations.
 * This is beyond application control.
 *
 * Covers:
 * - com.redmi.* (Redmi services)
 * - com.miui.home.launcher.Redmi* (Redmi launcher components)
 */
private data object IgnoreRedmiDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.redmi.") ||
                it.className.contains("Redmi") ||
                it.className.contains("redmi")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in POCO-specific components.
 * POCO is a Xiaomi sub-brand (formerly Pocophone) using MIUI/HyperOS.
 * This is beyond application control.
 *
 * Covers:
 * - com.poco.* (POCO services)
 * - com.miui.home.launcher.Poco* (POCO launcher components)
 */
private data object IgnorePocoDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.poco.") ||
                it.className.contains("Poco") ||
                it.className.contains("poco")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in MIUI Input handling components.
 * This occurs on Xiaomi/Redmi/POCO devices during touch/input event processing.
 * These are system-level input handlers that perform disk reads to check
 * gesture configurations, app-specific settings, etc. Beyond application control.
 *
 * Covers:
 * - android.view.MiuiInput* (MIUI input extensions)
 * - android.view.InputEvent* on MIUI (modified input stack)
 * - MotionEvent handling on MIUI devices
 */
private data object IgnoreMiuiInputDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("android.view.Miui") ||
                it.className.contains("MiuiMotion") ||
                it.className.contains("MiuiTouch") ||
                it.className.contains("MiInputMethod") ||
                // Catch generic input violations on MIUI
                (it.className.startsWith("android.view.") &&
                    (it.methodName?.contains("dispatch") == true ||
                        it.methodName?.contains("onTouch") == true))
        }
    }
}

// ==================== BBK Electronics (Oppo/Realme/OnePlus/Vivo) ====================

/**
 * Ignore a [DiskReadViolation] in Oppo/Realme's ColorOS components.
 * ColorOS is Oppo's custom Android skin, also used by Realme with minor modifications.
 * This is beyond application control.
 *
 * Covers:
 * - com.oppo.* (Oppo services)
 * - com.coloros.* (ColorOS system)
 * - com.oplus.* (Unified Oppo/OnePlus/Realme namespace since 2021)
 * - com.realme.* (Realme-specific services)
 */
private data object IgnoreOppoDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.oppo.") ||
                it.className.startsWith("com.coloros.") ||
                it.className.startsWith("com.oplus.") ||
                it.className.startsWith("com.realme.") ||
                it.className.startsWith("com.heytap.") ||
                it.className.contains("OppoPerf") ||
                it.className.contains("ColorOS")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in OnePlus's OxygenOS/ColorOS components.
 * OnePlus merged with Oppo in 2021, sharing the oplus namespace.
 * This is beyond application control.
 *
 * Covers:
 * - com.oneplus.* (OnePlus services)
 * - net.oneplus.* (OnePlus network services)
 * - com.oplus.* (Shared with Oppo)
 */
private data object IgnoreOnePlusDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.oneplus.") ||
                it.className.startsWith("net.oneplus.") ||
                it.className.contains("OnePlusPerf") ||
                it.className.contains("OxygenOS")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Vivo's FuntouchOS/OriginOS components.
 * Vivo uses FuntouchOS internationally and OriginOS in China.
 * This is beyond application control.
 *
 * Covers:
 * - com.vivo.* (Vivo services)
 * - com.bbk.* (BBK Electronics parent company)
 * - com.funtouch.* (FuntouchOS)
 * - com.iqoo.* (iQOO sub-brand)
 */
private data object IgnoreVivoDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.vivo.") ||
                it.className.startsWith("com.bbk.") ||
                it.className.startsWith("com.funtouch.") ||
                it.className.startsWith("com.iqoo.") ||
                it.className.contains("VivoPerf") ||
                it.className.contains("OriginOS")
        }
    }
}

// ==================== Huawei/Honor ====================

/**
 * Ignore a [DiskReadViolation] in Huawei's EMUI/HarmonyOS components.
 * Huawei uses EMUI (based on Android) and HarmonyOS (their own OS with Android compatibility).
 * This is beyond application control.
 *
 * Covers:
 * - com.huawei.* (Huawei services)
 * - com.hisilicon.* (HiSilicon chipset - Huawei's chip division)
 * - android.hwcontrol.* (Huawei control framework)
 */
private data object IgnoreHuaweiDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.huawei.") ||
                it.className.startsWith("com.hisilicon.") ||
                it.className.startsWith("android.hwcontrol.") ||
                it.className.contains("HwPerf") ||
                it.className.contains("EMUI") ||
                it.className.contains("HarmonyOS")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Honor's MagicOS components.
 * Honor split from Huawei in 2020 and developed MagicOS (formerly Magic UI).
 * This is beyond application control.
 *
 * Covers:
 * - com.honor.* (Honor services)
 * - com.hihonor.* (Honor international namespace)
 */
private data object IgnoreHonorDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.honor.") ||
                it.className.startsWith("com.hihonor.") ||
                it.className.contains("MagicOS") ||
                it.className.contains("HonorPerf")
        }
    }
}

// ==================== Transsion Holdings (Tecno/Infinix/Itel) ====================

/**
 * Ignore a [DiskReadViolation] in Transsion's HiOS/XOS components.
 * Transsion owns Tecno (HiOS), Infinix (XOS), and Itel - popular in Africa/Asia.
 * This is beyond application control.
 *
 * Covers:
 * - com.transsion.* (Parent company)
 * - com.tecno.* (Tecno brand)
 * - com.infinix.* (Infinix brand)
 * - com.itel.* (Itel brand)
 */
private data object IgnoreTranssionDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.transsion.") ||
                it.className.startsWith("com.tecno.") ||
                it.className.startsWith("com.infinix.") ||
                it.className.startsWith("com.itel.") ||
                it.className.contains("HiOS") ||
                it.className.contains("XOS")
        }
    }
}

// ==================== Other OEMs ====================

/**
 * Ignore a [DiskReadViolation] in Asus ROG Phone components.
 * Asus ROG phones have gaming-specific performance optimizations.
 * This is beyond application control.
 */
private data object IgnoreAsusRogDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.asus.") ||
                it.className.contains("AsusPerf") ||
                it.className.contains("ROGPhone")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Sony Xperia components.
 * This is beyond application control.
 */
private data object IgnoreSonyDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.sonymobile.") ||
                it.className.startsWith("com.sony.")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in LG components (legacy devices still in use).
 * LG exited the smartphone market in 2021 but devices are still in circulation.
 * This is beyond application control.
 */
private data object IgnoreLgDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.lge.") ||
                it.className.contains("LGPerf")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in ZTE/Nubia components.
 * ZTE and its gaming sub-brand Nubia have custom Android modifications.
 * This is beyond application control.
 */
private data object IgnoreZteDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.zte.") ||
                it.className.startsWith("cn.nubia.") ||
                it.className.startsWith("com.nubia.")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Lenovo components.
 * Lenovo makes tablets and phones with custom Android modifications.
 * This is beyond application control.
 */
private data object IgnoreLenovoDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.lenovo.")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Meizu's Flyme OS components.
 * Meizu uses Flyme OS, popular in China.
 * This is beyond application control.
 */
private data object IgnoreMeizuDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.meizu.") ||
                it.className.startsWith("com.flyme.") ||
                it.className.contains("FlymeOS")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Nothing Phone components.
 * Nothing Phone uses Nothing OS (near-stock with Glyph interface).
 * This is beyond application control.
 */
private data object IgnoreNothingDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.nothing.") ||
                it.className.contains("NothingOS") ||
                it.className.contains("Glyph")
        }
    }
}

// ==================== Japan Market ====================

/**
 * Ignore a [DiskReadViolation] in Sharp AQUOS components.
 * Sharp is one of the most popular Android brands in Japan (~15% market share).
 * AQUOS phones have custom software optimizations for their IGZO displays.
 * This is beyond application control.
 */
private data object IgnoreSharpAquosDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("jp.co.sharp.") ||
                it.className.startsWith("com.sharp.") ||
                it.className.contains("AQUOS") ||
                it.className.contains("SharpPerf")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Kyocera components.
 * Kyocera makes rugged phones popular in Japan and US carrier markets.
 * Known for TORQUE and DuraForce series.
 * This is beyond application control.
 */
private data object IgnoreKyoceraDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.kyocera.") ||
                it.className.startsWith("jp.kyocera.") ||
                it.className.contains("TORQUE") ||
                it.className.contains("DuraForce")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Fujitsu components.
 * Fujitsu makes the "arrows" phone series, popular in Japan.
 * This is beyond application control.
 */
private data object IgnoreFujitsuDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.fujitsu.") ||
                it.className.startsWith("jp.co.fujitsu.") ||
                it.className.contains("FujitsuPerf")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Rakuten Mini/Hand components.
 * Rakuten Mobile makes compact phones for the Japanese market.
 * This is beyond application control.
 */
private data object IgnoreRakutenDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("jp.co.rakuten.") ||
                it.className.startsWith("com.rakuten.")
        }
    }
}

// ==================== Europe Market ====================

/**
 * Ignore a [DiskReadViolation] in Nokia/HMD Global components.
 * Nokia phones run near-stock Android but HMD adds some optimizations.
 * Popular in Europe and emerging markets.
 * This is beyond application control.
 */
private data object IgnoreNokiaHmdDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.hmd.") ||
                it.className.startsWith("com.nokia.") ||
                it.className.contains("HMDGlobal")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in Fairphone components.
 * Fairphone makes sustainable/repairable phones, popular in Europe.
 * Uses near-stock Android with some custom eco-features.
 * This is beyond application control.
 */
private data object IgnoreFairphoneDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.fairphone.")
        }
    }
}

// ==================== Americas/Global Budget ====================

/**
 * Ignore a [DiskReadViolation] in TCL/Alcatel components.
 * TCL owns Alcatel, BlackBerry Mobile, and makes budget phones.
 * Popular in Latin America, Europe, and US prepaid market.
 * This is beyond application control.
 */
private data object IgnoreTclAlcatelDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.tcl.") ||
                it.className.startsWith("com.alcatel.") ||
                it.className.startsWith("com.tct.") ||
                it.className.contains("TCLPerf")
        }
    }
}

/**
 * Ignore a [DiskReadViolation] in BLU Products components.
 * BLU makes budget Android phones popular in USA and Latin America.
 * This is beyond application control.
 */
private data object IgnoreBluDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.blu.")
        }
    }
}

// ==================== Rugged/Industrial (Agriculture!) ====================

/**
 * Ignore a [DiskReadViolation] in CAT Phones (Caterpillar) components.
 * CAT makes rugged phones designed for outdoor/industrial use.
 * Very relevant for agricultural applications!
 * This is beyond application control.
 */
private data object IgnoreCatPhonesDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.caterpillar.") ||
                it.className.startsWith("com.cat.") ||
                it.className.startsWith("com.bullitt.") // Bullitt Group makes CAT phones
        }
    }
}

// ==================== Chipset-level (All Regions) ====================

/**
 * Ignore a [DiskReadViolation] in Qualcomm Snapdragon components.
 * Qualcomm provides chipsets and performance frameworks used by many OEMs.
 * Their boost/performance libraries can trigger disk reads during touch events.
 * This is beyond application control.
 */
private data object IgnoreQualcommDiskRead : IgnoreViolationRule {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun shouldIgnore(violation: Violation): Boolean {
        if (violation !is DiskReadViolation) return false

        return violation.stackTrace.any {
            it.className.startsWith("com.qualcomm.") ||
                it.className.startsWith("com.qti.") ||
                it.className.contains("QPerf") ||
                it.className.contains("Snapdragon")
        }
    }
}
