package movie.rating

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal class RatingProviderImdb(
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
        val rating = rating.find(body) ?: throw ResultNotFoundException()
        val ratingMultiplied = rating.groupValues[1].toFloat() * 10
        return ratingMultiplied.toInt().toByte()
    }

    companion object {

        private val rating =
            Regex("(?:\"aggregateRating\")[\\S ]+\"ratingValue\":([\\d.]+)")

    }

}