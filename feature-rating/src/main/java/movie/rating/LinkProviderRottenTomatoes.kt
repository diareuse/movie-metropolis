package movie.rating

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLParameter
import movie.rating.Correlation.Companion.correlate

internal class LinkProviderRottenTomatoes(
    private val client: LazyHttpClient
) : LinkProvider {

    override suspend fun getLink(descriptor: MovieDescriptor): String {
        val (name, year) = descriptor
        val query = name.encodeURLParameter()
        val response = client.getOrCreate()
            .get("https://www.rottentomatoes.com/search?search=$query")
        val body = response.bodyAsText()
        for (result in results.findAll(body)) {
            for (row in row.findAll(result.value)) {
                if (!row.value.contains(year.toString())) continue
                val title = title.find(row.value) ?: continue
                val correlationFactor = title.groupValues[2] correlate name
                if (correlationFactor >= .8f) {
                    return title.groupValues[1]
                }
            }
        }
        throw ResultNotFoundException()
    }

    companion object {

        private val results =
            Regex("<search-page-result.+?type=\"movie\".+?>[\\s\\S]+?<\\/search-page-result>")
        private val row =
            Regex("<search-page-media-row[\\s\\S]+?>[\\s\\S]+?<\\/search-page-media-row>")
        private val title =
            Regex("<a.+href=\"(.*?)\".+slot=\"title\".*?>\\s*([\\w .:&\\-]+)\\s*<\\/a>")

    }

}