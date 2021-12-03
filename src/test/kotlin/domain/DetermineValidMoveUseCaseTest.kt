package domain

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import data.GameStatus
import data.chess.ChessMove
import data.chess.ChessPiece
import data.chess.MoveType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

abstract class DetermineValidMoveUseCaseTest {

    protected val determineCorrectMoveBasedOnGameStatusUseCase = DetermineCorrectMoveBasedOnGameStatusUseCase()
    private val determineValidStraightMoveUseCase = DetermineValidStraightMoveUseCase()
    private val determineValidDiagonalMoveUseCase = DetermineValidDiagonalMoveUseCase()
    protected val determineValidMoveUseCase = DetermineValidMoveUseCase(
        determineCorrectMoveBasedOnGameStatusUseCase,
        DetermineCorrectQueenMoveUseCase(determineValidStraightMoveUseCase, determineValidDiagonalMoveUseCase),
        DetermineCorrectKingMoveUseCase(),
        DetermineCorrectBishopMoveUseCase(determineValidDiagonalMoveUseCase),
        DetermineCorrectKnightMoveUseCase(),
        DetermineCorrectRookMoveUseCase(determineValidStraightMoveUseCase),
        DetermineCorrectPawnMoveUseCase(),
        DetermineKingIsSafeAfterMakingAMoveUseCase(
            DetermineCorrectQueenMoveUseCase(determineValidStraightMoveUseCase, determineValidDiagonalMoveUseCase),
            DetermineCorrectKingMoveUseCase(),
            DetermineCorrectBishopMoveUseCase(determineValidDiagonalMoveUseCase),
            DetermineCorrectKnightMoveUseCase(),
            DetermineCorrectRookMoveUseCase(determineValidStraightMoveUseCase),
            DetermineCorrectPawnMoveUseCase()
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
            chessMove = ChessMove(ChessPiece.Pawn(false), "a7", "a6", MoveType.Normal),
            gameStatus = GameStatus.BLACK_TURN,
            expectedResult = true
        ),
        BlackPlayInWhiteTurn(
            chessMove = ChessMove(ChessPiece.Pawn(false), "a7", "a6", MoveType.Normal),
            gameStatus = GameStatus.WHITE_TURN,
            expectedResult = false
        ),
        WhitePlayInWhiteTurn(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.WHITE_TURN,
            expectedResult = true
        ),
        WhitePlayInBlackTurn(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.BLACK_TURN,
            expectedResult = false
        ),
        PlayInWhiteWonGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.WHITE_WIN,
            expectedResult = false
        ),
        PlayInBlackWonGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.BLACK_WIN,
            expectedResult = false
        ),
        PlayInDrawnGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.DRAW,
            expectedResult = false
        ),
        PlayInNotStartedGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
            gameStatus = GameStatus.NOT_STARTED,
            expectedResult = false
        ),
        PlayInAbortedGame(
            chessMove = ChessMove(ChessPiece.King(true), "a2", "a3", MoveType.Normal),
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
        val expectedResult: Boolean
    ) {
        // Pawn
        WhitePawnMoveUpOneSpace(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        WhitePawnMoveUpTwoSpace(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BlackPawnMoveUpOneSpace(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BlackPawnMoveUpTwoSpace(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        WhitePawnCapture(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BlackPawnCapture(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        WhitePawnEnPassant(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BlackPawnEnPassant(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        WhitePawnPromotion(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BlackPawnPromotion(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedPawnMoveUpWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedPawnCaptureEmptySpace(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedPawnMoveThatThreatenTheKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedPawnMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),

        // Rook
        RookLeftMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        RookRightMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        RookUpMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        RookDownMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedRookVerticalWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedRookHorizontalWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedRookMoveThatThreatenTheKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedRookMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),

        // King
        KingMoveUp(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KingMoveDown(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KingMoveLeft(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KingMoveRight(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KingMoveDiagonal(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedKingMoveWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedKingMoveToCheckMateSpace(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        QueenSideCastle(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KingSideCastle(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedKingSideCastleForMovedRook(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedKingSideCastleForMovedKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenSideCastleForMovedRook(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenSideCastleForMovedKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedKingSideCastleForSquaresAttacked(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenSideCastleForSquaresAttacked(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedKingMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),

        // Queen
        QueenLeftMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        QueenRightMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        QueenUpMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        QueenDownMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        QueenDiagonalMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedQueenVerticalWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenHorizontalWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenDiagonalWithBlocker(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenMoveThatThreatenTheKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedQueenMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),

        // Knight
        KnightTopLeftMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KnightTopRightMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KnightBottomLeftMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        KnightBottomRightMove(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedKnightMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedKnightMoveThatThreatenTheKing(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),

        // Bishop
        BishopTopRightDiagonal(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BishopTopLeftDiagonal(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BishopBottomRightDiagonal(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        BishopBottomLeftDiagonal(
            chessMove =,
            boardPosition =,
            expectedResult = true
        ),
        FailedBishopBlocked(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedBishopMoveToItsTeamPiece(
            chessMove =,
            boardPosition =,
            expectedResult = false
        ),
        FailedBishopMoveThatThreatenTheKing(
            chessMove =,
            boardPosition =,
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
            chessMove = moveTestCase.chessMove
        )
        Assert.assertEquals(moveTestCase.expectedResult, actual)
    }
}