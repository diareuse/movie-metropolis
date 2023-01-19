package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.MoviePreview

class EventPreviewFeatureFold(
    private vararg val options: EventPreviewFeature
) : EventPreviewFeature, Recoverable {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        options.foldCatching { option ->
            option.get { result(it.onFailureThrow()) }
        }
    }

}