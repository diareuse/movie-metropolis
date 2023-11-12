package movie.core

import movie.core.model.MoviePreview

class EventPreviewFeatureSort(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(): Result<Sequence<MoviePreview>> {
        return origin.get().map { items ->
            items.sortedByDescending { it.screeningFrom.time }
        }
    }

}