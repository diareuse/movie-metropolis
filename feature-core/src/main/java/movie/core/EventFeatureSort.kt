package movie.core

import movie.core.model.MoviePreview
import java.util.Date
import kotlin.math.abs

class EventFeatureSort(
    private val origin: EventFeature
) : EventFeature by origin {

    override suspend fun getCurrent(): Result<Iterable<MoviePreview>> {
        return origin.getCurrent().sortedByTime()
    }

    override suspend fun getUpcoming(): Result<Iterable<MoviePreview>> {
        return origin.getUpcoming().sortedByTime()
    }

    // ---

    private fun Result<Iterable<MoviePreview>>.sortedByTime() = map { items ->
        items.sortedBy { abs(Date().time - it.screeningFrom.time) }
    }

}