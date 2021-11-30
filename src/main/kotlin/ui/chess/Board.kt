package ui.chess

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.BoardRepresentation
import data.ChessPiece
import ui.components.KChessSmallRoundedCorner

@Composable
fun ChessBoard(
    locationToChessPiece: Map<String, ChessPiece>,
    modifier: Modifier = Modifier
) {
    val rowNames = "12345678"
    val colNames = "abcdefgh"

    Box(
        modifier = modifier.aspectRatio(1f).clip(KChessSmallRoundedCorner())
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
                            squareLocation = location,
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
    squareLocation: String? = null,
    chessPiece: ChessPiece? = null,
    modifier: Modifier = Modifier
) {
    key(squareLocation) {
        Box(
            modifier = modifier
                .background(if (isWhite) Color.White else Color.DarkGray)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,

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
                Square(isWhite = true, chessPiece = piece, modifier = Modifier.width(100.dp))
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