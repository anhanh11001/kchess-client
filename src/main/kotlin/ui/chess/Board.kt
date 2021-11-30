package ui.chess

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import data.BoardRepresentation
import data.ChessPiece
import ui.components.KChessSmallRoundedCorner

const val CHESS_PIECE_RATIO_WITHIN_SQUARE = 0.8f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChessBoard(
    locationToChessPiece: Map<String, ChessPiece>,
    modifier: Modifier = Modifier
) {
    val rowNames = "12345678"
    val colNames = "abcdefgh"

    fun determineChessSquareFromPosition(position: Offset, boxSize: IntSize): String? {
        val colIndex = (position.x / (boxSize.height / 8f)).toInt()
        val rowIndex = 7 - (position.y / (boxSize.width / 8f)).toInt()
        if (rowIndex < 0 || rowIndex >= 8 || colIndex < 0 || rowIndex >= 8) {
            return null
        }
        return "${rowNames[rowIndex]}${colNames[colIndex]}"
    }

    var locationMap by remember { mutableStateOf(locationToChessPiece) }
    var movingPiece by remember { mutableStateOf<ChessPiece?>(null) }
    var pointerLocation by remember { mutableStateOf(Offset(0f, 0f)) }
    var pieceSize by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onPointerEvent(PointerEventType.Press) {
                val pressedSquare =
                    determineChessSquareFromPosition(
                        currentEvent.changes[0].position,
                        size
                    ) ?: return@onPointerEvent
                val piece = locationMap[pressedSquare]
                if (piece != null) {
                    val newLocationMap = locationMap.toMutableMap()
                    newLocationMap.remove(pressedSquare)
                    locationMap = newLocationMap
                    movingPiece = piece
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                val releasedSquare = determineChessSquareFromPosition(
                    currentEvent.changes[0].position,
                    size
                ) ?: return@onPointerEvent
                val movedPiece = movingPiece
                if (movedPiece != null) {
                    val newLocationMap = locationMap.toMutableMap()
                    newLocationMap[releasedSquare] = movedPiece
                    locationMap = newLocationMap
                    movingPiece = null
                }
            }
            .onPointerEvent(PointerEventType.Move) {
                pointerLocation = currentEvent.changes[0].position
            }
            .clip(KChessSmallRoundedCorner())
            .onSizeChanged {
                pieceSize = it.width / 8 * CHESS_PIECE_RATIO_WITHIN_SQUARE
            }
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
                        val chessPiece = locationMap.getOrDefault(location, null)
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

        if (pointerLocation.y - pieceSize / 2 >= 0 &&
            pointerLocation.x - pieceSize / 2 >= 0
        ) {
            ChessPieceUI(
                movingPiece,
                modifier = Modifier.padding(
                    top = with(LocalDensity.current) { (pointerLocation.y - pieceSize / 2).toDp() },
                    start = with(LocalDensity.current) { (pointerLocation.x - pieceSize / 2).toDp() })
                    .width(with(LocalDensity.current) { pieceSize.toDp() })
                    .height(with(LocalDensity.current) { pieceSize.toDp() })
            )
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
            ChessPieceUI(chessPiece)
        }
    }
}

@Composable
fun ChessPieceUI(chessPiece: ChessPiece?, modifier: Modifier = Modifier) = when (chessPiece) {
    is ChessPiece.King -> King(chessPiece.isWhite, modifier)
    is ChessPiece.Queen -> Queen(chessPiece.isWhite, modifier)
    is ChessPiece.Bishop -> Bishop(chessPiece.isWhite, modifier)
    is ChessPiece.Knight -> Knight(chessPiece.isWhite, modifier)
    is ChessPiece.Rook -> Rook(chessPiece.isWhite, modifier)
    is ChessPiece.Pawn -> Pawn(chessPiece.isWhite, modifier)
    null -> {
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