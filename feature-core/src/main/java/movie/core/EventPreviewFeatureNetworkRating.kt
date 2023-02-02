package movie.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.MoviePreviewWithRating
import movie.core.model.MoviePreview

class EventPreviewFeatureNetworkRating(
    private val origin: EventPreviewFeature,
    private val detail: EventDetailFeature
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) = coroutineScope {
        var last: Result<List<MoviePreview>> = Result.failure(IllegalStateException())
        origin.get {
            result(it)
            last = it
        }
        val movies = last.getOrNull() ?: return@coroutineScope
        val updated = movies.toMutableList()
        for ((index, movie) in movies.withIndex()) {
            val rating = movie.rating
            if (rating != null && rating > 0) continue
            launch {
                updated[index] = MoviePreviewWithRating(movie, getRating(movie))
                result(Result.success(updated))
            }
        }
    }

    private suspend fun getRating(movie: MoviePreview): Byte? {
        return detail.get(movie).getOrNull()?.rating
    }

}