package movie.core

import movie.core.model.MoviePreview
import movie.core.util.requireNotEmpty

class EventPreviewFeatureRequireNotEmpty(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get(result.map {
            it.requireNotEmpty()
        })
    }

}