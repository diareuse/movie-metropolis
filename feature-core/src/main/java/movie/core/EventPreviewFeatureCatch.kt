package movie.core

class EventPreviewFeatureCatch(
    private val origin: EventPreviewFeature
) : EventPreviewFeature, Recoverable {

    override suspend fun get() = wrap { origin.get() }

}