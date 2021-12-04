package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class DetermineCorrectKingMoveUseCase(
    private val determineCorrectQueenMoveUseCase: DetermineCorrectQueenMoveUseCase,
    private val determineCorrectBishopMoveUseCase: DetermineCorrectBishopMoveUseCase,
    private val determineCorrectKnightMoveUseCase: DetermineCorrectKnightMoveUseCase,
    private val determineCorrectRookMoveUseCase: DetermineCorrectRookMoveUseCase
) {

    operator fun invoke(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove,
        checkForAttackingMoveOnly: Boolean = false,
        moveSequence: List<ChessMove>
    ): Boolean {
        val startingPosition = chessMove.startingPosition
        val endingPosition = chessMove.endingPosition

        if (!checkForAttackingMoveOnly && determineValidCastle(boardPosition, chessMove, moveSequence)) {
            return true
        }

        val colDiff = abs(startingPosition[0] - endingPosition[0])
        val rowDiff = abs(startingPosition[1] - endingPosition[1])

        if ((colDiff > 1 || rowDiff > 1) || (colDiff == 0 && rowDiff == 0)) return false

        val pieceInEndingPosition = boardPosition[endingPosition]
        return if (checkForAttackingMoveOnly) {
            pieceInEndingPosition != null && pieceInEndingPosition.isWhite != chessMove.chessPiece.isWhite
        } else {
            pieceInEndingPosition == null || (pieceInEndingPosition.isWhite != chessMove.chessPiece.isWhite)
        }
    }

    private fun determineValidCastle(
        boardPosition: Map<String, ChessPiece>,
        chessMove: ChessMove,
        moveSequence: List<ChessMove>
    ): Boolean {
        val validCastleMoveList = listOf(
            ValidCastleMove(
                isWhiteKing = true,
                kingStartingPosition = "e1",
                kingEndingPosition = "g1",
                rookStartingPosition = "h1",
                rookEndingPosition = "f1",
                notAttackedSquares = listOf("e1", "f1", "g1"),
                emptySquares = listOf("f1", "g1"),
                threatenPawnPosition = setOf("d2", "e2", "f2", "g2", "h2")
            ),
            ValidCastleMove(
                isWhiteKing = true,
                kingStartingPosition = "e1",
                kingEndingPosition = "c1",
                rookStartingPosition = "a1",
                rookEndingPosition = "d1",
                notAttackedSquares = listOf("e1", "d1", "c1"),
                emptySquares = listOf("d1", "c1", "b1"),
                threatenPawnPosition = setOf("a2", "b2", "c2", "d2", "e2", "f2")
            ),
            ValidCastleMove(
                isWhiteKing = false,
                kingStartingPosition = "e8",
                kingEndingPosition = "g8",
                rookStartingPosition = "h8",
                rookEndingPosition = "f8",
                notAttackedSquares = listOf("e8", "f8", "g8"),
                emptySquares = listOf("f8", "g8"),
                threatenPawnPosition = setOf("d7", "e7", "f7", "g7", "h7")
            ),
            ValidCastleMove(
                isWhiteKing = false,
                kingStartingPosition = "e8",
                kingEndingPosition = "c8",
                rookStartingPosition = "a8",
                rookEndingPosition = "d8",
                notAttackedSquares = listOf("e8", "d8", "c8"),
                emptySquares = listOf("d8", "c8", "b8"),
                threatenPawnPosition = setOf("a7", "b7", "c7", "d7", "e7", "f7")
            )
        )

        for (validCastleMove in validCastleMoveList) {
            if (chessMove.chessPiece.isWhite == validCastleMove.isWhiteKing &&
                chessMove.startingPosition == validCastleMove.kingStartingPosition &&
                chessMove.endingPosition == validCastleMove.kingEndingPosition
            ) {
                // The King should not move before
                if (moveSequence.find {
                        it.endingPosition == validCastleMove.kingStartingPosition ||
                                it.startingPosition == validCastleMove.kingStartingPosition
                    } != null
                ) continue

                // The Rook cannot not have moved before
                val rook = boardPosition[validCastleMove.rookStartingPosition]
                if (rook == null || rook.isWhite != validCastleMove.isWhiteKing) return false
                if (moveSequence.find {
                        it.endingPosition == validCastleMove.rookStartingPosition ||
                                it.startingPosition == validCastleMove.rookStartingPosition
                    } != null
                ) continue
                // Squares between the King and Rook must be unoccupied
                for (square in validCastleMove.emptySquares) {
                    if (boardPosition[square] != null) return false
                }

                // The King cannot be in Check
                // The squares the King has to go over when castling cannot be under attack
                for (square in validCastleMove.notAttackedSquares) {
                    if (isAttacked(
                            boardPosition,
                            square,
                            validCastleMove.threatenPawnPosition,
                            !validCastleMove.isWhiteKing
                        )
                    ) continue
                }

                return true
            }
        }
        return false
    }

    private fun isAttacked(
        boardPosition: Map<String, ChessPiece>,
        attackedSquare: String,
        threatenPawnPosition: Set<String>,
        isWhiteMove: Boolean
    ): Boolean {
        for (position in boardPosition.keys) {
            val piece = boardPosition[position]
            if (piece == null || piece.isWhite != isWhiteMove) continue

            val move = ChessMove(
                chessPiece = piece,
                startingPosition = position,
                endingPosition = attackedSquare,
                promotedPiece = null
            )

            when (piece) {
                is ChessPiece.King -> {
                    val isOpponentKingAttacking = invoke(
                        boardPosition = boardPosition,
                        move,
                        checkForAttackingMoveOnly = false,
                        moveSequence = emptyList()
                    )
                    if (isOpponentKingAttacking) return true
                }
                is ChessPiece.Queen -> {
                    if (determineCorrectQueenMoveUseCase(boardPosition, move)) return true
                }
                is ChessPiece.Knight -> {
                    if (determineCorrectKnightMoveUseCase(boardPosition, move)) return true
                }
                is ChessPiece.Bishop -> {
                    if (determineCorrectBishopMoveUseCase(boardPosition, move)) return true
                }
                is ChessPiece.Rook -> {
                    if (determineCorrectRookMoveUseCase(boardPosition, move)) return true
                }
                is ChessPiece.Pawn -> {
                    if (position in threatenPawnPosition) return true
                }
            }
        }

        return false
    }
}


data class ValidCastleMove(
    val isWhiteKing: Boolean,
    val kingStartingPosition: String,
    val kingEndingPosition: String,
    val rookStartingPosition: String,
    val rookEndingPosition: String,
    val notAttackedSquares: List<String>,
    val emptySquares: List<String>,
    val threatenPawnPosition: Set<String>
)