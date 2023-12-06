package movie.core

import movie.core.model.MoviePreview

interface EventPreviewFeature {

    @Throws(Throwable::class)
    suspend fun get(): Sequence<MoviePreview>

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

}