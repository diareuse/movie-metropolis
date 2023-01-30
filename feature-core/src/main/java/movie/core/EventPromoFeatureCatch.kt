package movie.core

import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

class EventPromoFeatureCatch(
    private val origin: EventPromoFeature
) : EventPromoFeature, Recoverable {

    override suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>) {
        runCatchingResult(callback) {
            origin.get(movie, it)
        }
    }

}