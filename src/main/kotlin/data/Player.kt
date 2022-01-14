package data

import chessbot.BotStrategy

data class Player(
    val playerId: Long,
    val name: String,
    val elo: Long,
    val imageUrl: String? = null,
    val nationality: String,
    val isBot: Boolean,
    val botStrategy: BotStrategy = BotStrategy.NOT_BOT
)