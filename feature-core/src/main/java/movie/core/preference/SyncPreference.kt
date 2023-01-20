package movie.core.preference

import java.util.Date
import kotlin.time.Duration

interface SyncPreference {

    var previewUpcoming: Date
    var previewCurrent: Date
    var cinema: Date

    companion object {

        fun Date.isInThreshold(duration: Duration): Boolean {
            return Date().before(this + duration)
        }

        private operator fun Date.plus(duration: Duration): Date {
            return Date(time + duration.inWholeMilliseconds)
        }

    }

}