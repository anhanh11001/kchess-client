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
import data.Game
import data.GameStatus
import data.chess.BoardRepresentation
import data.chess.ChessMove
import data.chess.ChessPiece
import data.mock.MockPlayers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ui.chess.ChessBoard
import ui.components.KChessSmallRoundedCorner
import ui.components.convertToDp
import ui.users.Avatar
import java.lang.IllegalArgumentException

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameUIState = gameViewModel.gameUIStateFlow.collectAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        BoardSection(
            boardMapping = gameUIState.value.boardPosition,
            blackPlayer = gameUIState.value.blackPlayer,
            whitePlayer = gameUIState.value.whitePlayer,
            timeLimit = gameUIState.value.timeLimit,
            gameStatus = gameUIState.value.gameStatus,
            onNewMoveMade = {
                gameViewModel.onNextMove(it)
            },
            onBlackTimeEnded = {
                gameViewModel.onTimeEnded(false)
            },
            onWhiteTimeEnded = {
                gameViewModel.onTimeEnded(true)
            },
            modifier = modifier.padding(16.dp).fillMaxHeight()
        )
        GameStatisticsAndActionsSection(
            moveSequences = gameUIState.value.moveSequence,
            gameStatus = gameUIState.value.gameStatus,
            onStartGameSelected = {
                gameViewModel.startGame()
            },
            onNewGameSelected = {
                gameViewModel.onPlayNewGame()
            },
            onDrawSelected = {

            },
            onResignSelected = {
                gameViewModel.onPlayerResigned()
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
    moveSequences: List<ChessMove>,
    gameStatus: GameStatus,
    onStartGameSelected: () -> Unit,
    onNewGameSelected: () -> Unit,
    onDrawSelected: () -> Unit,
    onResignSelected: () -> Unit,
    onNextMoveSelected: () -> Unit,
    onPreviousMoveSelected: () -> Unit,
    onLastMoveSelected: () -> Unit,
    onFirstMoveSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        GameStatusBar(
            gameStatus = gameStatus,
            onStartGameSelected = onStartGameSelected,
            onNewGameSelected = onNewGameSelected,
        )
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
fun GameStatusBar(
    gameStatus: GameStatus,
    onStartGameSelected: () -> Unit,
    onNewGameSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (gameStatus) {
        GameStatus.BLACK_WIN, GameStatus.WHITE_WIN, GameStatus.DRAW, GameStatus.ABORTED -> GameResultsWithNewGameButton(
            gameStatus = gameStatus,
            onNewGameSelected = onNewGameSelected,
            modifier = modifier
        )
        GameStatus.NOT_STARTED -> {
            Row(modifier = modifier) {
                Button(
                    onClick = onStartGameSelected
                ) {
                    Text("Start Game")
                }
            }
        }
        GameStatus.WHITE_TURN -> Text("White turn!", modifier = modifier.padding(start = 8.dp))
        GameStatus.BLACK_TURN -> Text("Black turn!", modifier = modifier.padding(start = 8.dp))
    }
}

@Composable
fun GameResultsWithNewGameButton(
    gameStatus: GameStatus,
    onNewGameSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameStatusText = when (gameStatus) {
        GameStatus.ABORTED -> "The game is aborted."
        GameStatus.BLACK_WIN -> "BLACK wins the game."
        GameStatus.DRAW -> "WHITE and BLACK draw the game."
        GameStatus.WHITE_WIN -> "WHITE wins the game."
        else -> throw IllegalArgumentException("Invalid game status $gameStatus")
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(gameStatusText, modifier = Modifier.padding(end = 16.dp, start = 8.dp))
        Button(onNewGameSelected) {
            Text("New Game")
        }
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
    moveSequences: List<ChessMove>,
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
                firstMove = firstMove.description(),
                secondMove = secondMove?.description().orEmpty()
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
    timeLimit: Long,
    gameStatus: GameStatus,
    onWhiteTimeEnded: () -> Unit,
    onBlackTimeEnded: () -> Unit,
    onNewMoveMade: (ChessMove) -> Unit,
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
                startingTimeInSeconds = timeLimit,
                isWhite = false,
                gameStatus = gameStatus,
                onTimeCountFinished = onBlackTimeEnded
            )
        }

        ChessBoard(
            locationToChessPiece = boardMapping,
            modifier = Modifier
                .weight(16f)
                .padding(bottom = 8.dp, top = 8.dp)
                .onSizeChanged { widthLimit = it.width },
            onNewMoveMade = onNewMoveMade
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = convertToDp(widthLimit))
        ) {
            PlayerBar(player = whitePlayer, modifier = Modifier.weight(1f))
            Timer(
                startingTimeInSeconds = timeLimit,
                isWhite = true,
                gameStatus = gameStatus,
                onTimeCountFinished = onWhiteTimeEnded
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
    gameStatus: GameStatus,
    onTimeCountFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeInSeconds by remember { mutableStateOf(startingTimeInSeconds) }
    if (gameStatus == GameStatus.NOT_STARTED) {
        timeInSeconds = startingTimeInSeconds
    }
    var shouldCountDown by remember { mutableStateOf(false) }
    shouldCountDown = (isWhite && gameStatus == GameStatus.WHITE_TURN) ||
            (!isWhite && gameStatus == GameStatus.BLACK_TURN)

    LaunchedEffect("timer") {
        flow {
            while (true) {
                delay(1000L)
                emit(Unit)
            }
        }.collect {
            if (shouldCountDown) {
                timeInSeconds = (timeInSeconds - 1).coerceAtLeast(0)
                if (timeInSeconds <= 0) {
                    onTimeCountFinished()
                }
            }
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