package movie.core

import movie.core.preference.EventPreference

class EventPreviewFeatureFilterMovie(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference
) : EventPreviewFeature {

    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    override suspend fun get() = origin.get().map { movies ->
        when (preference.onlyMovies) {
            true -> movies.filter { it.genres.count() > 0 && "musical" !in it.genres }
            else -> movies
        }
    }

}