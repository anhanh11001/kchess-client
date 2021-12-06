package domain

import data.chess.BoardRepresentation
import data.chess.ChessPiece

class ChessBoardTestData {

    companion object {
        val CHESS_TEST_BOARD_OPENING = BoardRepresentation.DEFAULT_BOARD_MAP
        val CHESS_TEST_BOARD_ONE = mapOf<String, ChessPiece>(
            "d1" to ChessPiece.Rook(true),
            "e1" to ChessPiece.King(true),
            "h1" to ChessPiece.Rook(true),
            "a2" to ChessPiece.Pawn(false),
            "c2" to ChessPiece.Pawn(true),
            "d2" to ChessPiece.Queen(true),
            "e2" to ChessPiece.Bishop(true),
            "f2" to ChessPiece.Pawn(true),
            "c3" to ChessPiece.Knight(true),
            "e3" to ChessPiece.Bishop(true),
            "f3" to ChessPiece.Knight(true),
            "b4" to ChessPiece.Pawn(true),
            "c4" to ChessPiece.Pawn(false),
            "d4" to ChessPiece.Pawn(true),
            "g4" to ChessPiece.Pawn(true),
            "h4" to ChessPiece.Pawn(true),
            "e5" to ChessPiece.Pawn(true),
            "f5" to ChessPiece.Pawn(false),
            "g5" to ChessPiece.Pawn(false),
            "h5" to ChessPiece.Pawn(false),
            "c6" to ChessPiece.Knight(false),
            "d6" to ChessPiece.Pawn(false),
            "h6" to ChessPiece.Knight(false),
            "b7" to ChessPiece.Pawn(true),
            "d7" to ChessPiece.Bishop(false),
            "e7" to ChessPiece.Pawn(false),
            "g7" to ChessPiece.Bishop(false),
            "a8" to ChessPiece.Rook(false),
            "d8" to ChessPiece.Queen(false),
            "e8" to ChessPiece.King(false),
            "h8" to ChessPiece.Rook(false)

        )
        val CHESS_TEST_BOARD_TWO = mapOf<String, ChessPiece>(
            "a2" to ChessPiece.Pawn(true),
            "d2" to ChessPiece.King(true),
            "b3" to ChessPiece.Queen(true),
            "c3" to ChessPiece.Pawn(true),
            "e3" to ChessPiece.Rook(true),
            "h3" to ChessPiece.Bishop(true),
            "d5" to ChessPiece.Pawn(false),
            "e5" to ChessPiece.Bishop(false),
            "f5" to ChessPiece.Knight(false),
            "a6" to ChessPiece.Rook(true),
            "d6" to ChessPiece.Queen(false),
            "e6" to ChessPiece.King(false),
            "f6" to ChessPiece.Rook(false),
            "h6" to ChessPiece.Rook(true)
        )

        val CHESS_TEST_BOARD_THREE = mapOf<String, ChessPiece>(
            "a1" to ChessPiece.Rook(true),
            "e1" to ChessPiece.King(true),
            "h1" to ChessPiece.Rook(true),
            "e3" to ChessPiece.Pawn(false),
            "c4" to ChessPiece.Bishop(false),
            "b5" to ChessPiece.Queen(true),
            "g8" to ChessPiece.King(false)
        )
        val CHESS_TEST_BOARD_FOUR = mapOf<String, ChessPiece>(
            "a1" to ChessPiece.Rook(true),
            "e1" to ChessPiece.King(true),
            "h1" to ChessPiece.Rook(true),

            "e2" to ChessPiece.Pawn(true),
            "f4" to ChessPiece.Bishop(false),
            "g8" to ChessPiece.King(false)
        )
    }
}