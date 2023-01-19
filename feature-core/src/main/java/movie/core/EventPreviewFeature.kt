package movie.core

import movie.core.model.MoviePreview

interface EventPreviewFeature {

    suspend fun get(result: ResultCallback<List<MoviePreview>>)

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

}