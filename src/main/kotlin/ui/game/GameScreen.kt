package ui.game

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import data.Player
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import common.TimeFormatter
import data.chess.BoardRepresentation
import data.chess.ChessPiece
import data.mock.MockPlayers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ui.chess.ChessBoard
import ui.components.KChessSmallRoundedCorner
import ui.components.convertToDp
import ui.users.Avatar

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val blackPlayer = MockPlayers.MAGNUS_CARLSEN
    val whitePlayer = MockPlayers.FABIANO_CARUANA
    Row(verticalAlignment = Alignment.CenterVertically) {
        BoardSection(
            boardMapping = BoardRepresentation.DEFAULT_BOARD_MAP,
            blackPlayer = blackPlayer,
            whitePlayer = whitePlayer,
            modifier = modifier.padding(16.dp).fillMaxHeight()
        )
        GameStatisticsAndActionsSection(
            moveSequences = listOf("f1", "b1", "c2", "d3", "d4"),
            onDrawSelected = {

            },
            onResignSelected = {

            },
            onPreviousMoveSelected = {

            },
            onNextMoveSelected = {

            },
            onFirstMoveSelected = {

            },
            onLastMoveSelected = {

            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun GameStatisticsAndActionsSection(
    moveSequences: List<String>,
    onDrawSelected: () -> Unit,
    onResignSelected: () -> Unit,
    onNextMoveSelected: () -> Unit,
    onPreviousMoveSelected: () -> Unit,
    onLastMoveSelected: () -> Unit,
    onFirstMoveSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        GamePastMoveView(
            moveSequences,
            modifier = Modifier.weight(1f)
        )
        GameActionBar(
            onDrawSelected = onDrawSelected,
            onResignSelected = onResignSelected,
            onNextMoveSelected = onNextMoveSelected,
            onPreviousMoveSelected = onPreviousMoveSelected,
            onLastMoveSelected = onLastMoveSelected,
            onFirstMoveSelected = onFirstMoveSelected,
            modifier = Modifier.height(40.dp)
        )
    }
}

@Composable
fun GameActionBar(
    onDrawSelected: () -> Unit,
    onResignSelected: () -> Unit,
    onNextMoveSelected: () -> Unit,
    onPreviousMoveSelected: () -> Unit,
    onLastMoveSelected: () -> Unit,
    onFirstMoveSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(8.dp)) {
        GameActionButton(
            title = "Draw",
            iconPath = "icons/handshake.svg",
            onButtonClicked = onDrawSelected,
            modifier = Modifier.padding(end = 16.dp)
        )
        GameActionButton(
            title = "Resign",
            iconPath = "icons/white_flag.svg",
            onButtonClicked = onResignSelected,
            modifier = Modifier.padding(end = 16.dp)
        )
        Box(modifier = Modifier.weight(1f))
        GameControllerSection(
            onNextMoveSelected = onNextMoveSelected,
            onPreviousMoveSelected = onPreviousMoveSelected,
            onLastMoveSelected = onLastMoveSelected,
            onFirstMoveSelected = onFirstMoveSelected,
            modifier = Modifier.fillMaxHeight(1f)
        )
    }
}

@Composable
fun GamePastMoveView(
    moveSequences: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text("Game Moves", modifier = Modifier.padding(bottom = 2.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            thickness = 2.dp
        )
        var moveNumber = 1
        while ((moveNumber - 1) * 2 < moveSequences.size) {
            val firstMove = moveSequences[(moveNumber - 1) * 2]
            val secondMove = moveSequences.getOrNull((moveNumber - 1) * 2 + 1)
            MoveTile(
                moveNumber = moveNumber,
                firstMove = firstMove,
                secondMove = secondMove.orEmpty()
            )
            moveNumber++
        }
    }
}

@Composable
fun MoveTile(
    moveNumber: Int,
    firstMove: String,
    secondMove: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (moveNumber % 2 == 0) {
        Color.LightGray
    } else {
        Color.DarkGray
    }
    val textColor = if (moveNumber % 2 == 0) {
        Color.Black
    } else {
        Color.White
    }
    Row(modifier = modifier.background(backgroundColor)) {
        Text(
            moveNumber.toString(),
            color = textColor,
            modifier = Modifier.weight(1f).padding(2.dp)
        )
        Text(
            firstMove,
            color = textColor,
            modifier = Modifier.weight(1f).padding(2.dp)
        )
        Text(
            secondMove,
            color = textColor,
            modifier = Modifier.weight(1f).padding(2.dp)
        )
        Box(modifier = Modifier.weight(6f))
    }
}

@Composable
fun GameControllerSection(
    onNextMoveSelected: () -> Unit,
    onPreviousMoveSelected: () -> Unit,
    onLastMoveSelected: () -> Unit,
    onFirstMoveSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth()
    ) {
        IconButton(
            onClick = onFirstMoveSelected,
            modifier = Modifier.aspectRatio(1f).padding(2.dp).offset(x = (-8).dp)
        ) {
            Icon(
                painter = painterResource("icons/arrow-first.svg"),
                contentDescription = "First Move",
                tint = Color.DarkGray
            )
        }
        IconButton(
            onClick = onPreviousMoveSelected,
            modifier = Modifier.aspectRatio(1f).padding(2.dp).offset(x = (-8).dp)
        ) {
            Icon(
                painter = painterResource("icons/arrow-previous.svg"),
                contentDescription = "Previous Move",
                tint = Color.DarkGray
            )
        }
        IconButton(
            onClick = onNextMoveSelected,
            modifier = Modifier.aspectRatio(1f).padding(2.dp).offset(x = (-8).dp)
        ) {
            Icon(
                painter = painterResource("icons/arrow-next.svg"),
                contentDescription = "Previous Move",
                tint = Color.DarkGray
            )
        }
        IconButton(
            onClick = onLastMoveSelected,
            modifier = Modifier.aspectRatio(1f).padding(2.dp)
        ) {
            Icon(
                painter = painterResource("icons/arrow-last.svg"),
                contentDescription = "Last Move",
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun GameActionButton(
    title: String,
    iconPath: String,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onButtonClicked,
        modifier = modifier.fillMaxHeight()
    ) {
        Row {
            Icon(
                painter = painterResource(iconPath),
                contentDescription = title,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(end = 4.dp)
            )
            Text(title)
        }
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
                .widthIn(max = convertToDp(widthLimit))
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
                .widthIn(max = convertToDp(widthLimit))
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