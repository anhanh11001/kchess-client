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
        adjustedBoardPosition[chessMove.endingPosition] = chessMove.chessPiece

        val kingPosition = findKingPosition(isWhiteKing, boardPosition)
        for (location in boardPosition.keys) {
            val piece = requireNotNull(boardPosition[location])
            if (piece.isWhite == isWhiteKing) continue
            val captureTheKingMove = ChessMove(
                moveFromWhitePlayer = !isWhiteKing,
                chessPiece = piece,
                startingPosition = location,
                endingPosition = kingPosition,
                moveType = MoveType.Normal // TODO: Fix this for pawn promotion and castle
            )

            val isValidToCaptureKing = when (piece) {
                is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(adjustedBoardPosition, captureTheKingMove)
                is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(adjustedBoardPosition, captureTheKingMove)
                is ChessPiece.King -> determineCorrectKingMoveUseCase(adjustedBoardPosition, captureTheKingMove)
                is ChessPiece.Rook -> determineCorrectRookMoveUseCase(adjustedBoardPosition, captureTheKingMove)
                is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(adjustedBoardPosition, captureTheKingMove)
                is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(adjustedBoardPosition, captureTheKingMove)
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