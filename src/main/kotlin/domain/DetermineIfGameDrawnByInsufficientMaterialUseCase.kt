package domain

import data.chess.ChessPiece

class DetermineIfGameDrawnByInsufficientMaterialUseCase {

    operator fun invoke(boardPosition: Map<String, ChessPiece>): Boolean {
        val (whitePieces, blackPieces) = boardPosition.values
            .partition { it.isWhite }
            .let { Pair(it.first.toSet(), it.second.toSet()) }

        // King vs King
        if (whitePieces == setOf(ChessPiece.King(true)) &&
            blackPieces == setOf(ChessPiece.King(false))
        ) {
            return true
        }

        // King & Bishop vs King
        if (whitePieces == setOf(ChessPiece.King(true), ChessPiece.Bishop(true)) &&
            blackPieces == setOf(ChessPiece.King(false))
        ) {
            return true
        }
        if (whitePieces == setOf(ChessPiece.King(true)) &&
            blackPieces == setOf(ChessPiece.King(false), ChessPiece.Bishop(false))
        ) {
            return true
        }

        // King & Knight vs King
        if (whitePieces == setOf(ChessPiece.King(true), ChessPiece.Knight(true)) &&
            blackPieces == setOf(ChessPiece.King(false))
        ) {
            return true
        }
        if (whitePieces == setOf(ChessPiece.King(true)) &&
            blackPieces == setOf(ChessPiece.King(false), ChessPiece.Knight(false))
        ) {
            return true
        }

        // Bishop & King vs Bishop & King
        if (whitePieces == setOf(ChessPiece.King(true), ChessPiece.Bishop(true)) &&
            blackPieces == setOf(ChessPiece.King(false), ChessPiece.Bishop(false))
        ) {
            // Determine if the bishops are in different color square
            var sumOfSquare = 0
            for (square in boardPosition.keys) {
                if (boardPosition[square] is ChessPiece.Bishop) {
                    sumOfSquare += (square[0] - 'a') + (square[1].digitToInt())
                }
            }
            return sumOfSquare % 2 == 0
        }

        return false
    }
}