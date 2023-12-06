package movie.core

import movie.core.model.MoviePreview

class EventPreviewFeatureSort(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(): Sequence<MoviePreview> {
        return origin.get().let { items ->
            items.sortedByDescending { it.screeningFrom.time }
        }
    }

}