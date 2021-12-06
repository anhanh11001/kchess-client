package domain

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import data.GameStatus
import data.chess.ChessMove
import data.chess.ChessPiece
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

abstract class DetermineValidMoveUseCaseTest {

    protected val determineCorrectMoveBasedOnGameStatusUseCase = DetermineCorrectMoveBasedOnGameStatusUseCase()
    private val determineValidStraightMoveUseCase = DetermineValidStraightMoveUseCase()
    private val determineValidDiagonalMoveUseCase = DetermineValidDiagonalMoveUseCase()
    private val determineCorrectQueenMoveUseCase = DetermineCorrectQueenMoveUseCase(
        determineValidStraightMoveUseCase,
        determineValidDiagonalMoveUseCase
    )
    private val determineCorrectBishopMoveUseCase = DetermineCorrectBishopMoveUseCase(determineValidDiagonalMoveUseCase)
    private val determineCorrectKnightMoveUseCase = DetermineCorrectKnightMoveUseCase()
    private val determineCorrectRookMoveUseCase = DetermineCorrectRookMoveUseCase(determineValidStraightMoveUseCase)
    private val determineCorrectKingMoveUseCase = DetermineCorrectKingMoveUseCase(
        determineCorrectQueenMoveUseCase,
        determineCorrectBishopMoveUseCase,
        determineCorrectKnightMoveUseCase,
        determineCorrectRookMoveUseCase
    )
    private val determineCorrectPawnMoveUseCase = DetermineCorrectPawnMoveUseCase()
    protected val determineValidMoveUseCase = DetermineValidMoveUseCase(
        determineCorrectMoveBasedOnGameStatusUseCase,
        determineCorrectQueenMoveUseCase,
        determineCorrectKingMoveUseCase,
        determineCorrectBishopMoveUseCase,
        determineCorrectKnightMoveUseCase,
        determineCorrectRookMoveUseCase,
        determineCorrectPawnMoveUseCase,
        DetermineKingIsSafeAfterMakingAMoveUseCase(
            determineCorrectQueenMoveUseCase,
            determineCorrectKingMoveUseCase,
            determineCorrectBishopMoveUseCase,
            determineCorrectKnightMoveUseCase,
            determineCorrectRookMoveUseCase,
            determineCorrectPawnMoveUseCase,
            UpdateBoardAfterMoveUseCase()
        )
    )
}

@RunWith(TestParameterInjector::class)
class DetermineValidMoveUseCaseByGameStatusTest : DetermineValidMoveUseCaseTest() {

    enum class GameStatusTestCase(
        val chessMove: ChessMove,
        val gameStatus: GameStatus,
        val boardPosition: Map<String, ChessPiece> = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
        val expectedResult: Boolean
    ) {
        BlackPlayInBlackTurn(
            chessMove = ChessMove(ChessPiece.Pawn(false), "a7", "a6"),
            gameStatus = GameStatus.BLACK_TURN,
            expectedResult = true
        ),
        BlackPlayInWhiteTurn(
            chessMove = ChessMove(ChessPiece.Pawn(false), "a7", "a6"),
            gameStatus = GameStatus.WHITE_TURN,
            expectedResult = false
        ),
        WhitePlayInWhiteTurn(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.WHITE_TURN,
            expectedResult = true
        ),
        WhitePlayInBlackTurn(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.BLACK_TURN,
            expectedResult = false
        ),
        PlayInWhiteWonGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.WHITE_WIN,
            expectedResult = false
        ),
        PlayInBlackWonGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.BLACK_WIN,
            expectedResult = false
        ),
        PlayInDrawnGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.DRAW,
            expectedResult = false
        ),
        PlayInNotStartedGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.NOT_STARTED,
            expectedResult = false
        ),
        PlayInAbortedGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3"),
            gameStatus = GameStatus.ABORTED,
            expectedResult = false
        )
    }

    @Test
    fun testValidMoveBasedOnGameStatus(@TestParameter gameStatusTestCase: GameStatusTestCase) {
        val actual = determineCorrectMoveBasedOnGameStatusUseCase(
            gameStatus = gameStatusTestCase.gameStatus,
            chessMove = gameStatusTestCase.chessMove

        )

        Assert.assertEquals(gameStatusTestCase.expectedResult, actual)
    }

}


