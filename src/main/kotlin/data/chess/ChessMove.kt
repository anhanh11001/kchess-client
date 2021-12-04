package data.chess

import kotlin.math.abs


data class ChessMove(
    val chessPiece: ChessPiece,
    val startingPosition: String,
    val endingPosition: String,
    val promotedPiece: ChessPiece? = null
) {

    fun description(): String {
        if (chessPiece is ChessPiece.King) {
            if (abs(startingPosition[0] - endingPosition[0]) == 2) return "0-0"
            if (abs(startingPosition[0] - endingPosition[0]) == 3) return "0-0-0"
        }
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