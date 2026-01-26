package io.homeassistant.companion.android.settings.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.homeassistant.companion.android.common.compose.theme.HATheme

/**
 * Fragment hosting the About screen with CoFarmer and Enkitek branding.
 */
@AndroidEntryPoint
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HATheme {
                    AboutScreen(
                        onBackClick = {
                            parentFragmentManager.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
