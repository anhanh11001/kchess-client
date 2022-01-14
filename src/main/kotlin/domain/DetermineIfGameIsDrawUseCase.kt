package domain

import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineIfGameIsDrawUseCase(
    private val determineIfGameDrawnByInsufficientMaterialUseCase: DetermineIfGameDrawnByInsufficientMaterialUseCase,
    private val determineIfGameDrawnByThreeFoldRepetitionUseCase: DetermineIfGameDrawnByThreeFoldRepetitionUseCase,
    private val determineIfGameDrawnByFiftyMoveRuleUseCase: DetermineIfGameDrawnByFiftyMoveRuleUseCase
) {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        moveSequence: List<ChessMove>
    ): Boolean {
        return determineIfGameDrawnByFiftyMoveRuleUseCase(moveSequence) ||
                determineIfGameDrawnByThreeFoldRepetitionUseCase(moveSequence) ||
                determineIfGameDrawnByInsufficientMaterialUseCase(boardPosition.values.toSet())
    }
}