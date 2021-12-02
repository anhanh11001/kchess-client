package domain

import data.chess.ChessPiece
import kotlin.math.abs

class DetermineValidDiagonalMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        startingPosition: String,
        endingPosition: String,
        isMovedPieceWhite: Boolean
    ): Boolean {
        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])
        require(rowDiff == colDiff && rowDiff != 0) { "Invalid diagonal move" }

        val rowIncreasing = startingPosition[1] < endingPosition[1]
        val colIncreasing = startingPosition[0] < endingPosition[0]

        for (distance in 1 until rowDiff) {
            val checkedSquareRow = if (rowIncreasing) {
                startingPosition[0] + distance
            } else {
                startingPosition[0] - distance
            }
            val checkedSquareCol = if (colIncreasing) {
                startingPosition[1] + distance
            } else {
                startingPosition[1] - distance
            }
            if (boardPosition["$checkedSquareCol$checkedSquareRow"] != null) {
                return false
            }
        }
        val pieceInEndingPosition = boardPosition[endingPosition]
        return pieceInEndingPosition == null || (pieceInEndingPosition.isWhite != isMovedPieceWhite)
    }
}