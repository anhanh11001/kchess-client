package data.chess

data class ChessMove(
    val chessPiece: ChessPiece,
    val startingPosition: String,
    val endingPosition: String,
    val moveType: MoveType
) {

    fun description(): String {
        val chessName = when (chessPiece) {
            is ChessPiece.Pawn -> ""
            is ChessPiece.King -> "K"
            is ChessPiece.Queen -> "Q"
            is ChessPiece.Knight -> "N"
            is ChessPiece.Bishop -> "B"
            is ChessPiece.Rook -> "R"
        }
        return "$chessName$endingPosition"
    }

}

sealed class MoveType {
    object Normal : MoveType()
    data class PawnPromotion(
        val promotedPiece: ChessPiece
    ) : MoveType()

}