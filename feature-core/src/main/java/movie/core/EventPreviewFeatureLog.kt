package movie.core

import movie.core.model.MoviePreview

class EventPreviewFeatureLog(
    private val origin: EventPreviewFeature,
    private val tag: String
) : EventPreviewFeature {

    override suspend fun get(): Sequence<MoviePreview> {
        println("<- $tag")
        return try {
            origin.get().also {
                println("-> $tag - OK")
            }
        } catch (e: Throwable) {
            println("-> $tag - err: ${e.stackTraceToString()}")
            throw e
        }
    }

}