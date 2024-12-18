package movie.style.util

import androidx.compose.ui.unit.*

val Int.pc get() = 12.dp * this
val Float.pc get() = 12.dp * this
val Double.pc get() = (12 * this).dp