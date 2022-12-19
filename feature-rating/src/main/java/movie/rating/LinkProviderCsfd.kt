package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.encodeURLParameter

class LinkProviderCsfd(
    private val client: HttpClient
): LinkProvider {

    override suspend fun getLink(descriptor: MovieDescriptor): String {
        val (name, year) = descriptor
        val query = name.encodeURLParameter()
        val response = client.get("https://www.csfd.cz/hledat/?q=$query")
        val body = response.bodyAsText()
        for (row in row.findAll(body).toList().also { println(it.joinToString { it.value }) }) {
            if (!row.value.contains(year.toString())) continue
            val title = title.find(row.value) ?: continue
            val correlationFactor = title.groupValues[2] correlate name
            if (correlationFactor >= .8f) {
                return "https://www.csfd.cz" + title.groupValues[1]
            }
        }
        throw ResultNotFoundException()
    }

    companion object {

        private val row =
            Regex("<article[\\s\\S]+?>[\\s\\S]+?<\\/article>")

        private val title =
            Regex("<a[ \\S]+?href=\"(.*?)\"[ \\S]+?class=\".*?film-title-name.*?\">([\\w .:&\\-]+?)</a>")

    }

}