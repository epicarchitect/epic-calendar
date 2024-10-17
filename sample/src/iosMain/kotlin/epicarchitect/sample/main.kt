package epicarchitect.sample

import androidx.compose.ui.window.ComposeUIViewController

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    },
    content = {
        App()
    }
)