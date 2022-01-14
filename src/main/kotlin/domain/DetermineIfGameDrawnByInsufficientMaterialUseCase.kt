package domain

import data.chess.ChessPiece

class DetermineIfGameDrawnByInsufficientMaterialUseCase {

    operator fun invoke(chessPieces: Set<ChessPiece>): Boolean {
        return false
    }
}