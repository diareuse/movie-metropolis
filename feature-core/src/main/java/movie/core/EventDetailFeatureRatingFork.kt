package movie.core

import movie.core.db.dao.MovieRatingDao
import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureRatingFork(
    private val success: EventDetailFeature,
    private val failure: EventDetailFeature,
    private val rating: MovieRatingDao
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        when (rating.isRecent(movie.id)) {
            true -> success.get(movie, result)
            else -> failure.get(movie, result)
        }
    }

}