@RunWith(TestParameterInjector::class)
class DetermineValidMoveUseCaseByChessMoveTest : DetermineValidMoveUseCaseTest() {
    enum class MoveTestCase(
        val chessMove: ChessMove,
        val boardPosition: Map<String, ChessPiece>,
        val expectedResult: Boolean,
        val moveSequence: List<ChessMove> = emptyList()
    ) {
        // Pawn
        WhitePawnMoveUpOneSpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "e2",
                endingPosition = "e3",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        WhitePawnMoveUpTwoSpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "e2",
                endingPosition = "e4",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        BlackPawnMoveUpOneSpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "e7",
                endingPosition = "e6",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        BlackPawnMoveUpTwoSpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "e7",
                endingPosition = "e5",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        WhitePawnCapture(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "g4",
                endingPosition = "h5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        BlackPawnCapture(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "h5",
                endingPosition = "g4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        WhitePawnEnPassant(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "e5",
                endingPosition = "f6"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.Pawn(false),
                    startingPosition = "f7",
                    endingPosition = "f5"
                )
            )
        ),
        BlackPawnEnPassant(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "c4",
                endingPosition = "b3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.Pawn(true),
                    startingPosition = "b2",
                    endingPosition = "b4"
                )
            )
        ),
        WhitePawnPromotion(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "b7",
                endingPosition = "b8",
                promotedPiece = ChessPiece.Queen(true)
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        BlackPawnPromotion(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "a2",
                endingPosition = "a1",
                promotedPiece = ChessPiece.Queen(false)
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedPawnMoveUpWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "f2",
                endingPosition = "f4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false
        ),
        FailedPawnCaptureEmptySpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "e2",
                endingPosition = "d3",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedPawnMoveThatThreatenTheKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(false),
                startingPosition = "d5",
                endingPosition = "d4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = false
        ),
        FailedPawnMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Pawn(true),
                startingPosition = "f2",
                endingPosition = "f3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false
        ),

        // Rook
        RookLeftMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(true),
                startingPosition = "d1",
                endingPosition = "a1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        RookRightMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(false),
                startingPosition = "a8",
                endingPosition = "b8"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        RookUpMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(true),
                startingPosition = "h1",
                endingPosition = "h3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        RookDownMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(false),
                startingPosition = "h8",
                endingPosition = "h7"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedRookVerticalWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(true),
                startingPosition = "a1",
                endingPosition = "a4",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedRookHorizontalWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(true),
                startingPosition = "h6",
                endingPosition = "b6"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = false
        ),
        FailedRookMoveThatThreatenTheKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(false),
                startingPosition = "f6",
                endingPosition = "f8"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = false
        ),
        FailedRookMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Rook(true),
                startingPosition = "a1",
                endingPosition = "a2",
                promotedPiece = null
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),

        // King
        KingMoveUp(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "d2",
                endingPosition = "d3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = true
        ),
        KingMoveDown(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "d2",
                endingPosition = "d1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = true
        ),
        KingMoveLeft(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "d2",
                endingPosition = "c2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = true
        ),
        KingMoveRight(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "f1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        KingMoveDiagonal(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(false),
                startingPosition = "e8",
                endingPosition = "f7"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedKingMoveWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "e2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedKingMoveToCheckMateSpace(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "f2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = false
        ),
        QueenSideCastle(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "c1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = true
        ),
        KingSideCastle(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "g1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedKingSideCastleForMovedRook(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "g1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.Rook(true),
                    startingPosition = "h1",
                    endingPosition = "h2"
                ),
                ChessMove(
                    chessPiece = ChessPiece.Rook(true),
                    startingPosition = "h2",
                    endingPosition = "h1"
                ),
            )
        ),
        FailedKingSideCastleForMovedKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "g1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.King(true),
                    startingPosition = "e1",
                    endingPosition = "f1"
                ),
                ChessMove(
                    chessPiece = ChessPiece.King(true),
                    startingPosition = "f1",
                    endingPosition = "e1"
                ),
            )
        ),
        FailedQueenSideCastleForMovedRook(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "c1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = false,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.Rook(true),
                    startingPosition = "a1",
                    endingPosition = "a2"
                ),
                ChessMove(
                    chessPiece = ChessPiece.Rook(true),
                    startingPosition = "a2",
                    endingPosition = "a1"
                ),
            )
        ),
        FailedQueenSideCastleForMovedKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),
                startingPosition = "e1",
                endingPosition = "c1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = false,
            moveSequence = listOf(
                ChessMove(
                    chessPiece = ChessPiece.King(true),
                    startingPosition = "e1",
                    endingPosition = "d1"
                ),
                ChessMove(
                    chessPiece = ChessPiece.King(true),
                    startingPosition = "d1",
                    endingPosition = "e1"
                ),
            )
        ),
        FailedKingSideCastleForSquaresAttacked(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(false),
                startingPosition = "e1",
                endingPosition = "g1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = false
        ),
        FailedQueenSideCastleForSquaresAttacked(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),

                startingPosition = "e1",
                endingPosition = "c1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_FOUR,
            expectedResult = false
        ),
        FailedKingMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.King(true),

                startingPosition = "e1",
                endingPosition = "e2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_FOUR,
            expectedResult = false
        ),

        // Queen
        QueenLeftMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(false),
                startingPosition = "d8",
                endingPosition = "b8"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        QueenRightMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "b5",
                endingPosition = "h5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_THREE,
            expectedResult = true
        ),
        QueenUpMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d2",
                endingPosition = "d3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        QueenDownMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "b3",
                endingPosition = "b1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = true
        ),
        QueenDiagonalMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(false),
                startingPosition = "d8",
                endingPosition = "a5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedQueenVerticalWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d1",
                endingPosition = "d4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedQueenHorizontalWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d2",
                endingPosition = "b2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false
        ),
        FailedQueenDiagonalWithBlocker(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d2",
                endingPosition = "f4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false
        ),
        FailedQueenMoveThatThreatenTheKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d1",
                endingPosition = "g4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedQueenMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Queen(true),
                startingPosition = "d1",
                endingPosition = "d2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),

        // Knight
        KnightTopLeftMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(true),
                startingPosition = "g1",
                endingPosition = "f3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        KnightTopRightMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(true),
                startingPosition = "g1",
                endingPosition = "h3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = true
        ),
        KnightBottomLeftMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(false),
                startingPosition = "c6",
                endingPosition = "a5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        KnightBottomRightMove(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(false),
                startingPosition = "c6",
                endingPosition = "e5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedKnightMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(false),
                startingPosition = "h6",
                endingPosition = "f5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = false
        ),
        FailedKnightMoveThatThreatenTheKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Knight(false),
                startingPosition = "f5",
                endingPosition = "g3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = false
        ),

        // Bishop
        BishopTopRightDiagonal(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(true),
                startingPosition = "e3",
                endingPosition = "f4"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        BishopTopLeftDiagonal(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(false),
                startingPosition = "d7",
                endingPosition = "c8"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        BishopBottomRightDiagonal(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(true),
                startingPosition = "e2",
                endingPosition = "f1"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        BishopBottomLeftDiagonal(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(false),
                startingPosition = "g7",
                endingPosition = "e5"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_ONE,
            expectedResult = true
        ),
        FailedBishopBlocked(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(true),
                startingPosition = "c1",
                endingPosition = "e3"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedBishopMoveToItsTeamPiece(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(true),
                startingPosition = "c1",
                endingPosition = "b2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            expectedResult = false
        ),
        FailedBishopMoveThatThreatenTheKing(
            chessMove = ChessMove(
                chessPiece = ChessPiece.Bishop(false),
                startingPosition = "e5",
                endingPosition = "h2"
            ),
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_TWO,
            expectedResult = false
        )
    }

    @Test
    fun testValidChessMoveBasedOnPiece(@TestParameter moveTestCase: MoveTestCase) {
        val actual = determineValidMoveUseCase(
            gameStatus = if (moveTestCase.chessMove.chessPiece.isWhite) {
                GameStatus.WHITE_TURN
            } else {
                GameStatus.BLACK_TURN
            },
            boardPosition = moveTestCase.boardPosition,
            chessMove = moveTestCase.chessMove,
            moveSequence = moveTestCase.moveSequence
        )
        Assert.assertEquals(moveTestCase.expectedResult, actual)
    }
}