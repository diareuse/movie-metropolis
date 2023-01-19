package movie.core

import movie.core.model.MoviePreview

class EventPreviewFeatureRequireNotEmpty(
    private val origin: EventPreviewFeature
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get {
            val output = it.map { items ->
                items.requireNotEmpty()
            }
            result(output)
        }
    }

    private fun <T> List<T>.requireNotEmpty() = apply {
        require(isNotEmpty())
    }

}