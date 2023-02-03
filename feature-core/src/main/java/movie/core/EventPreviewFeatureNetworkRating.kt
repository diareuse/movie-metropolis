package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.MoviePreviewWithRating
import movie.core.model.MoviePreview

class EventPreviewFeatureNetworkRating(
    private val origin: EventPreviewFeature,
    private val detail: EventDetailFeature
) : EventPreviewFeature {

    private val ratings = mutableMapOf<String, Byte?>()

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) = coroutineScope {
        origin.get(result.thenParallelize(this) { movie ->
            val rating = movie.rating
            if (rating != null && rating > 0) movie
            else MoviePreviewWithRating(movie, getRating(movie))
        })
    }

    private suspend fun getRating(movie: MoviePreview): Byte? {
        return ratings.getOrPut(movie.id) {
            detail.get(movie).getOrNull()?.rating
        }
    }

}