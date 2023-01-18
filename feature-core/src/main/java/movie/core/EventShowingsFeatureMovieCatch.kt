package movie.core

import java.util.Date

class EventShowingsFeatureMovieCatch(
    private val origin: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        runCatchingResult(result) {
            origin.get(date, it)
        }
    }

}