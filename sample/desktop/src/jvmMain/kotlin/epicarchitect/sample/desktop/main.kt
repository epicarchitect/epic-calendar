package epicarchitect.sample.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import epicarchitect.sample.App

fun main() = application {
    Window(
        title = "Epic Calendar",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
