package data.chess

data class ChessMove(
    val moveFromWhitePlayer: Boolean,
    val chessPiece: ChessPiece,
    val startingPosition: String,
    val endingPosition: String,
    val moveType: MoveType
)

sealed class MoveType {
    object Normal : MoveType()
    object Castle : MoveType()
    data class PawnPromotion(
        val promotedPiece: ChessPiece
    ) : MoveType()

}