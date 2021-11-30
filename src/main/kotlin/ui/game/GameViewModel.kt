package ui.game

import data.Game
import data.Player
import domain.GetGameByGameIdUseCase
import domain.GetPlayerByIdUseCase

data class GameUIState(
    val blackPlayer: Player,
    val whitePlayer: Player,
    val game: Game
)

class GameViewModel(
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
    private val getGameByGameIdUseCase: GetGameByGameIdUseCase
) {


}