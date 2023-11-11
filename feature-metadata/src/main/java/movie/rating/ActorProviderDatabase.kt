package movie.rating

import movie.rating.database.ActorDao
import movie.rating.database.ActorReferenceDao
import movie.rating.model.ActorReference

internal class ActorProviderDatabase(
    private val actor: ActorDao,
    private val reference: ActorReferenceDao
) : ActorProvider {
    override suspend fun get(query: String): Actor {
        val actor = actor.selectBy(query) ?: error("Couldn't find actor '$query'")
        val refs = reference.selectRelated(actor.id)
        return Actor(
            id = actor.id,
            name = actor.name,
            popularity = actor.popularity,
            image = actor.image,
            movies = refs.map {
                ActorReference(
                    id = it.id,
                    name = it.name,
                    backdrop = it.backdrop,
                    image = it.image,
                    popularity = it.popularity,
                    rating = it.rating,
                    releasedAt = it.releasedAt
                )
            }
        )
    }
}