package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun convertToDp(value: Int): Dp {
    return with(LocalDensity.current) {
        value.toDp()
    }
}

@Composable
fun convertToDp(value: Float): Dp {
    return with(LocalDensity.current) {
        value.toDp()
    }
}