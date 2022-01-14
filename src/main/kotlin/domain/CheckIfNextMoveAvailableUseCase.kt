package domain

import data.chess.ChessPiece

class CheckIfNextMoveAvailableUseCase {

    operator fun invoke(
        isWhiteMove: Boolean,
        boardPosition: Map<String, ChessPiece>
    ): NextMoveResult {
        return TODO()
    }
}

enum class NextMoveResult {
    CHECK_MATE,
    STALE_MATE,
    NEXT_MOVE_EXISTED
}