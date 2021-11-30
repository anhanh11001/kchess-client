package ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainScreen(
    onNewGameClicked: () -> Unit,
    onFindRoomClicked: () -> Unit,
    onExitClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(32.dp)
    ) {
        Image(
            painter = painterResource("icons/chess_game_icon.svg"),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(0.4f)
        )

        GameIcon(modifier = Modifier.padding(bottom = 32.dp))
        MainScreenButton(
            title = "New Game",
            onClick = onNewGameClicked
        )

        MainScreenButton(
            title = "Find Room",
            onClick = onFindRoomClicked
        )

        MainScreenButton(
            title = "Exit",
            onClick = onExitClicked
        )
    }
}

@Composable
fun MainScreenButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(8.dp)
    ) {
        Text(title)
    }
}

@Composable
fun GameIcon(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun MainScreenButtonPreview() {
    MainScreenButton(
        title = "Hello World",
        onClick = {}
    )
}