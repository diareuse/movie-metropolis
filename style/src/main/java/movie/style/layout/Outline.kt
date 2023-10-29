package movie.style.layout

import androidx.compose.ui.graphics.*

fun Outline.toPath() = when (this) {
    is Outline.Generic -> path
    is Outline.Rectangle -> Path().apply {
        addRect(bounds)
    }

    is Outline.Rounded -> Path().apply {
        addRoundRect(roundRect)
    }
}