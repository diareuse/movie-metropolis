package movie.rating

import movie.rating.database.ActorDao
import movie.rating.database.ActorReferenceConnection
import movie.rating.database.ActorReferenceConnectionDao
import movie.rating.database.ActorReferenceDao
import movie.rating.database.ActorReferenceStored
import movie.rating.database.ActorStored

internal class ActorProviderStoring(
    private val origin: ActorProvider,
    private val actor: ActorDao,
    private val connection: ActorReferenceConnectionDao,
    private val reference: ActorReferenceDao
) : ActorProvider {

    override suspend fun get(query: String): Actor {
        return origin.get(query).also {
            actor.insertOrUpdate(ActorStored(it.id, it.name, it.popularity, it.image, query))
            for (m in it.movies) {
                reference.insertOrUpdate(
                    ActorReferenceStored(
                        id = m.id,
                        name = m.name,
                        backdrop = m.backdrop,
                        image = m.image,
                        popularity = m.popularity,
                        rating = m.rating,
                        releasedAt = m.releasedAt
                    )
                )
                connection.insertOrUpdate(ActorReferenceConnection(it.id, m.id))
            }
        }
    }

}