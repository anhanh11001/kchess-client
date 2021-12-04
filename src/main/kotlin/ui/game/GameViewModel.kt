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
    val blackPlayer: Player,
    val whitePlayer: Player,
    val boardPosition: Map<String, ChessPiece>,
    val gameId: Long,
    val moveSequence: List<ChessMove> = emptyList(),
    val gameStatus: GameStatus,
    val timeLimit: Long
)

class GameViewModel(
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
    private val getGameByGameIdUseCase: GetGameByGameIdUseCase,
    private val determineNextMoveUseCase: DetermineNextMoveUseCase,
    private val determineValidMoveUseCase: DetermineValidMoveUseCase
) {

    val gameUIStateFlow = MutableStateFlow(
        GameUIState(
            gameId = 1L,
            blackPlayer = MockPlayers.FABIANO_CARUANA,
            whitePlayer = MockPlayers.MAGNUS_CARLSEN,
            moveSequence = emptyList(),
            gameStatus = GameStatus.NOT_STARTED,
            timeLimit = 600L,
            boardPosition = BoardRepresentation.DEFAULT_BOARD_MAP
        )
    )

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