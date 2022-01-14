package domain

import data.chess.ChessPiece
import java.lang.IllegalArgumentException

class FindKingPositionUseCase {

    operator fun invoke(
        isWhiteKing: Boolean,
        boardPosition: Map<String, ChessPiece>
    ): String {
        for (position in boardPosition.keys) {
            val piece = boardPosition[position]
            if (piece is ChessPiece.King && piece.isWhite == isWhiteKing) {
                return position
            }
        }
        throw IllegalArgumentException("Invalid board: The board should have the king for both side.")
    }
}