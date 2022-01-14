package domain

abstract class ValidatorGenerator {

    protected val determineCorrectMoveBasedOnGameStatusUseCase = DetermineCorrectMoveBasedOnGameStatusUseCase()
    protected val determineValidStraightMoveUseCase = DetermineValidStraightMoveUseCase()
    protected val determineValidDiagonalMoveUseCase = DetermineValidDiagonalMoveUseCase()
    protected val determineCorrectQueenMoveUseCase = DetermineCorrectQueenMoveUseCase(
        determineValidStraightMoveUseCase,
        determineValidDiagonalMoveUseCase
    )
    protected val determineCorrectBishopMoveUseCase = DetermineCorrectBishopMoveUseCase(determineValidDiagonalMoveUseCase)
    protected val determineCorrectKnightMoveUseCase = DetermineCorrectKnightMoveUseCase()
    protected val determineCorrectRookMoveUseCase = DetermineCorrectRookMoveUseCase(determineValidStraightMoveUseCase)
    protected val determineCorrectKingMoveUseCase = DetermineCorrectKingMoveUseCase(
        determineCorrectQueenMoveUseCase,
        determineCorrectBishopMoveUseCase,
        determineCorrectKnightMoveUseCase,
        determineCorrectRookMoveUseCase
    )
    protected val determineCorrectPawnMoveUseCase = DetermineCorrectPawnMoveUseCase()
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