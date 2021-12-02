package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class DetermineCorrectKingMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove
    ): Boolean {
        val startingPosition = chessMove.startingPosition
        val endingPosition = chessMove.endingPosition

        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])

        if ((colDiff > 1 || rowDiff > 1) || (colDiff == 0 && rowDiff == 0)) return false

        val pieceInEndingPosition = boardPosition[endingPosition]
        return pieceInEndingPosition == null || (pieceInEndingPosition.isWhite != chessMove.chessPiece.isWhite)
    }
}