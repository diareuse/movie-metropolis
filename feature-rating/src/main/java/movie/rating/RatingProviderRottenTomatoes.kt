package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class RatingProviderRottenTomatoes(
    private val client: HttpClient,
    private val link: LinkProvider
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        val link = link.getLink(descriptor)
        return getMaxScore(link)
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

        private val score =
            Regex("<score-board[\\n\\r\\S ]+?>")
        private val scores =
            Regex("(?<=audiencescore|tomatometerscore)=\"(\\d+)\"")

    }

}