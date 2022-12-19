package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLParameter

class RatingProviderRottenTomatoes(
    private val client: HttpClient
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        val link = getDetailLink(descriptor.name, descriptor.year)
        return getMaxScore(link)
    }

    private suspend fun getDetailLink(name: String, year: Int): String {
        val query = name.encodeURLParameter()
        val response = client.get("https://www.rottentomatoes.com/search?search=$query")
        val body = response.bodyAsText()
        for (result in results.findAll(body).toList().also { require(it.isNotEmpty()) }) {
            for (row in row.findAll(result.value).toList().also { require(it.isNotEmpty()) }) {
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

    private suspend fun getMaxScore(link: String): Byte {
        val response = client.get(link)
        val body = response.bodyAsText()
        val scoreBoard = score.find(body)?.value ?: throw ResultNotFoundException()
        return scores.findAll(scoreBoard)
            .flatMap { it.groupValues.drop(1) }
            .mapNotNull { it.toByteOrNull() }
            .maxByOrNull { it }
            ?: throw ResultNotFoundException()
    }


    companion object {

        private val results =
            Regex("<search-page-result.+?type=\"movie\".+?>[\\s\\S]+?<\\/search-page-result>")
        private val row =
            Regex("<search-page-media-row[\\s\\S]+?>[\\s\\S]+?<\\/search-page-media-row>")
        private val title =
            Regex("<a.+href=\"(.*?)\".+slot=\"title\".*?>\\s*([\\w .:&\\-]+)\\s*<\\/a>")
        private val score =
            Regex("<score-board[\\n\\r\\S ]+?>")
        private val scores =
            Regex("(?<=audiencescore|tomatometerscore)=\"(\\d+)\"")

    }

}