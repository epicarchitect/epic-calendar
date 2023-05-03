import androidx.compose.ui.window.ComposeUIViewController

actual fun getPlatformName(): String = "iOS"

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController { App() }