package movie.metropolis.app.util

import kotlin.time.Duration

fun Duration.toStringComponents() = toComponents { hours, minutes, _, _ ->
    when {
        hours <= 0 && minutes <= 0 -> ""
        hours <= 0 -> "%dm".format(minutes)
        else -> "%dh %dm".format(hours, minutes)
    }
}