package movie.core

import movie.core.preference.EventPreference

class EventPreviewFeatureFilterKeywords(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference
) : EventPreviewFeature {

    override suspend fun get() = origin.get().map { movies ->
        val keywords = preference.keywords
        movies.filter { m -> keywords.none { it in m.name } }
    }

}