package data.chess

sealed class ChessPiece(open val isWhite: Boolean) {
    class King(override val isWhite: Boolean, val hasMoved: Boolean = false) : ChessPiece(isWhite)
    class Queen(override val isWhite: Boolean) : ChessPiece(isWhite)
    class Rook(override val isWhite: Boolean, val hasMoved: Boolean = false) : ChessPiece(isWhite)
    class Knight(override val isWhite: Boolean) : ChessPiece(isWhite)
    class Bishop(override val isWhite: Boolean) : ChessPiece(isWhite)
    class Pawn(override val isWhite: Boolean) : ChessPiece(isWhite)
}