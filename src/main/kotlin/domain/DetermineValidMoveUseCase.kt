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
        chessMove: ChessMove,
        moveSequence: List<ChessMove>
    ): Boolean {
        println("Starting the test")
        if (!determineCorrectMoveBasedOnGameStatusUseCase(gameStatus, chessMove)) {
            println("Failed status")
            return false
        }

        println("Passed status")

        if (boardPosition[chessMove.startingPosition] != chessMove.chessPiece) {
            println("Failed position ${boardPosition[chessMove.startingPosition]} ${chessMove.chessPiece}")
            return false
        }
        println("Passed position")

        val isChessPieceDirectionValid = when (chessMove.chessPiece) {
            is ChessPiece.King -> determineCorrectKingMoveUseCase(boardPosition, chessMove, false, moveSequence)
            is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Rook -> determineCorrectRookMoveUseCase(boardPosition, chessMove)
            is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(
                boardPosition,
                chessMove,
                false,
                moveSequence.lastOrNull()
            )
        }

        if (!isChessPieceDirectionValid) {
            println("Failed correct move")
            return false
        }
        println("Passed correct move")

        val isKingStillSafe = determineKingIsSafeAfterMakingAMoveUseCase(
            chessMove = chessMove,
            boardPosition = boardPosition,
            isWhiteKing = chessMove.chessPiece.isWhite,
            moveSequence = moveSequence
        )
        if (!isKingStillSafe) {
            println("Failed king safety")
            return false
        }
        println("Passed king safety")

        return true
    }
}