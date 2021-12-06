package ui.chess

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
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
import androidx.compose.ui.unit.*
import data.chess.BoardRepresentation
import data.chess.ChessMove
import data.chess.ChessPiece
import ui.components.KChessSmallRoundedCorner
import ui.components.convertToDp

const val CHESS_PIECE_RATIO_WITHIN_SQUARE = 0.8f

data class TemporaryMove(
    val chessPiece: ChessPiece,
    val initialPosition: String,
    val endingPosition: String? = null
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChessBoard(
    locationToChessPiece: Map<String, ChessPiece>,
    onNewMoveMade: (ChessMove) -> Unit,
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
        return "${colNames[colIndex]}${rowNames[rowIndex]}"
    }

    // State
    var nextPossibleMove by remember { mutableStateOf<TemporaryMove?>(null) }
    var pawnPromotionMove by remember { mutableStateOf<TemporaryMove?>(null) }
    var pointerLocation by remember { mutableStateOf(Offset(0f, 0f)) }
    var pieceSize by remember { mutableStateOf(0f) }
    var locationMap by remember { mutableStateOf(emptyMap<String, ChessPiece>()) }

    // Set up state after function recall
    val chessBoardPositionMap = locationToChessPiece.toMutableMap()
    nextPossibleMove?.initialPosition?.let { chessBoardPositionMap.remove(it) }
    locationMap = chessBoardPositionMap

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onPointerEvent(PointerEventType.Press) {
                val pressedSquare = determineChessSquareFromPosition(
                    currentEvent.changes[0].position,
                    size
                ) ?: return@onPointerEvent
                val piece = locationMap[pressedSquare]
                if (piece != null) {
                    val newLocationMap = locationMap.toMutableMap()
                    newLocationMap.remove(pressedSquare)
                    locationMap = newLocationMap
                    nextPossibleMove = TemporaryMove(piece, pressedSquare)
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                val releasedSquare = determineChessSquareFromPosition(
                    currentEvent.changes[0].position,
                    size
                ) ?: return@onPointerEvent
                val temporaryMove = nextPossibleMove
                if (temporaryMove != null) {
                    val newLocationMap = locationMap.toMutableMap()
                    newLocationMap[temporaryMove.initialPosition] = temporaryMove.chessPiece
                    locationMap = newLocationMap

                    if (temporaryMove.chessPiece is ChessPiece.Pawn &&
                        (releasedSquare[1].digitToInt() == 1 || releasedSquare[1].digitToInt() == 8)
                    ) {
                        pawnPromotionMove = temporaryMove.copy(endingPosition = releasedSquare)
                    } else {
                        onNewMoveMade(
                            ChessMove(
                                chessPiece = temporaryMove.chessPiece,
                                startingPosition = temporaryMove.initialPosition,
                                endingPosition = releasedSquare
                            )
                        )
                    }

                    nextPossibleMove = null
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
                        val location = "$col$row"
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

        if (pointerLocation.y - pieceSize / 2 >= 0 && pointerLocation.x - pieceSize / 2 >= 0) {
            ChessPieceUI(
                nextPossibleMove?.chessPiece,
                modifier = Modifier.padding(
                    top = convertToDp(pointerLocation.y - pieceSize / 2),
                    start = convertToDp(pointerLocation.x - pieceSize / 2)
                )
                    .width(convertToDp(pieceSize))
                    .height(convertToDp(pieceSize))
            )
        }

        pawnPromotionMove?.let { promotionMove ->
            val promotedPosition = requireNotNull(promotionMove.endingPosition)
            val squareSize = convertToDp(pieceSize / CHESS_PIECE_RATIO_WITHIN_SQUARE)
            val leftPadding = (promotionMove.endingPosition[0] - 'a') * squareSize
            val topPadding = if (promotionMove.endingPosition[1].digitToInt() == 1) {
                squareSize * 4
            } else {
                0.dp
            }
            PromotedPawnPieceSelectionBar(
                isWhite = promotionMove.chessPiece.isWhite,
                onPieceSelected = { promotedPiece ->
                    onNewMoveMade(
                        ChessMove(
                            chessPiece = promotionMove.chessPiece,
                            startingPosition = promotionMove.initialPosition,
                            endingPosition = promotedPosition,
                            promotedPiece = promotedPiece
                        )
                    )
                    pawnPromotionMove = null
                },
                modifier = Modifier
                    .padding(top = topPadding, start = leftPadding)
                    .width(squareSize)
                    .aspectRatio(0.25f)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PromotedPawnPieceSelectionBar(
    isWhite: Boolean,
    onPieceSelected: (ChessPiece) -> Unit,
    modifier: Modifier = Modifier
) {

    val selectableChessPiece = listOf(
        ChessPiece.Queen(isWhite),
        ChessPiece.Rook(isWhite),
        ChessPiece.Knight(isWhite),
        ChessPiece.Bishop(isWhite)
    )

    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        Column {
            selectableChessPiece.forEach { chessPiece ->
                Square(
                    isWhite = isWhite,
                    chessPiece = chessPiece,
                    modifier = Modifier.clickable {
                        onPieceSelected(chessPiece)
                    }
                )
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
    ChessBoard(emptyMap(), {})
}

@Preview
@Composable
fun BoardWithPiecesPreview() {
    ChessBoard(BoardRepresentation.DEFAULT_BOARD_MAP, {})
}