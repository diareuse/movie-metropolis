package movie.core.util

import java.util.Calendar
import java.util.Date

val Date.dayStart
    get() = Calendar.getInstance().let {
        it.time = this
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
        it.time
    }

val Date.dayEnd
    get() = Calendar.getInstance().let {
        it.time = dayStart
        it.add(Calendar.DAY_OF_YEAR, 1)
        it.add(Calendar.MILLISECOND, -1)
        it.time
    }