package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureFold(
    private vararg val options: EventDetailFeature
) : EventDetailFeature, Recoverable {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        options.foldCatching { option ->
            option.get(movie) { result(it.onFailureThrow()) }
        }
    }

}