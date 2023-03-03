package movie.rating

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal abstract class AbstractRatingProvider : RatingProvider {

    abstract val client: LazyHttpClient
    abstract val provider: LinkProvider

    final override suspend fun get(descriptor: MovieDescriptor): AvailableRating? {
        val link = provider.getLink(descriptor)
        val rating = try {
            val content = client.getOrCreate().get(link).bodyAsText()
            getRating(content)
        } catch (e: ResultNotFoundException) {
            return null
        }
        return AvailableRating(rating, link)
    }

    @Throws(ResultNotFoundException::class)
    abstract fun getRating(content: String): Byte

}