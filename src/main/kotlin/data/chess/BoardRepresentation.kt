package data.chess

class BoardRepresentation {

    companion object {
        val DEFAULT_BOARD_MAP = mapOf(
            "a1" to ChessPiece.Rook(true),
            "b1" to ChessPiece.Knight(true),
            "c1" to ChessPiece.Bishop(true),
            "d1" to ChessPiece.Queen(true),
            "e1" to ChessPiece.King(true),
            "f1" to ChessPiece.Bishop(true),
            "g1" to ChessPiece.Knight(true),
            "h1" to ChessPiece.Rook(true),
            "a2" to ChessPiece.Pawn(true),
            "b2" to ChessPiece.Pawn(true),
            "c2" to ChessPiece.Pawn(true),
            "d2" to ChessPiece.Pawn(true),
            "e2" to ChessPiece.Pawn(true),
            "f2" to ChessPiece.Pawn(true),
            "g2" to ChessPiece.Pawn(true),
            "h2" to ChessPiece.Pawn(true),
            "a8" to ChessPiece.Rook(false),
            "b8" to ChessPiece.Knight(false),
            "c8" to ChessPiece.Bishop(false),
            "d8" to ChessPiece.Queen(false),
            "e8" to ChessPiece.King(false),
            "f8" to ChessPiece.Bishop(false),
            "g8" to ChessPiece.Knight(false),
            "h8" to ChessPiece.Rook(false),
            "a7" to ChessPiece.Pawn(false),
            "b7" to ChessPiece.Pawn(false),
            "c7" to ChessPiece.Pawn(false),
            "d7" to ChessPiece.Pawn(false),
            "e7" to ChessPiece.Pawn(false),
            "f7" to ChessPiece.Pawn(false),
            "g7" to ChessPiece.Pawn(false),
            "h7" to ChessPiece.Pawn(false),
        )
    }
}