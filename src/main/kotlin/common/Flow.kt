package common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun tickerFlow(
    periodInMillis: Long,
    initialDelayInMillis: Long = 0
) = flow {
    delay(initialDelayInMillis)
    while (true) {
        emit(Unit)
        delay(periodInMillis)
    }
}