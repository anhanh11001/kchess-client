package domain

import data.chess.ChessMove
import data.chess.ChessPiece
import kotlin.math.abs

class UpdateBoardAfterMoveUseCase {

    operator fun invoke(
        chessMove: ChessMove,
        boardPosition: Map<String, ChessPiece>
    ): Map<String, ChessPiece> {
        val adjustedBoardPosition = boardPosition.toMutableMap()
        when (val chessPiece = chessMove.chessPiece) {
            is ChessPiece.Pawn -> when {
                // Pawn Promotion
                (chessPiece.isWhite && chessMove.endingPosition[1].digitToInt() == 8 && chessMove.promotedPiece != null) ||
                        (!chessPiece.isWhite && chessMove.endingPosition[1].digitToInt() == 1 && chessMove.promotedPiece != null) -> {
                    adjustedBoardPosition.remove(chessMove.startingPosition)
                    adjustedBoardPosition.remove(chessMove.endingPosition)
                    adjustedBoardPosition[chessMove.endingPosition] = chessMove.promotedPiece
                    return adjustedBoardPosition
                }

                // EnPassant
                (abs(chessMove.startingPosition[0] - chessMove.endingPosition[0]) == 1 &&
                        abs(chessMove.startingPosition[1] - chessMove.endingPosition[1]) == 1 &&
                        adjustedBoardPosition[chessMove.endingPosition] == null) -> {
                    val capturePawnPosition = "${chessMove.endingPosition[0]}${chessMove.startingPosition[1]}"
                    adjustedBoardPosition.remove(capturePawnPosition)

                    adjustedBoardPosition.remove(chessMove.startingPosition)
                    adjustedBoardPosition.remove(chessMove.endingPosition)
                    adjustedBoardPosition[chessMove.endingPosition] = chessMove.chessPiece
                    return adjustedBoardPosition
                }
            }
            is ChessPiece.King -> when {
                chessPiece.isWhite && chessMove.startingPosition == "e1" && chessMove.endingPosition == "g1" -> {
                    adjustedBoardPosition["g1"] = requireNotNull(adjustedBoardPosition["e1"])
                    adjustedBoardPosition["f1"] = requireNotNull(adjustedBoardPosition["h1"])
                    adjustedBoardPosition.remove("h1")
                    adjustedBoardPosition.remove("e1")
                    return adjustedBoardPosition
                }
                chessPiece.isWhite && chessMove.startingPosition == "e1" && chessMove.endingPosition == "c1" -> {
                    adjustedBoardPosition["c1"] = requireNotNull(adjustedBoardPosition["e1"])
                    adjustedBoardPosition["d1"] = requireNotNull(adjustedBoardPosition["a1"])
                    adjustedBoardPosition.remove("a1")
                    adjustedBoardPosition.remove("e1")
                    return adjustedBoardPosition
                }
                !chessPiece.isWhite && chessMove.startingPosition == "e8" && chessMove.endingPosition == "g8" -> {
                    adjustedBoardPosition["g8"] = requireNotNull(adjustedBoardPosition["e8"])
                    adjustedBoardPosition["f8"] = requireNotNull(adjustedBoardPosition["h8"])
                    adjustedBoardPosition.remove("h8")
                    adjustedBoardPosition.remove("e8")
                    return adjustedBoardPosition
                }
                !chessPiece.isWhite && chessMove.startingPosition == "e8" && chessMove.endingPosition == "c8" -> {
                    adjustedBoardPosition["c8"] = requireNotNull(adjustedBoardPosition["e8"])
                    adjustedBoardPosition["d8"] = requireNotNull(adjustedBoardPosition["a8"])
                    adjustedBoardPosition.remove("a8")
                    adjustedBoardPosition.remove("e8")
                    return adjustedBoardPosition
                }
            }
            else -> {
            }
        }

        adjustedBoardPosition.remove(chessMove.startingPosition)
        adjustedBoardPosition.remove(chessMove.endingPosition)
        adjustedBoardPosition[chessMove.endingPosition] = chessMove.chessPiece
        return adjustedBoardPosition
    }
}