package domain

import data.chess.ChessMove
import data.chess.ChessPiece

class DetermineIfKingIsValidToCapture(
    private val determineCorrectQueenMoveUseCase: DetermineCorrectQueenMoveUseCase,
    private val determineCorrectKingMoveUseCase: DetermineCorrectKingMoveUseCase,
    private val determineCorrectBishopMoveUseCase: DetermineCorrectBishopMoveUseCase,
    private val determineCorrectKnightMoveUseCase: DetermineCorrectKnightMoveUseCase,
    private val determineCorrectRookMoveUseCase: DetermineCorrectRookMoveUseCase,
    private val determineCorrectPawnMoveUseCase: DetermineCorrectPawnMoveUseCase,
) {

    operator fun invoke(
        captureTheKingMove: ChessMove,
        boardPosition: Map<String, ChessPiece>,
        moveSequence: List<ChessMove>
    ): Boolean = when (captureTheKingMove.chessPiece) {
        is ChessPiece.Pawn -> determineCorrectPawnMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove,
            checkForCapturingInPlaceOnly = true,
            opponentLastPlayedMove = moveSequence.lastOrNull()
        )
        is ChessPiece.Queen -> determineCorrectQueenMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove
        )
        is ChessPiece.King -> determineCorrectKingMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove,
            checkForCapturingOnly = true,
            moveSequence = moveSequence
        )
        is ChessPiece.Rook -> determineCorrectRookMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove
        )
        is ChessPiece.Bishop -> determineCorrectBishopMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove
        )
        is ChessPiece.Knight -> determineCorrectKnightMoveUseCase(
            boardPosition = boardPosition,
            chessMove = captureTheKingMove
        )
    }
}