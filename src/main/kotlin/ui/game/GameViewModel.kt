package ui.game

import common.tickerFlow
import data.*
import data.chess.BoardRepresentation
import data.chess.ChessMove
import data.chess.ChessPiece
import data.mock.MockPlayers
import domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class GameUIState(
    val gameId: Long = 1L,
    val blackPlayer: Player = MockPlayers.BOT_DAVID,
    val whitePlayer: Player = MockPlayers.BOT_DAVID,
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
    @Volatile
    private var requestNextMoveForBot = false
    private val botScope = CoroutineScope(Job())

    init {
        botScope.launch {
            tickerFlow(DELAY_TIME_BETWEEN_BOT_MOVE).collect {
                if (requestNextMoveForBot) {
                    requestNextMoveForBot = false
                    evaluateNextMoveForBots()
                }
            }
        }
    }

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
        requestNextMoveForBot = true
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
        requestNextMoveForBot = true
    }

    private fun evaluateNextMoveForBots() {
        val gameState = gameUIStateFlow.value
        if (gameState.gameStatus == GameStatus.WHITE_TURN && gameState.whitePlayer.isBot) {
            val botMove = determineNextMoveUseCase(
                botStrategy = gameState.whitePlayer.botStrategy,
                isWhiteTurn = true,
                boardPosition = gameState.boardPosition,
                pastMoveSequences = gameState.moveSequence
            )
            onNextMove(botMove)
        } else if (gameState.gameStatus == GameStatus.BLACK_TURN && gameState.blackPlayer.isBot) {
            val botMove = determineNextMoveUseCase(
                botStrategy = gameState.blackPlayer.botStrategy,
                isWhiteTurn = false,
                boardPosition = gameState.boardPosition,
                pastMoveSequences = gameState.moveSequence
            )
            onNextMove(botMove)
        }
    }

    private companion object {
        private const val DELAY_TIME_BETWEEN_BOT_MOVE = 100L
    }
}