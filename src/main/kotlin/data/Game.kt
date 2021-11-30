package data

data class Game(
    val gameId: Long,
    val blackPlayerId: Long,
    val whitePlayerId: Long,
    val moveSequence: String,
    val gameStatus: GameStatus,
    val timeLimit: Long
)

enum class GameStatus {
    BLACK_WIN,
    WHITE_WIN,
    DRAW,
    NOT_STARTED,
    BLACK_TURN,
    WHITE_TURN,
    ABORTED
}