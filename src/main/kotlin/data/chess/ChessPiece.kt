package data.chess

sealed class ChessPiece(open val isWhite: Boolean) {
    data class King(override val isWhite: Boolean) : ChessPiece(isWhite)
    data class Queen(override val isWhite: Boolean) : ChessPiece(isWhite)
    data class Rook(override val isWhite: Boolean) : ChessPiece(isWhite)
    data class Knight(override val isWhite: Boolean) : ChessPiece(isWhite)
    data class Bishop(override val isWhite: Boolean) : ChessPiece(isWhite)
    data class Pawn(override val isWhite: Boolean) : ChessPiece(isWhite)
}