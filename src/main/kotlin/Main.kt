import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.setupDependencyInjection
import ui.game.GameScreen

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

//    DesktopMaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
    GameScreen()
}

fun main() = application {
    // Application Setup
    setupDependencyInjection()

    // Entry point to UI
    Window(
        onCloseRequest = ::exitApplication,
        title = "Play Chess and Have Fun"
    ) {
        App()
    }
}
