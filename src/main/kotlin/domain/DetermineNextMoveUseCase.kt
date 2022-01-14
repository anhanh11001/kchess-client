package domain

import chessbot.BotStrategy
import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineNextMoveUseCase(
    private val determineNextRandomMoveUseCase: DetermineNextRandomMoveUseCase,
    private val determineNextSimpleMinimaxMoveUseCase: DetermineNextSimpleMinimaxMoveUseCase,
    private val determineNextDeepLearningMoveUseCase: DetermineNextDeepLearningMoveUseCase
) {

    operator fun invoke(
        botStrategy: BotStrategy,
        isWhiteTurn: Boolean,
        boardPosition: Map<String, ChessPiece>,
        pastMoveSequences: List<ChessMove>
    ): ChessMove {
        return when (botStrategy) {
            BotStrategy.NOT_BOT -> throw IllegalArgumentException("Only bot player can call this domain class")
            BotStrategy.RANDOM -> determineNextRandomMoveUseCase(
                isWhiteTurn = isWhiteTurn,
                boardPosition = boardPosition,
                pastMoveSequences = pastMoveSequences
            )
            BotStrategy.DEEP_BOT -> determineNextDeepLearningMoveUseCase(
                isWhiteTurn = isWhiteTurn,
                boardPosition = boardPosition,
                pastMoveSequences = pastMoveSequences
            )
            BotStrategy.SIMPLE_MINIMAX -> determineNextSimpleMinimaxMoveUseCase(
                isWhiteTurn = isWhiteTurn,
                boardPosition = boardPosition,
                pastMoveSequences = pastMoveSequences
            )
        }
    }
}