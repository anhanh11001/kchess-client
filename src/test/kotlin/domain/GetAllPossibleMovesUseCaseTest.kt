package domain

import org.junit.Assert
import org.junit.Test

class GetAllPossibleMovesUseCaseTest : ValidatorGenerator() {

    private val getAllPossibleMovesUseCase = GetAllPossibleMovesUseCase(determineValidMoveUseCase)

    @Test
    fun testCorrectNumberOfOpeningMoves() {
        val expected = 20
        val actual = getAllPossibleMovesUseCase(
            isWhiteTurn = true,
            boardPosition = ChessBoardTestData.CHESS_TEST_BOARD_OPENING,
            pastMoveSequences = emptyList()
        ).size
        Assert.assertEquals(expected, actual)
    }
}