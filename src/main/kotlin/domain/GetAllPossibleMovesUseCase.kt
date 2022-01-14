package domain

import data.GameStatus
import data.chess.ChessMove
import data.chess.ChessPiece

class GetAllPossibleMovesUseCase(
    private val determineValidMoveUseCase: DetermineValidMoveUseCase
) {

    operator fun invoke(
        isWhiteTurn: Boolean,
        boardPosition: Map<String, ChessPiece>,
        pastMoveSequences: List<ChessMove>
    ): Set<ChessMove> {
        val possibleMoves = mutableSetOf<ChessMove>()

        for (position in boardPosition.keys) {
            val chessPiece = boardPosition[position] ?: continue
            if (chessPiece.isWhite != isWhiteTurn) continue

            for (col in "abcdefgh") {
                for (row in "12345678") {
                    val square = "$col$row"
                    val chessMove = ChessMove(
                        chessPiece = chessPiece,
                        startingPosition = position,
                        endingPosition =  square,
                        promotedPiece = null // TODO: Handle this
                    )

                    val isValidMove = determineValidMoveUseCase(
                        gameStatus = if (isWhiteTurn) {
                            GameStatus.WHITE_TURN
                        } else {
                            GameStatus.BLACK_TURN
                        },
                        boardPosition = boardPosition,
                        chessMove = chessMove,
                        moveSequence = pastMoveSequences
                    )

                    if (isValidMove) possibleMoves.add(chessMove)
                }
            }
        }

        return possibleMoves
    }
}