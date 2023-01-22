package movie.core

import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.MoviePreviewWithRating
import movie.core.model.MoviePreview

class EventPreviewFeatureRating(
    private val origin: EventPreviewFeature,
    private val detail: EventDetailFeature
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get {
            result(it)
            val output = it.map { movies ->
                movies.map { movie ->
                    val rating = movie.rating
                    if (rating != null && rating > 0) movie
                    else MoviePreviewWithRating(movie, getRating(movie))
                }
            }
            result(output)
        }
    }

    private suspend fun getRating(movie: MoviePreview): Byte? {
        return detail.get(movie).getOrNull()?.rating
    }

}