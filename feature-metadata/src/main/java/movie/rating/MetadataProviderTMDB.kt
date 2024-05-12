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
        val data = client.get().search(descriptor) ?: return null
        return MovieMetadata(
            id = data.id,
            rating = (data.rating * 10).toInt().toByte(),
            posterImageUrl = data.posterPath?.let(TMDB::image).orEmpty(),
            overlayImageUrl = data.backdropPath?.let(TMDB::image).orEmpty(),
            url = TMDB.url("/movie/${data.id}")
        )
    }

    private suspend fun HttpClient.search(descriptor: MovieDescriptor): SearchData? {
        val result = get(TMDB.api("/search/movie")) {
            url.parameters.apply {
                append("query", descriptor.name)
                append("include_adult", "false")
                append("primary_release_year", descriptor.year.toString())
            }
        }
        return result.body<ListResponse<SearchData>>().results.firstOrNull()
    }

}