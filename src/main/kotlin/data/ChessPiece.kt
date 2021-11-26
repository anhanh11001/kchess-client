package data

sealed class ChessPiece(open val isWhite: Boolean) {
    class King(override val isWhite: Boolean): ChessPiece(isWhite)
    class Queen(override val isWhite: Boolean): ChessPiece(isWhite)
    class Rook(override val isWhite: Boolean): ChessPiece(isWhite)
    class Knight(override val isWhite: Boolean): ChessPiece(isWhite)
    class Bishop(override val isWhite: Boolean): ChessPiece(isWhite)
    class Pawn(override val isWhite: Boolean): ChessPiece(isWhite)
}