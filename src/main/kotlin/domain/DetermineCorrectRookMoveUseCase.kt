package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class DetermineCorrectRookMoveUseCase(
    private val determineValidStraightMoveUseCase: DetermineValidStraightMoveUseCase
) {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove,
        checkForAttackingMoveOnly: Boolean = false
    ): Boolean {
        val startingPosition = chessMove.startingPosition
        val endingPosition = chessMove.endingPosition

        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])

        return (colDiff == 0 || rowDiff == 0) && determineValidStraightMoveUseCase(
            boardPosition = boardPosition,
            startingPosition = chessMove.startingPosition,
            endingPosition = chessMove.endingPosition,
            isMovedPieceWhite = chessMove.chessPiece.isWhite,
            checkForAttackingMoveOnly = checkForAttackingMoveOnly
        )
    }
}