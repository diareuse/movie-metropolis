package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLParameter

class RatingProviderImdb(
    private val client: HttpClient
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        val link = getDetailLink(descriptor.name, descriptor.year)
        return getRating(link)
    }

    private suspend fun getDetailLink(name: String, year: Int): String {
        val query = name.encodeURLParameter()
        val response = client.get("https://www.imdb.com/find?q=$query")
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

    private suspend fun getRating(link: String): Byte {
        val response = client.get(link)
        val body = response.bodyAsText()
        return rating.findAll(body)
            .map { it.groupValues[1].toFloat() * 10 }
            .map { it.toInt().toByte() }
            .maxByOrNull { it }
            ?: throw ResultNotFoundException()
    }

    companion object {

        private val rows =
            Regex("<li\\s+?class=\"[ \\S]+?find-title-result[\\s\\S]+?\"[\\s\\S]+?>[\\s\\S]+?</li>")

        private val title =
            Regex("<a[\\s\\S]+?href=\"(\\S+?)\">([\\w .:&\\-]+)<\\/a>")

        private val rating =
            Regex("\"ratingValue\":([\\d.]+)")

    }

}