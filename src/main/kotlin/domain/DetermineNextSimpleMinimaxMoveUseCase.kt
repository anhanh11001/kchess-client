package domain

import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineNextSimpleMinimaxMoveUseCase {

    operator fun invoke(
        isWhiteTurn: Boolean,
        boardPosition: Map<String, ChessPiece>,
        pastMoveSequences: List<ChessMove>
    ): ChessMove {
        return TODO()
    }
}