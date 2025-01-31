package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import movie.rating.model.ListResponse
import movie.rating.model.SearchData
import javax.inject.Provider

class MetadataProviderTMDB(
    private val client: Provider<HttpClient>
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor): MovieMetadata? {
        val data = with(client.get()) {
            var out = search(descriptor.name, descriptor.year.toString())
            out = out ?: search(descriptor.name, null)
            out = out ?: search(descriptor.name.substringAfter(':'), descriptor.year.toString())
            out
        } ?: return null
        return MovieMetadata(
            id = data.id,
            rating = (data.rating * 10).toInt().toByte(),
            posterImageUrl = data.posterPath?.let(TMDB::image).orEmpty(),
            overlayImageUrl = data.backdropPath?.let(TMDB::image).orEmpty(),
            url = TMDB.url("/movie/${data.id}")
        )
    }

    private suspend fun HttpClient.search(name: String, year: String?): SearchData? {
        val result = get(TMDB.api("/search/movie")) {
            url.parameters.apply {
                append("query", name)
                append("include_adult", "false")
                if (year != null)
                    append("primary_release_year", year)
            }
        }
        return result.body<ListResponse<SearchData>>().results.firstOrNull()
    }

}