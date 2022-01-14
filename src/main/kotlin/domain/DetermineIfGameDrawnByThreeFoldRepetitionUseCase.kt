package domain

import data.chess.ChessMove

class DetermineIfGameDrawnByThreeFoldRepetitionUseCase {

    operator fun invoke(moveSequence: List<ChessMove>): Boolean {
        return false
    }
}