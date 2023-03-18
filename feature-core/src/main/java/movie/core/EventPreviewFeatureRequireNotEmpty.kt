package movie.core

import movie.core.model.MoviePreview
import movie.core.util.requireNotEmpty

class EventPreviewFeatureRequireNotEmpty(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(): Result<Sequence<MoviePreview>> {
        return origin.get().mapCatching { it.requireNotEmpty() }
    }

}