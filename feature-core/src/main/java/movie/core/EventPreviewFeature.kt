package movie.core

import movie.core.model.MoviePreview

interface EventPreviewFeature {

    suspend fun get(result: ResultCallback<List<MoviePreview>>)

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

    companion object {

        suspend fun EventPreviewFeature.get(): Result<List<MoviePreview>> {
            var out: Result<List<MoviePreview>> = Result.failure(IllegalStateException())
            get { out = it }
            return out
        }

    }

}