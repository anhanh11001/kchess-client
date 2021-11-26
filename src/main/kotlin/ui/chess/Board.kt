package ui.chess

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.BoardRepresentation
import data.ChessPiece

@Composable
fun ChessBoard(
    locationToChessPiece: Map<String, ChessPiece>
) {
    val rowNames = "12345678"
    val colNames = "abcdefgh"

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxHeight(0.8f)
    ) {
        Row(modifier = Modifier.matchParentSize()) {
            colNames.forEachIndexed { colIndex, col ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .weight(1f)
                ) {
                    rowNames.reversed().forEachIndexed { rowIndex, row ->
                        val location = "$row$col"
                        val chessPiece = locationToChessPiece.getOrDefault(location, null)
                        val isSquareWhite = (colIndex + rowIndex) % 2 == 0

                        Square(
                            isWhite = isSquareWhite,
                            chessPiece = chessPiece,
                            modifier = Modifier.fillMaxHeight(1f).weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Square(
    isWhite: Boolean,
    chessPiece: ChessPiece? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(if (isWhite) Color.White else Color.DarkGray)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        when (chessPiece) {
            is ChessPiece.King -> King(chessPiece.isWhite)
            is ChessPiece.Queen -> Queen(chessPiece.isWhite)
            is ChessPiece.Bishop -> Bishop(chessPiece.isWhite)
            is ChessPiece.Knight -> Knight(chessPiece.isWhite)
            is ChessPiece.Rook -> Rook(chessPiece.isWhite)
            is ChessPiece.Pawn -> Pawn(chessPiece.isWhite)
            null -> {
            }
        }
    }
}

@Preview
@Composable
fun AllSquaresPreview() {
    val chessPieces = listOf(
        ChessPiece.King(true),
        ChessPiece.Queen(true),
        ChessPiece.Rook(true),
        ChessPiece.Bishop(true),
        ChessPiece.Knight(true),
        ChessPiece.Pawn(true),
        ChessPiece.King(false),
        ChessPiece.Queen(false),
        ChessPiece.Rook(false),
        ChessPiece.Bishop(false),
        ChessPiece.Knight(false),
        ChessPiece.Pawn(false),
    )
    Row {
        for (piece in chessPieces) {
            Column {
                Square(
                    isWhite = false,
                    chessPiece = piece,
                    modifier = Modifier.width(100.dp)
                )
                Square(isWhite = true, chessPiece = piece, Modifier.width(100.dp))
            }
        }

    }
}

@Preview
@Composable
fun EmptyBoardPreview() {
    ChessBoard(emptyMap())
}

@Preview
@Composable
fun BoardWithPiecesPreview() {
    ChessBoard(BoardRepresentation.DEFAULT_BOARD_MAP)
}