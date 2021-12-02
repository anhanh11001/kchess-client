package domain

import data.GameStatus
import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineValidMoveUseCase(
    private val determineCorrectMoveBasedOnGameStatusUseCase: DetermineCorrectMoveBasedOnGameStatusUseCase,
    private val determineCorrectQueenMoveUseCase: DetermineCorrectQueenMoveUseCase,
    private val determineCorrectKingMoveUseCase: DetermineCorrectKingMoveUseCase,
    private val determineCorrectBishopMoveUseCase: DetermineCorrectBishopMoveUseCase,
    private val determineCorrectKnightMoveUseCase: DetermineCorrectKnightMoveUseCase,
    private val determineCorrectRookMoveUseCase: DetermineCorrectRookMoveUseCase,
    private val determineCorrectPawnMoveUseCase: DetermineCorrectPawnMoveUseCase,
    private val determineKingIsSafeAfterMakingAMoveUseCase: DetermineKingIsSafeAfterMakingAMoveUseCase
) {

    operator fun invoke(
        gameStatus: GameStatus,
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove
    ): Boolean {
//        if (!determineCorrectMoveBasedOnGameStatusUseCase(gameStatus, chessMove)) {
//            return false
//        }

        if (boardPosition[chessMove.startingPosition] != chessMove.chessPiece) {
            return false
        }
        val isChessPieceDirectionValid = when (chessMove.chessPiece) {
            is ChessPiece.King -> determineCorrectKingMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Rook -> determineCorrectRookMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(boardPosition, chessMove)
        }
        if (!isChessPieceDirectionValid) return false

        val isKingStillSafe = determineKingIsSafeAfterMakingAMoveUseCase(
            chessMove = chessMove,
            boardPosition = boardPosition,
            isWhiteKing = chessMove.chessPiece.isWhite
        )
        if (!isKingStillSafe) return false

        return true
    }
}