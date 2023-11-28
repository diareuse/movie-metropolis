package movie.core.preference

import java.util.Date
import kotlin.time.Duration

interface SyncPreference {

    var previewUpcoming: Date
    var previewCurrent: Date
    var cinema: Date
    var booking: Date

    companion object {

        fun Date.isInThreshold(duration: Duration): Boolean {
            return Date(time + duration.inWholeMilliseconds).before(Date())
        }

    }

}