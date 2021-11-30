package ui.game

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import data.Player
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import common.TimeFormatter
import data.BoardRepresentation
import data.ChessPiece
import data.mock.MockPlayers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ui.chess.ChessBoard
import ui.components.KChessSmallRoundedCorner
import ui.users.Avatar

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    val blackPlayer = MockPlayers.MAGNUS_CARLSEN
    val whitePlayer = MockPlayers.FABIANO_CARUANA
    Row(verticalAlignment = Alignment.CenterVertically) {
        BoardSection(
            boardMapping = BoardRepresentation.DEFAULT_BOARD_MAP,
            blackPlayer = blackPlayer,
            whitePlayer = whitePlayer,
            modifier = modifier.padding(16.dp).fillMaxHeight()
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
    var widthLimit by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = with(LocalDensity.current) { widthLimit.toDp() })
        ) {
            PlayerBar(player = blackPlayer, modifier = Modifier.weight(1f))
            Timer(
                startingTimeInSeconds = 600,
                isWhite = false
            )
        }

        ChessBoard(
            locationToChessPiece = boardMapping,
            modifier = Modifier
                .weight(16f)
                .padding(bottom = 8.dp, top = 8.dp)
                .onSizeChanged { widthLimit = it.width }
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = with(LocalDensity.current) { widthLimit.toDp() })
        ) {
            PlayerBar(player = whitePlayer, modifier = Modifier.weight(1f))
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
            modifier = Modifier.fillMaxHeight().clip(KChessSmallRoundedCorner())
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
            timeInSeconds = (timeInSeconds - 1).coerceAtLeast(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(2.5f)
            .clip(KChessSmallRoundedCorner())
            .background(if (isWhite) Color.White else Color.Black)
            .alpha(0.8f),
        contentAlignment = Alignment.CenterEnd
    ) {

        Text(
            text = TimeFormatter.formatTimeInMillisToReadableString(timeInSeconds),
            color = if (isWhite) Color.DarkGray else Color.LightGray,
            modifier = Modifier.offset(x = (-8).dp)
        )
    }
}


@Preview
@Composable
fun MockPlayerBar() {
    PlayerBar(MockPlayers.MAGNUS_CARLSEN)
}