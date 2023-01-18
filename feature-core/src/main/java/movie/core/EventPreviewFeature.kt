package movie.core

import movie.core.model.MoviePreview

interface EventPreviewFeature {

    suspend fun get(): Result<Iterable<MoviePreview>>

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

}