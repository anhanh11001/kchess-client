package domain

import chessbot.BotStrategy
import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineNextRandomMoveUseCase {


    operator fun invoke(
        isWhiteTurn: Boolean,
        boardPosition: Map<String, ChessPiece>
    ): ChessMove {
        return TODO()
    }
}