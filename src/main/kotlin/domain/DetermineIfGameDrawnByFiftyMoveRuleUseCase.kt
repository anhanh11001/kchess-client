package domain

import data.chess.ChessMove

class DetermineIfGameDrawnByFiftyMoveRuleUseCase {

    operator fun invoke(moveSequence: List<ChessMove>): Boolean {
        return false
    }
}