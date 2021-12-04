package ui.game

import data.*
import data.chess.BoardRepresentation
import data.chess.ChessMove
import data.chess.ChessPiece
import data.mock.MockPlayers
import domain.DetermineNextMoveUseCase
import domain.DetermineValidMoveUseCase
import domain.GetGameByGameIdUseCase
import domain.GetPlayerByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow

data class GameUIState(
    val gameId: Long = 1L,
    val blackPlayer: Player = MockPlayers.FABIANO_CARUANA,
    val whitePlayer: Player = MockPlayers.MAGNUS_CARLSEN,
    val boardPosition: Map<String, ChessPiece> = BoardRepresentation.DEFAULT_BOARD_MAP,
    val moveSequence: List<ChessMove> = emptyList(),
    val gameStatus: GameStatus = GameStatus.NOT_STARTED,
    val timeLimit: Long = 600L
)

class GameViewModel(
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
    private val getGameByGameIdUseCase: GetGameByGameIdUseCase,
    private val determineNextMoveUseCase: DetermineNextMoveUseCase,
    private val determineValidMoveUseCase: DetermineValidMoveUseCase
) {

    val gameUIStateFlow = MutableStateFlow(GameUIState())

    fun onPlayNewGame() {
        gameUIStateFlow.value = GameUIState()
    }

    fun onPlayerResigned() {
        val currentGameState = gameUIStateFlow.value
        if (currentGameState.gameStatus == GameStatus.WHITE_TURN) {
            gameUIStateFlow.value = currentGameState.copy(
                gameStatus = GameStatus.BLACK_WIN
            )
        } else if (currentGameState.gameStatus == GameStatus.BLACK_TURN) {
            gameUIStateFlow.value = currentGameState.copy(
                gameStatus = GameStatus.WHITE_WIN
            )
        }
    }

    fun startGame() {
        val currentGameState = gameUIStateFlow.value
        if (currentGameState.gameStatus == GameStatus.NOT_STARTED) {
            gameUIStateFlow.value = currentGameState.copy(gameStatus = GameStatus.WHITE_TURN)
        }
    }

    fun onTimeEnded(isWhite: Boolean) {
        val currentGameState = gameUIStateFlow.value

        val gameStatus = if (isWhite) {
            GameStatus.BLACK_WIN
        } else {
            GameStatus.WHITE_WIN
        }
        gameUIStateFlow.value = currentGameState.copy(gameStatus = gameStatus)
    }

    fun onNextMove(chessMove: ChessMove) {
        val currentGameState = gameUIStateFlow.value
        val isValidMove = determineValidMoveUseCase(
            gameStatus = currentGameState.gameStatus,
            boardPosition = currentGameState.boardPosition,
            chessMove = chessMove
        )
        if (isValidMove) {
            val newBoardPosition = currentGameState.boardPosition.toMutableMap()
            newBoardPosition.remove(chessMove.startingPosition)
            newBoardPosition[chessMove.endingPosition] = chessMove.chessPiece

            val gameStatus = if (chessMove.chessPiece.isWhite) {
                GameStatus.BLACK_TURN
            } else {
                GameStatus.WHITE_TURN
            }
            val updatedMoveSequence = currentGameState.moveSequence.toMutableList()
            updatedMoveSequence.add(chessMove)

            gameUIStateFlow.value = currentGameState.copy(
                boardPosition = newBoardPosition,
                gameStatus = gameStatus,
                moveSequence = updatedMoveSequence
            )
        }
    }
}