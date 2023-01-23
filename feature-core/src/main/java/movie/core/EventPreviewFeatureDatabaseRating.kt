package movie.core

import movie.core.adapter.MoviePreviewWithRating
import movie.core.db.dao.MovieRatingDao
import movie.core.model.MoviePreview

class EventPreviewFeatureDatabaseRating(
    private val origin: EventPreviewFeature,
    private val rating: MovieRatingDao
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get(result.thenMap { movies ->
            movies.map { movie ->
                val rating = movie.rating
                if (rating != null && rating > 0) movie
                else MoviePreviewWithRating(movie, getRating(movie))
            }
        })
    }

    private suspend fun getRating(movie: MoviePreview): Byte? {
        return rating.select(movie.id)
    }

}