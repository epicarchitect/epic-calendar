import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Epic calendar sample",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}