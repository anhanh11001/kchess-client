package di

import androidx.compose.ui.window.ApplicationScope
import org.koin.core.context.startKoin
import org.koin.dsl.module

val chessModule = module {

}

val gameModule = module {

}


val KOIN_MODULE_LIST = listOf(
    chessModule,
    gameModule
)

fun ApplicationScope.setupDependencyInjection() {
    startKoin {
        printLogger()
        modules(KOIN_MODULE_LIST)
    }
}