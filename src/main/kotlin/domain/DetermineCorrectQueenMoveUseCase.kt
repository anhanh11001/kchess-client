package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class DetermineCorrectQueenMoveUseCase(
    private val determineValidStraightMoveUseCase: DetermineValidStraightMoveUseCase,
    private val determineValidDiagonalMoveUseCase: DetermineValidDiagonalMoveUseCase
) {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove
    ): Boolean {
        val startingPosition = chessMove.startingPosition
        val endingPosition = chessMove.endingPosition

        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])

        return when {
            (colDiff == 0 && rowDiff != 0) || (rowDiff == 0 && colDiff != 0) -> determineValidStraightMoveUseCase(
                boardPosition = boardPosition,
                startingPosition = startingPosition,
                endingPosition = endingPosition,
                isMovedPieceWhite = chessMove.chessPiece.isWhite
            )
            colDiff == rowDiff && colDiff != 0 -> determineValidDiagonalMoveUseCase(
                boardPosition = boardPosition,
                startingPosition = startingPosition,
                endingPosition = endingPosition,
                isMovedPieceWhite = chessMove.chessPiece.isWhite
            )
            else -> false
        }
    }
}