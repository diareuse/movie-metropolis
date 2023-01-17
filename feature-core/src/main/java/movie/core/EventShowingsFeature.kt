package movie.core

import movie.core.model.MoviePreview

interface EventShowingsFeature {

    suspend fun get(): Result<Iterable<MoviePreview>>

    interface Factory {
        fun current(): EventShowingsFeature
        fun upcoming(): EventShowingsFeature
    }

}