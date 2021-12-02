package domain

import data.chess.ChessPiece
import kotlin.math.abs

class DetermineValidStraightMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        startingPosition: String,
        endingPosition: String,
        isMovedPieceWhite: Boolean
    ): Boolean {
        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])
        require(rowDiff == 0 || colDiff == 0) { "Invalid straight move" }

        when {
            colDiff == 0 && rowDiff == 0 -> return false
            colDiff == 0 -> {
                val rowIncreasing = startingPosition[1] < endingPosition[1]
                val startingIndex = if (rowIncreasing) {
                    startingPosition[1].digitToInt() + 1
                } else {
                    endingPosition[1].digitToInt() + 1
                }
                val endingIndex = if (rowIncreasing) {
                    endingPosition[1].digitToInt()
                } else {
                    startingPosition[1].digitToInt()
                }

                for (i in startingIndex until endingIndex) {
                    if (boardPosition["${startingPosition[0]}$i"] != null) {
                        return false
                    }
                }

                val pieceInEndingPosition = boardPosition[endingPosition]
                return pieceInEndingPosition == null || (isMovedPieceWhite == pieceInEndingPosition.isWhite)
            }
            rowDiff == 0 -> {
                val colIncreasing = startingPosition[1] < endingPosition[1]
                val startingIndex = if (colIncreasing) {
                    startingPosition[0].digitToInt() + 1
                } else {
                    endingPosition[0].digitToInt() + 1
                }
                val endingIndex = if (colIncreasing) {
                    endingPosition[0].digitToInt()
                } else {
                    startingPosition[0].digitToInt()
                }

                for (i in startingIndex until endingIndex) {
                    if (boardPosition["$i${startingPosition[1]}"] != null) {
                        return false
                    }
                }

                val pieceInEndingPosition = boardPosition[endingPosition]
                return pieceInEndingPosition == null || (isMovedPieceWhite == pieceInEndingPosition.isWhite)

            }
            else -> return false
        }
    }
}