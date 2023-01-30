package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

class EventPromoFeatureFold(
    private vararg val options: EventPromoFeature
) : EventPromoFeature, Recoverable {

    override suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>) {
        options.foldCatching { option ->
            option.get(movie) { callback(it.onFailureThrow()) }
        }
    }

}