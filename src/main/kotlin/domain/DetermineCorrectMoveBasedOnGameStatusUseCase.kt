package domain

import data.GameStatus
import data.chess.ChessMove

class DetermineCorrectMoveBasedOnGameStatusUseCase {

    operator fun invoke(
        gameStatus: GameStatus,
        chessMove: ChessMove
    ): Boolean {
        if (gameStatus != GameStatus.WHITE_TURN || gameStatus != GameStatus.BLACK_TURN) {
            return false
        }
        if (gameStatus == GameStatus.BLACK_TURN && chessMove.moveFromWhitePlayer) {
            return false
        }
        if (gameStatus == GameStatus.WHITE_TURN && !chessMove.moveFromWhitePlayer) {
            return false
        }
        return true
    }
}