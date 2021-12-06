package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class DetermineCorrectPawnMoveUseCase {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove,
        checkForCapturingInPlaceOnly: Boolean = false,
        opponentLastPlayedMove: ChessMove?
    ): Boolean {
        val chessPiece = chessMove.chessPiece
        require(chessPiece is ChessPiece.Pawn)

        val startPosition = chessMove.startingPosition
        val endPosition = chessMove.endingPosition

        val movedVerticalSpace = endPosition[1].digitToInt() - startPosition[1].digitToInt()
        if (chessPiece.isWhite &&
            movedVerticalSpace != 1 &&
            !(movedVerticalSpace == 2 && startPosition[1].digitToInt() == 2)
        ) return false

        if (!chessPiece.isWhite &&
            movedVerticalSpace != -1 &&
            !(movedVerticalSpace == -2 && startPosition[1].digitToInt() == 7)
        ) return false

        return when {
            startPosition[0] - endPosition[0] == 0 -> !checkForCapturingInPlaceOnly && determineValidStraightPawnMove(
                isWhitePawn = chessPiece.isWhite,
                startingRow = startPosition[1].digitToInt(),
                endingRow = endPosition[1].digitToInt(),
                col = startPosition[0],
                boardPosition = boardPosition,
                promotedPiece = chessMove.promotedPiece
            )

            abs(startPosition[0] - endPosition[0]) == 1 -> determineValidDiagonalPawnMove(
                isWhitePawn = chessPiece.isWhite,
                startingRow = startPosition[1].digitToInt(),
                endingRow = endPosition[1].digitToInt(),
                endingCol = endPosition[0],
                boardPosition = boardPosition,
                opponentLastPlayedMove = opponentLastPlayedMove,
                checkForCapturingInPlaceOnly = checkForCapturingInPlaceOnly,
                promotedPiece = chessMove.promotedPiece
            )
            else -> false
        }
    }

    private fun determineValidStraightPawnMove(
        isWhitePawn: Boolean,
        startingRow: Int,
        endingRow: Int,
        col: Char,
        boardPosition: Map<String, ChessPiece>,
        promotedPiece: ChessPiece?
    ): Boolean {
        return if (isWhitePawn) { // White Pawn
            when {
                endingRow == 8 -> boardPosition["$col$endingRow"] == null && promotedPiece != null &&
                        promotedPiece !is ChessPiece.King && promotedPiece !is ChessPiece.Pawn
                startingRow < 2 || startingRow > 7 -> false
                startingRow == 2 && endingRow == 4 -> boardPosition["${col}3"] == null && boardPosition["${col}4"] == null
                else -> endingRow == startingRow + 1 && boardPosition["$col$endingRow"] == null
            }
        } else { // Black Pawn
            when {
                endingRow == 1 -> boardPosition["$col$endingRow"] == null && promotedPiece != null &&
                        promotedPiece !is ChessPiece.King && promotedPiece !is ChessPiece.Pawn
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
        boardPosition: Map<String, ChessPiece>,
        opponentLastPlayedMove: ChessMove?,
        checkForCapturingInPlaceOnly: Boolean,
        promotedPiece: ChessPiece?
    ): Boolean {
        val endPosition = "$endingCol$endingRow"
        if (isWhitePawn && ((startingRow < 2 || startingRow > 7) || (endingRow - startingRow != 1))) return false
        if (!isWhitePawn && ((startingRow > 7 || startingRow < 2) || (startingRow - endingRow != 1))) return false

        return if (boardPosition[endPosition] == null) {
            // EnPassant
            if (endingRow == 1 || endingRow == 8) return false
            if (checkForCapturingInPlaceOnly) return false
            val lastMoveChessPiece = opponentLastPlayedMove?.chessPiece as? ChessPiece.Pawn ?: return false
            if (lastMoveChessPiece.isWhite == isWhitePawn) return false
            if ((isWhitePawn && endingRow != 6) || (!isWhitePawn && endingRow != 3)) return false
            if (abs(opponentLastPlayedMove.startingPosition[1].digitToInt() - opponentLastPlayedMove.endingPosition[1].digitToInt()) != 2) return false
            if (opponentLastPlayedMove.endingPosition != "$endingCol$startingRow") return false
            true
        } else {
            if ((endingRow == 1 || endingRow == 8) && (promotedPiece == null || promotedPiece is ChessPiece.King || promotedPiece is ChessPiece.Pawn)) return false
            requireNotNull(boardPosition[endPosition]).isWhite != isWhitePawn
        }
    }

}