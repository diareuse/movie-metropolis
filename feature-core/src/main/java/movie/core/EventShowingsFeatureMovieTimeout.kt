package movie.core

import kotlinx.coroutines.withTimeout
import java.util.Date
import kotlin.time.Duration.Companion.seconds

class EventShowingsFeatureMovieTimeout(
    private val origin: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date) = withTimeout(10.seconds) {
        origin.get(date)
    }

}