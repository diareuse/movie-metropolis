package movie.metropolis.app.util

import kotlin.time.Duration

fun Duration.toStringComponents() = toComponents { hours, minutes, _, _ ->
    when {
        hours <= 0 -> "%d".format(minutes)
        else -> "%dh %dm".format(hours, minutes)
    }
}