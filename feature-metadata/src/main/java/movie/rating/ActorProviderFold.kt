package movie.rating

internal class ActorProviderFold(
    private vararg val options: ActorProvider
) : ActorProvider {

    override suspend fun get(query: String): Actor {
        return options.firstNotNullOf { it.runCatching { get(query) }.getOrNull() }
    }

}