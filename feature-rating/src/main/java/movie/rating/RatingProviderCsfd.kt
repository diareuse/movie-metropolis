package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal class RatingProviderCsfd(
    private val client: HttpClient,
    private val link: LinkProvider
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        val link = link.getLink(descriptor)
        return getRating(link)
    }

    private suspend fun getRating(link: String): Byte {
        val response = client.get(link)
        val body = response.bodyAsText()
        val result = rating.find(body) ?: throw ResultNotFoundException()
        return result.groupValues[1].toByte()
    }

    companion object {

        private val rating =
            Regex("(?:class=\"film-rating-average\"[\\s\\S]*?>)\\s+?(\\d+)%\\s+?<\\/")

    }

}