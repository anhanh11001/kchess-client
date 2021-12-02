package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import data.chess.MoveType
import kotlin.math.abs

class DetermineCorrectPawnMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove
    ): Boolean {
        val chessPiece = chessMove.chessPiece
        require(chessPiece is ChessPiece.Pawn)

        val startPosition = chessMove.startingPosition
        val endPosition = chessMove.endingPosition

        return when (val moveType = chessMove.moveType) {
            MoveType.Castle -> false
            is MoveType.Normal -> {
                when {
                    startPosition[0] - endPosition[0] == 0 -> determineValidStraightPawnMove(
                        isWhitePawn = chessPiece.isWhite,
                        startingRow = startPosition[1].digitToInt(),
                        endingRow = endPosition[1].digitToInt(),
                        col = startPosition[0],
                        boardPosition = boardPosition
                    )

                    abs(startPosition[0] - endPosition[0]) == 1 -> determineValidDiagonalPawnMove(
                        isWhitePawn = chessPiece.isWhite,
                        startingRow = startPosition[1].digitToInt(),
                        endingRow = endPosition[1].digitToInt(),
                        endingCol = endPosition[0],
                        boardPosition = boardPosition
                    )
                    else -> false
                }
            }
            is MoveType.PawnPromotion -> determineValidPawnPromotion(
                isWhitePawn = chessPiece.isWhite,
                startingPosition = chessMove.startingPosition,
                endingPosition = endPosition,
                boardPosition = boardPosition,
                promotedPiece = moveType.promotedPiece
            )
        }
    }

    private fun determineValidStraightPawnMove(
        isWhitePawn: Boolean,
        startingRow: Int,
        endingRow: Int,
        col: Char,
        boardPosition: Map<String, ChessPiece>
    ): Boolean {
        return if (isWhitePawn) { // White Pawn
            when {
                startingRow < 2 || startingRow > 7 -> false
                startingRow == 2 && endingRow == 4 -> boardPosition["${col}3"] == null && boardPosition["${col}4"] == null
                else -> endingRow == startingRow + 1 && boardPosition["$col$endingRow"] == null
            }
        } else { // Black Pawn
            when {
                startingRow < 3 || startingRow > 7 -> false
                startingRow == 7 && endingRow == 5 -> boardPosition["${col}5"] == null && boardPosition["${col}6"] == null
                else -> endingRow == startingRow - 1 && boardPosition["$col$endingRow"] == null
            }
        }
    }

    private fun determineValidDiagonalPawnMove(
        isWhitePawn: Boolean,
        startingRow: Int,
        endingRow: Int,
        endingCol: Char,
        boardPosition: Map<String, ChessPiece>
    ): Boolean {
        val endPosition = "$endingCol$endingRow"
        if (isWhitePawn) {
            if (startingRow < 2 || startingRow > 6) return false
            if (endingRow - startingRow != 1) return false
            return if (boardPosition[endPosition] == null) {
                // TODO: En Passant
                false
            } else {
                !requireNotNull(boardPosition[endPosition]).isWhite
            }
        } else {
            if (startingRow > 7 || startingRow < 3) return false
            if (startingRow - endingRow != 1) return false
            return if (boardPosition[endPosition] == null) {
                // TODO: En Passant
                false
            } else {
                requireNotNull(boardPosition[endPosition]).isWhite
            }
        }
    }

    private fun determineValidPawnPromotion(
        isWhitePawn: Boolean,
        startingPosition: String,
        endingPosition: String,
        boardPosition: Map<String, ChessPiece>,
        promotedPiece: ChessPiece
    ): Boolean {
        if (promotedPiece is ChessPiece.King || promotedPiece is ChessPiece.Pawn) return false

        if (isWhitePawn) {
            return when {
                startingPosition[1].digitToInt() != 7 && endingPosition[1].digitToInt() != 8 -> false
                startingPosition[0] == endingPosition[0] -> {
                    boardPosition[endingPosition] == null
                }
                abs(startingPosition[0] - endingPosition[0]) == 1 -> {
                    val takenPiece = boardPosition[endingPosition]
                    takenPiece != null && !takenPiece.isWhite
                }
                else -> false
            }
        } else {
            return when {
                startingPosition[1].digitToInt() != 2 && endingPosition[1].digitToInt() != 1 -> false
                startingPosition[0] == endingPosition[0] -> {
                    boardPosition[endingPosition] == null
                }
                abs(startingPosition[0] - endingPosition[0]) == 1 -> {
                    val takenPiece = boardPosition[endingPosition]
                    takenPiece != null && !takenPiece.isWhite
                }
                else -> false
            }
        }
    }
}