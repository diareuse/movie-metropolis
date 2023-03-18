package movie.core

import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureFold(
    private vararg val options: EventDetailFeature
) : EventDetailFeature, Recoverable {

    override suspend fun get(movie: Movie): Result<MovieDetail> {
        return options.fold { get(movie) }
    }

}