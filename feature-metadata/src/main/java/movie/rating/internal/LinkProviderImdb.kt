package movie.rating.internal

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLParameter
import movie.rating.MovieDescriptor
import movie.rating.ResultNotFoundException
import movie.rating.internal.Correlation.Companion.correlate

internal class LinkProviderImdb(
    private val client: LazyHttpClient
) : LinkProvider {

    override suspend fun getLink(descriptor: MovieDescriptor): String {
        val (name, year) = descriptor
        val query = name.encodeURLParameter(true)
        val response = client.getOrCreate().get("https://www.imdb.com/find?q=$query")
        val body = response.bodyAsText()
        for (row in rows.findAll(body)) {
            if (!row.value.contains(year.toString())) continue
            for (title in title.findAll(row.value)) {
                val correlationFactor = title.groupValues[2] correlate name
                if (correlationFactor >= .8f) {
                    return "https://www.imdb.com" + title.groupValues[1]
                }
            }
        }
        throw ResultNotFoundException()
    }

    companion object {

        private val rows =
            Regex("<li\\s+?class=\"[ \\S]+?find-title-result[\\s\\S]+?\"[\\s\\S]+?>[\\s\\S]+?</li>")

        private val title =
            Regex("<a[\\s\\S]+?href=\"(\\S+?)\">([\\w .:&\\-]+)<\\/a>")

    }

}