package ui.game

import data.*
import data.chess.BoardRepresentation
import data.chess.ChessMove
import data.chess.ChessPiece
import data.mock.MockPlayers
import domain.*
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
    private val updateBoardAfterMoveUseCase: UpdateBoardAfterMoveUseCase,
    private val determineValidMoveUseCase: DetermineValidMoveUseCase,
    private val checkIfNextMoveAvailableUseCase: CheckIfNextMoveAvailableUseCase
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
            chessMove = chessMove,
            moveSequence = currentGameState.moveSequence
        )
        if (isValidMove) {
            val newBoardPosition = updateBoardAfterMoveUseCase(chessMove, currentGameState.boardPosition)
            val isNextMoveFromWhitePlayer = !chessMove.chessPiece.isWhite
            val updatedMoveSequence = currentGameState.moveSequence.toMutableList()
            updatedMoveSequence.add(chessMove)

            val gameStatus = when (
                checkIfNextMoveAvailableUseCase(
                    isWhiteMove = !chessMove.chessPiece.isWhite,
                    boardPosition = newBoardPosition,
                    pastMoveSequences = updatedMoveSequence
                )
            ) {
                NextMoveResult.NEXT_MOVE_EXISTED -> if (isNextMoveFromWhitePlayer) {
                    GameStatus.WHITE_TURN
                } else {
                    GameStatus.BLACK_TURN
                }
                NextMoveResult.CHECK_MATE -> if (isNextMoveFromWhitePlayer) {
                    GameStatus.BLACK_WIN
                } else {
                    GameStatus.WHITE_WIN
                }
                NextMoveResult.STALE_MATE -> GameStatus.DRAW
            }
            gameUIStateFlow.value = currentGameState.copy(
                boardPosition = newBoardPosition,
                gameStatus = gameStatus,
                moveSequence = updatedMoveSequence
            )
        }
    }
}