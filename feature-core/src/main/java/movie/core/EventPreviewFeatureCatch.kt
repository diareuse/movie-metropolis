package movie.core

import movie.core.model.MoviePreview

class EventPreviewFeatureCatch(
    private val origin: EventPreviewFeature
) : EventPreviewFeature, Recoverable {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        runCatchingResult(result) {
            origin.get(it)
        }
    }

}