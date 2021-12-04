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
    val game: Game,
    val boardPosition: Map<String, ChessPiece>
)

class GameViewModel(
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
    private val getGameByGameIdUseCase: GetGameByGameIdUseCase,
    private val determineNextMoveUseCase: DetermineNextMoveUseCase,
    private val determineValidMoveUseCase: DetermineValidMoveUseCase
) {

    val gameUIStateFlow = MutableStateFlow(
        GameUIState(
            blackPlayer = MockPlayers.FABIANO_CARUANA,
            whitePlayer = MockPlayers.MAGNUS_CARLSEN,
            game = Game(
                gameId = 1L,
                blackPlayerId = MockPlayers.FABIANO_CARUANA.playerId,
                whitePlayerId = MockPlayers.MAGNUS_CARLSEN.playerId,
                moveSequence = "",
                gameStatus = GameStatus.NOT_STARTED,
                timeLimit = 600L
            ),
            boardPosition = BoardRepresentation.DEFAULT_BOARD_MAP
        )
    )

    fun startGame() {
        val currentGameState = gameUIStateFlow.value
        if (currentGameState.game.gameStatus == GameStatus.NOT_STARTED) {
            val updatedGame = currentGameState.game.copy(gameStatus = GameStatus.WHITE_TURN)
            val updatedUIState = currentGameState.copy(game = updatedGame)
            gameUIStateFlow.value = updatedUIState
        }
    }

    fun onTimeEnded(isWhite: Boolean) {
        val currentGameState = gameUIStateFlow.value

        val gameStatus = if (isWhite) {
            GameStatus.BLACK_WIN
        } else {
            GameStatus.WHITE_WIN
        }
        val updatedGame = currentGameState.game.copy(gameStatus = gameStatus)
        gameUIStateFlow.value = currentGameState.copy(game = updatedGame)
    }

    fun onNextMove(chessMove: ChessMove) {
        val currentGameState = gameUIStateFlow.value
        val isValidMove = determineValidMoveUseCase(
            gameStatus = currentGameState.game.gameStatus,
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
            val updatedGame = currentGameState.game.copy(gameStatus = gameStatus)

            gameUIStateFlow.value = currentGameState.copy(
                boardPosition = newBoardPosition,
                game = updatedGame
            )
        }
    }
}