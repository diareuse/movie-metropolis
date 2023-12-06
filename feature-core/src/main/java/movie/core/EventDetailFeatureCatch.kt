package movie.core

import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureCatch(
    private val origin: EventDetailFeature
) : EventDetailFeature, Recoverable {

    override suspend fun get(movie: Movie): MovieDetail {
        return origin.get(movie)
    }

}