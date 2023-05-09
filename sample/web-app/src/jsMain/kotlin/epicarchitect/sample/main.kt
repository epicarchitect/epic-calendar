package epicarchitect.sample

import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("Epic Calendar") {
            App()
        }
    }
}
