package ui.chess

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.svgResource
import java.io.File

@Composable
fun King(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/king-white.svg"
    } else {
        "icons/king-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "King piece"
    )
}

@Composable
fun Queen(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/queen-white.svg"
    } else {
        "icons/queen-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "Queen piece"
    )
}

@Composable
fun Knight(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/knight-white.svg"
    } else {
        "icons/knight-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "Knight piece"
    )

}

@Composable
fun Bishop(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/bishop-white.svg"
    } else {
        "icons/bishop-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "Bishop piece"
    )
}

@Composable
fun Rook(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/rook-white.svg"
    } else {
        "icons/rook-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "Rook piece"
    )
}

@Composable
fun Pawn(isWhite: Boolean = true) {
    val svgPath = if (isWhite) {
        "icons/pawn-white.svg"
    } else {
        "icons/pawn-black.svg"
    }
    Image(
        painter = painterResource(svgPath),
        contentDescription = "Pawn piece"
    )
}

@Preview
@Composable
fun BlackPieces() {
    Row {
        King(isWhite = false)
        Queen(isWhite = false)
        Bishop(isWhite = false)
        Rook(isWhite = false)
        Knight(isWhite = false)
        Pawn(isWhite = false)
    }
}

@Preview
@Composable
fun WhitePieces() {
    Row {
        King(isWhite = true)
        Queen(isWhite = true)
        Bishop(isWhite = true)
        Rook(isWhite = true)
        Knight(isWhite = true)
        Pawn(isWhite = true)
    }
}