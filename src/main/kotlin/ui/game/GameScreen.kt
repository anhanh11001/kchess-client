package ui.game

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import data.Player
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import common.TimeFormatter
import data.BoardRepresentation
import data.ChessPiece
import data.mock.MockPlayers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ui.chess.ChessBoard
import ui.users.Avatar

@Composable
fun GameScreen() {
    val blackPlayer = MockPlayers.MAGNUS_CARLSEN
    val whitePlayer = MockPlayers.FABIANO_CARUANA
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoardSection(
            boardMapping = BoardRepresentation.DEFAULT_BOARD_MAP,
            blackPlayer = blackPlayer,
            whitePlayer = whitePlayer,
            modifier = Modifier.padding(16.dp).fillMaxHeight()
        )
    }
}

@Composable
fun BoardSection(
    boardMapping: Map<String, ChessPiece>,
    blackPlayer: Player,
    whitePlayer: Player,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.weight(1f).padding(bottom = 8.dp)
        ) {
            PlayerBar(player = blackPlayer)
            Timer(
                startingTimeInSeconds = 600,
                isWhite = false
            )
        }

        ChessBoard(
            locationToChessPiece = boardMapping,
            modifier = Modifier.weight(16f).padding(bottom = 8.dp)
        )
        Row(modifier = Modifier.weight(1f)) {
            PlayerBar(player = whitePlayer)
            Timer(
                startingTimeInSeconds = 600,
                isWhite = true
            )
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun PlayerBar(
    player: Player,
    modifier: Modifier = Modifier
) {
    val formattedPlayerTitle = "${player.name} (${player.elo})"
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Avatar(
            imageUrl = player.imageUrl,
            modifier = Modifier.fillMaxHeight()
        )
        Text(
            text = formattedPlayerTitle,
            modifier = Modifier.offset(x = 8.dp),
            fontSize = 16.sp
        )
    }
}

@Composable
fun Timer(
    startingTimeInSeconds: Long,
    isWhite: Boolean,
    modifier: Modifier = Modifier
) {
    var timeInSeconds by remember { mutableStateOf(startingTimeInSeconds) }

    LaunchedEffect("timer") {
        flow {
            while (true) {
                delay(1000L)
                emit(Unit)
            }
        }.collect {
            timeInSeconds--
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(2.5f)
            .padding(8.dp)
            .background(if (isWhite) Color.White else Color.Black)
            .alpha(0.8f),
        contentAlignment = Alignment.CenterEnd
    ) {

        Text(
            text = TimeFormatter.formatTimeInMillisToReadableString(timeInSeconds),
            color = if (isWhite) Color.DarkGray else Color.LightGray,
            modifier = Modifier.offset(x = -8.dp)
        )
    }
}


@Preview
@Composable
fun MockPlayerBar() {
    PlayerBar(MockPlayers.MAGNUS_CARLSEN)
}