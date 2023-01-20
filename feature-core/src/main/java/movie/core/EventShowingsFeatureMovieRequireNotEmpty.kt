package movie.core

import movie.core.util.requireNotEmpty
import java.util.Date

class EventShowingsFeatureMovieRequireNotEmpty(
    private val origin: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        origin.get(date) {
            result(it.requireNotEmpty())
        }
    }

}