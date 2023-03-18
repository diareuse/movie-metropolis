package movie.core

import movie.core.model.MoviePreview
import java.util.Date
import kotlin.math.abs

class EventPreviewFeatureSort(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(): Result<Sequence<MoviePreview>> {
        return origin.get().map { items ->
            items.sortedBy { abs(Date().time - it.screeningFrom.time) }
        }
    }

}