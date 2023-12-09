package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import movie.rating.internal.LazyHttpClient
import movie.rating.model.ActorReference
import movie.rating.model.ActorResponse
import movie.rating.model.ListResponse
import java.util.Date
import java.util.Locale

internal class ActorProviderTMDB(
    private val client: LazyHttpClient
) : ActorProvider {

    override suspend fun get(query: String): Actor {
        val actor = client.getOrCreate().search(query) ?: error("Couldn't find '$query'")
        return Actor(
            id = actor.id,
            name = actor.name,
            popularity = actor.popularity.toInt(),
            image = actor.picture?.let(TMDB::image).orEmpty(),
            movies = actor.references.map {
                ActorReference(
                    id = it.id,
                    name = it.name.orEmpty(),
                    backdrop = it.backdrop?.let(TMDB::image).orEmpty(),
                    image = it.image?.let(TMDB::image).orEmpty(),
                    popularity = it.popularity.toInt(),
                    rating = it.rating.times(10).toInt().toByte(),
                    releasedAt = it.releasedAt ?: Date(0)
                )
            }
        )
    }

    private suspend fun HttpClient.search(query: String): ActorResponse? {
        val result = get(TMDB.api("/search/person")) {
            url.parameters.apply {
                append("query", query)
                append("include_adult", "false")
                append("language", Locale.getDefault().language)
            }
        }
        return result.body<ListResponse<ActorResponse>>().results.firstOrNull()
    }

}