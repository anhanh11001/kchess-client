package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import data.chess.MoveType
import kotlin.math.abs

class DetermineCorrectKnightMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove
    ): Boolean {
        val startingPosition = chessMove.startingPosition
        val endingPosition = chessMove.endingPosition

        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])

        return if ((colDiff == 1 && rowDiff == 2) || (rowDiff == 1 && colDiff == 2)) {
            val pieceInEndingPosition = boardPosition[endingPosition]
            pieceInEndingPosition == null || (pieceInEndingPosition.isWhite != chessMove.chessPiece.isWhite)
        } else {
            false
        }
    }
}