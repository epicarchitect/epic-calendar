package epicarchitect.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Epic Calendar",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}