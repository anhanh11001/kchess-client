package domain

import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineNextRandomMoveUseCase(
    private val getAllPossibleMovesUseCase: GetAllPossibleMovesUseCase
) {

    operator fun invoke(
        isWhiteTurn: Boolean,
        boardPosition: Map<String, ChessPiece>,
        pastMoveSequences: List<ChessMove>
    ): ChessMove {
        val allMoves = getAllPossibleMovesUseCase(
            isWhiteTurn = isWhiteTurn,
            boardPosition = boardPosition,
            pastMoveSequences = pastMoveSequences
        )
        return allMoves.random()
    }
}