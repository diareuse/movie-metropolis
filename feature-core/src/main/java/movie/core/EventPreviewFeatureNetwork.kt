package movie.core

import movie.core.adapter.MoviePreviewFromResponse
import movie.core.model.MoviePreview
import movie.core.nwk.EventService
import movie.core.nwk.model.ShowingType

class EventPreviewFeatureNetwork(
    private val service: EventService,
    private val type: ShowingType
) : EventPreviewFeature {

    override suspend fun get(): Result<Sequence<MoviePreview>> {
        return service.getMoviesByType(type)
            .map { it.body.asSequence().map(::MoviePreviewFromResponse) }
    }

}