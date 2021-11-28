package di

import androidx.compose.ui.window.ApplicationScope
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

val chessModule = module {

}

val gameModule = module {

}

val apiModule = module {
    factory {
        HttpClient(CIO)
    }
}


val KOIN_MODULE_LIST = listOf(
    chessModule,
    gameModule,
    apiModule
)

fun ApplicationScope.setupDependencyInjection() {
    startKoin {
        printLogger()
        modules(KOIN_MODULE_LIST)
    }
}