package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.*

private class ExtendedPaddingValues(
    private val origin: PaddingValues,
    private val other: PaddingValues
) : PaddingValues {

    override fun calculateBottomPadding() =
        origin.calculateBottomPadding() + other.calculateBottomPadding()

    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        origin.calculateLeftPadding(layoutDirection) + other.calculateLeftPadding(layoutDirection)

    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        origin.calculateRightPadding(layoutDirection) + other.calculateRightPadding(layoutDirection)

    override fun calculateTopPadding() =
        origin.calculateTopPadding() + other.calculateTopPadding()

}

operator fun PaddingValues.plus(
    other: PaddingValues
): PaddingValues = ExtendedPaddingValues(
    origin = this,
    other = other
)

fun PaddingValues.plus(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
) = this + PaddingValues(
    start = start,
    top = top,
    end = end,
    bottom = bottom
)

fun PaddingValues.plus(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
) = this + PaddingValues(
    horizontal = horizontal,
    vertical = vertical
)

fun PaddingValues.plus(all: Dp) = this + PaddingValues(all)