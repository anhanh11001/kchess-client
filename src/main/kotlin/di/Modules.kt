package di

import androidx.compose.ui.window.ApplicationScope
import domain.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ui.game.GameViewModel

val chessModule = module {

}

val gameModule = module {
    factory { GameViewModel(get(), get(), get(), get(), get(), get()) }
}

val apiModule = module {
    factory {
        HttpClient(CIO)
    }
}

val domainModule = module {
    factory { DetermineCorrectPawnMoveUseCase() }
    factory { DetermineCorrectRookMoveUseCase(get()) }
    factory { DetermineCorrectKnightMoveUseCase() }
    factory { DetermineCorrectKingMoveUseCase(get(), get(), get(), get()) }
    factory { DetermineCorrectQueenMoveUseCase(get(), get()) }
    factory { DetermineCorrectBishopMoveUseCase(get()) }
    factory { DetermineCorrectMoveBasedOnGameStatusUseCase() }
    factory { GetGameByGameIdUseCase() }
    factory { GetPlayerByIdUseCase() }
    factory { DetermineValidStraightMoveUseCase() }
    factory { DetermineValidDiagonalMoveUseCase() }
    factory {
        DetermineValidMoveUseCase(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { CheckIfNextMoveAvailableUseCase(get(), get(), get()) }
    factory { FindKingPositionUseCase() }
    factory { DetermineNextMoveUseCase(get(), get(), get()) }
    factory { DetermineNextRandomMoveUseCase(get()) }
    factory { DetermineNextDeepLearningMoveUseCase() }
    factory { DetermineNextSimpleMinimaxMoveUseCase() }
    factory { GetAllPossibleMovesUseCase(get()) }
    factory {
        DetermineKingIsSafeAfterMakingAMoveUseCase(
            get(),
            get(),
            get(),
        )
    }
    factory {
        DetermineIfKingIsValidToCapture(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { UpdateBoardAfterMoveUseCase() }
}


val KOIN_MODULE_LIST = listOf(
    chessModule,
    gameModule,
    apiModule,
    domainModule
)

fun ApplicationScope.setupDependencyInjection() {
    startKoin {
        printLogger()
        modules(KOIN_MODULE_LIST)
    }
}