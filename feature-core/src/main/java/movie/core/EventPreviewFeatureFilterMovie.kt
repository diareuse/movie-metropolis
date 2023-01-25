package movie.core

import movie.core.model.MoviePreview
import movie.core.preference.EventPreference

class EventPreviewFeatureFilterMovie(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get(result.map { movies ->
            when (preference.onlyMovies) {
                true -> movies.filter { it.genres.toList().isNotEmpty() }
                else -> movies
            }
        })
    }

}