package navigation

import com.arkivanov.essenty.parcelable.Parcelable

sealed class NavScreen : Parcelable {
    object Main: NavScreen()
    data class GameScreen(val gameId: Long) : NavScreen()
}
