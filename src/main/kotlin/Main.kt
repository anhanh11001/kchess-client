import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.push
import com.sun.tools.javac.Main
import di.setupDependencyInjection
import navigation.NavScreen
import navigation.rememberRouter
import ui.game.GameScreen
import ui.home.MainScreen
import ui.verification.LoginScreen

const val PREVIEWING = true

@Composable
@Preview
fun App(onExitApplication: () -> Unit) {
    if (PREVIEWING) {
        PreviewSpace()
        return
    }
    val router = rememberRouter<NavScreen>(initialConfiguration = { NavScreen.Main })

    Children(routerState = router.state) { screen ->
        when (val configuration = screen.configuration) {
            is NavScreen.GameScreen -> GameScreen()
            is NavScreen.Main -> MainScreen(
                onNewGameClicked = {
                    router.push(NavScreen.GameScreen(gameId = 100L))
                },
                onFindRoomClicked = {

                },
                onExitClicked = onExitApplication
            )
        }
    }
}

fun main() = application {
    // Application Setup
    setupDependencyInjection()

    // Entry point to UI
    Window(
        onCloseRequest = ::exitApplication,
        title = "Play Chess and Have Fun"
    ) {
        App(::exitApplication)
    }
}

@Composable
fun PreviewSpace() {
//    var text by remember { mutableStateOf("Hello, World!") }

//    DesktopMaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
    LoginScreen()
}
