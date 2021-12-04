package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import java.lang.IllegalArgumentException

class DetermineKingIsSafeAfterMakingAMoveUseCase(
    private val determineCorrectQueenMoveUseCase: DetermineCorrectQueenMoveUseCase,
    private val determineCorrectKingMoveUseCase: DetermineCorrectKingMoveUseCase,
    private val determineCorrectBishopMoveUseCase: DetermineCorrectBishopMoveUseCase,
    private val determineCorrectKnightMoveUseCase: DetermineCorrectKnightMoveUseCase,
    private val determineCorrectRookMoveUseCase: DetermineCorrectRookMoveUseCase,
    private val determineCorrectPawnMoveUseCase: DetermineCorrectPawnMoveUseCase,
    private val updateBoardAfterMoveUseCase: UpdateBoardAfterMoveUseCase
) {

    operator fun invoke(
        chessMove: ChessMove,
        boardPosition: Map<String, ChessPiece>,
        isWhiteKing: Boolean,
        moveSequence: List<ChessMove>
    ): Boolean {
        val adjustedBoardPosition = updateBoardAfterMoveUseCase(chessMove, boardPosition)

        val kingPosition = findKingPosition(isWhiteKing, adjustedBoardPosition)
        for (location in adjustedBoardPosition.keys) {
            val piece = requireNotNull(adjustedBoardPosition[location])
            if (piece.isWhite == isWhiteKing) continue
            val captureTheKingMove = ChessMove(
                chessPiece = piece,
                startingPosition = location,
                endingPosition = kingPosition
            )

            val isValidToCaptureKing = when (piece) {
                is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true,
                    opponentLastPlayedMove = moveSequence.lastOrNull()
                )
                is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove
                )
                is ChessPiece.King -> determineCorrectKingMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove,
                    checkForAttackingMoveOnly = true,
                    moveSequence = moveSequence
                )
                is ChessPiece.Rook -> determineCorrectRookMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove
                )
                is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove
                )
                is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(
                    boardPosition = adjustedBoardPosition,
                    chessMove = captureTheKingMove
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