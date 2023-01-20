package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

interface EventCinemaFeature {

    suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>)

    companion object {

        suspend fun EventCinemaFeature.get(location: Location?): Result<Iterable<Cinema>> {
            var out: Result<Iterable<Cinema>> = Result.failure(IllegalStateException())
            get(location) {
                out = it
            }
            return out
        }

    }

}