package movie.core

import kotlinx.coroutines.withTimeout
import java.util.Date
import kotlin.time.Duration.Companion.seconds

class EventShowingsFeatureCinemaTimeout(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date) = withTimeout(5.seconds) {
        origin.get(date)
    }

}