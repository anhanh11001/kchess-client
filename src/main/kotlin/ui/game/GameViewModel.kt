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

    fun onNextMove(chessMove: ChessMove) {
        val currentGameState = gameUIStateFlow.value
        val isValidMove = determineValidMoveUseCase(
            gameStatus = currentGameState.game.gameStatus,
            boardPosition = currentGameState.boardPosition,
            chessMove = chessMove
        )
        if (!isValidMove) {

        } else {

        }
    }
}