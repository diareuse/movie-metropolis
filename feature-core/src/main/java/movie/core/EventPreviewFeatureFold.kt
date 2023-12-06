package movie.core

class EventPreviewFeatureFold(
    private vararg val options: EventPreviewFeature
) : EventPreviewFeature, Recoverable {

    override suspend fun get() = options.foldSimple { get() }

}