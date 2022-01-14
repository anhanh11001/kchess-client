package domain

import data.chess.ChessMove
import data.chess.ChessPiece

class CheckIfNextMoveAvailableUseCase(
    private val findAllPossibleMovesUseCase: GetAllPossibleMovesUseCase,
    private val findKingPositionUseCase: FindKingPositionUseCase,
    private val determineIfKingIsValidToCapture: DetermineIfKingIsValidToCapture
) {

    operator fun invoke(
        isWhiteMove: Boolean,
        boardPosition: Map<String, ChessPiece>,
        pastMoveSequences: List<ChessMove>
    ): NextMoveResult {
        val isNextMoveAvailable = findAllPossibleMovesUseCase(
            isWhiteTurn = isWhiteMove,
            boardPosition = boardPosition,
            pastMoveSequences = pastMoveSequences
        ).isNotEmpty()
        if (isNextMoveAvailable) {
            return NextMoveResult.NEXT_MOVE_EXISTED
        }

        val kingPosition = findKingPositionUseCase(
            isWhiteKing = isWhiteMove,
            boardPosition = boardPosition
        )

        for (position in boardPosition.keys) {
            val chessPiece = boardPosition[position]
            if (chessPiece == null || chessPiece.isWhite == isWhiteMove) {
                continue
            }

            val captureTheKingMove = ChessMove(
                chessPiece = chessPiece,
                startingPosition = position,
                endingPosition = kingPosition
            )

            if (determineIfKingIsValidToCapture(
                    captureTheKingMove = captureTheKingMove,
                    boardPosition = boardPosition,
                    moveSequence = pastMoveSequences
                )
            ) {
                return NextMoveResult.CHECK_MATE
            }
        }

        return NextMoveResult.STALE_MATE
    }
}

enum class NextMoveResult {
    CHECK_MATE,
    STALE_MATE,
    NEXT_MOVE_EXISTED
}