package data.mock

import data.Player

object MockPlayers {

    val MAGNUS_CARLSEN = Player(
        playerId = 100L,
        name = "Magnus Carlsen",
        elo = 2855,
        imageUrl = "https://pbs.twimg.com/profile_images/1384791850259783681/y9O88Dj8_400x400.jpg",
        nationality = "Norway",
        isBot = false
    )

    val FABIANO_CARUANA = Player(
        playerId = 101L,
        name = "Fabiano Caruana",
        elo = 2791,
        imageUrl = "https://en.chessbase.com/Portals/all/thumbs/085/85469.jpeg",
        nationality = "USA",
        isBot = false
    )

    val BOT_DAVID = Player(
        playerId = 102L,
        name = "Bot David",
        elo = 3000,
        imageUrl = null,
        nationality = "Internet",
        isBot = true
    )
}