package data

class BoardRepresentation {

    companion object {
        val DEFAULT_BOARD_MAP = mapOf(
            "1a" to ChessPiece.Rook(true),
            "1b" to ChessPiece.Knight(true),
            "1c" to ChessPiece.Bishop(true),
            "1d" to ChessPiece.Queen(true),
            "1e" to ChessPiece.King(true),
            "1f" to ChessPiece.Bishop(true),
            "1g" to ChessPiece.Knight(true),
            "1h" to ChessPiece.Rook(true),
            "2a" to ChessPiece.Pawn(true),
            "2b" to ChessPiece.Pawn(true),
            "2c" to ChessPiece.Pawn(true),
            "2d" to ChessPiece.Pawn(true),
            "2e" to ChessPiece.Pawn(true),
            "2f" to ChessPiece.Pawn(true),
            "2g" to ChessPiece.Pawn(true),
            "2h" to ChessPiece.Pawn(true),
            "8a" to ChessPiece.Rook(false),
            "8b" to ChessPiece.Knight(false),
            "8c" to ChessPiece.Bishop(false),
            "8d" to ChessPiece.Queen(false),
            "8e" to ChessPiece.King(false),
            "8f" to ChessPiece.Bishop(false),
            "8g" to ChessPiece.Knight(false),
            "8h" to ChessPiece.Rook(false),
            "7a" to ChessPiece.Pawn(false),
            "7b" to ChessPiece.Pawn(false),
            "7c" to ChessPiece.Pawn(false),
            "7d" to ChessPiece.Pawn(false),
            "7e" to ChessPiece.Pawn(false),
            "7f" to ChessPiece.Pawn(false),
            "7g" to ChessPiece.Pawn(false),
            "7h" to ChessPiece.Pawn(false),
        )
    }
}