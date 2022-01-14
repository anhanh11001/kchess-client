package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import java.lang.IllegalArgumentException

class DetermineKingIsSafeAfterMakingAMoveUseCase(
    private val updateBoardAfterMoveUseCase: UpdateBoardAfterMoveUseCase,
    private val findKingPositionUseCase: FindKingPositionUseCase,
    private val determineIfKingIsValidToCapture: DetermineIfKingIsValidToCapture
) {

    operator fun invoke(
        chessMove: ChessMove,
        boardPosition: Map<String, ChessPiece>,
        isWhiteKing: Boolean,
        moveSequence: List<ChessMove>
    ): Boolean {
        val adjustedBoardPosition = updateBoardAfterMoveUseCase(chessMove, boardPosition)

        val kingPosition = findKingPositionUseCase(isWhiteKing, adjustedBoardPosition)
        for (location in adjustedBoardPosition.keys) {
            val piece = requireNotNull(adjustedBoardPosition[location])
            if (piece.isWhite == isWhiteKing) continue
            val promotedPiece = if (piece is ChessPiece.Pawn &&
                (kingPosition[1].digitToInt() == 1 || kingPosition[1].digitToInt() == 8)
            ) {
                ChessPiece.Queen(!isWhiteKing)
            } else {
                null
            }
            val captureTheKingMove = ChessMove(
                chessPiece = piece,
                startingPosition = location,
                endingPosition = kingPosition,
                promotedPiece = promotedPiece
            )

            val isValidToCaptureKing = determineIfKingIsValidToCapture(
                captureTheKingMove = captureTheKingMove,
                boardPosition = adjustedBoardPosition,
                moveSequence = moveSequence
            )

            if (isValidToCaptureKing) {
                return false
            }
        }

        return true
    }
}