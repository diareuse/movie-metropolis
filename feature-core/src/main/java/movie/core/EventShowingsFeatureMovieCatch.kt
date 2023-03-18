package movie.core

import java.util.Date

class EventShowingsFeatureMovieCatch(
    private val origin: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie, Recoverable {

    override suspend fun get(date: Date) = wrap { origin.get(date) }

}