package epicarchitect.sample.ios

import androidx.compose.ui.window.ComposeUIViewController
import epicarchitect.sample.App

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    },
    content = {
        App()
    }
)
