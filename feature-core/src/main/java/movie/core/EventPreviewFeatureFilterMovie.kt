package movie.core

import movie.core.preference.EventPreference

class EventPreviewFeatureFilterMovie(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference
) : EventPreviewFeature {

    @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
    override suspend fun get() = origin.get().let { movies ->
        when (preference.onlyMovies) {
            true -> movies.filter { it.genres.count() <= 0 || it.genres !in DisabledEvents }
            else -> movies
        }
    }

    private operator fun Iterable<String>.contains(other: Iterable<String>): Boolean {
        for (it in this)
            if (it in other) return true
        return false
    }

    companion object {

        private val DisabledEvents = listOf("musical", "dance", "concert")

    }

}