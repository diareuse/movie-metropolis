package movie.core

import movie.core.model.MoviePreview

interface EventPreviewFeature {

    suspend fun get(): Result<Sequence<MoviePreview>>

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

}