package movie.core

import movie.core.util.requireNotEmpty
import java.util.Date

class EventShowingsFeatureMovieRequireNotEmpty(
    private val origin: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date) = origin.get(date).map {
        it.requireNotEmpty()
        for ((_, values) in it)
            values.requireNotEmpty()
        it
    }

}