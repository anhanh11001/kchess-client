package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import data.chess.MoveType
import java.lang.IllegalArgumentException

class DetermineKingIsSafeAfterMakingAMoveUseCase(
    private val determineCorrectQueenMoveUseCase: DetermineCorrectQueenMoveUseCase,
    private val determineCorrectKingMoveUseCase: DetermineCorrectKingMoveUseCase,
    private val determineCorrectBishopMoveUseCase: DetermineCorrectBishopMoveUseCase,
    private val determineCorrectKnightMoveUseCase: DetermineCorrectKnightMoveUseCase,
    private val determineCorrectRookMoveUseCase: DetermineCorrectRookMoveUseCase,
    private val determineCorrectPawnMoveUseCase: DetermineCorrectPawnMoveUseCase,
) {

    operator fun invoke(
        chessMove: ChessMove,
        boardPosition: Map<String, ChessPiece>,
        isWhiteKing: Boolean
    ): Boolean {
        val adjustedBoardPosition = boardPosition.toMutableMap()
        adjustedBoardPosition.remove(chessMove.startingPosition)
        adjustedBoardPosition.remove(chessMove.endingPosition)
        adjustedBoardPosition[chessMove.endingPosition] = chessMove.chessPiece

        val kingPosition = findKingPosition(isWhiteKing, adjustedBoardPosition)
        for (location in adjustedBoardPosition.keys) {
            val piece = requireNotNull(adjustedBoardPosition[location])
            if (piece.isWhite == isWhiteKing) continue
            val captureTheKingMove = ChessMove(
                chessPiece = piece,
                startingPosition = location,
                endingPosition = kingPosition,
                moveType = MoveType.Normal // TODO: Fix this for pawn promotion and castle
            )

            val isValidToCaptureKing = when (piece) {
                is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
                is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
                is ChessPiece.King -> determineCorrectKingMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
                is ChessPiece.Rook -> determineCorrectRookMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
                is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
                is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true
                )
            }

            if (isValidToCaptureKing) {
                return false
            }
        }

        return true
    }

    private fun findKingPosition(
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