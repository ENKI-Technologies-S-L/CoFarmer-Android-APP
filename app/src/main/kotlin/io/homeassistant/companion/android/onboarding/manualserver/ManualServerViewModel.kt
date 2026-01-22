package io.homeassistant.companion.android.onboarding.manualserver

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
internal class ManualServerViewModel @Inject constructor() : ViewModel() {
    private val serverUrlMutableFlow = MutableStateFlow("")
    val serverUrlFlow = serverUrlMutableFlow.asStateFlow()

    private val isServerUrlValidMutableFlow = MutableStateFlow(false)
    val isServerUrlValidFlow = isServerUrlValidMutableFlow.asStateFlow()

    fun onServerUrlChange(url: String) {
        serverUrlMutableFlow.update { url }
        validateServerUrl(url)
    }

    /**
     * Returns the normalized URL with https:// scheme.
     * If the user enters a URL without scheme, https:// is prepended.
     */
    fun getNormalizedUrl(): String {
        val url = serverUrlMutableFlow.value.trim()
        return normalizeUrl(url)
    }

    private fun validateServerUrl(url: String) {
        val normalizedUrl = normalizeUrl(url.trim())
        isServerUrlValidMutableFlow.update {
            normalizedUrl.isNotEmpty() &&
                URLUtil.isValidUrl(normalizedUrl) &&
                runCatching { URL(normalizedUrl) }.isSuccess
        }
    }

    /**
     * Normalizes the URL by adding https:// if no scheme is present.
     * - If URL starts with http:// or https://, it's used as-is
     * - Otherwise, https:// is prepended
     */
    private fun normalizeUrl(url: String): String {
        if (url.isEmpty()) return ""
        return when {
            url.startsWith("https://") || url.startsWith("http://") -> url
            else -> "https://$url"
        }
    }
}
