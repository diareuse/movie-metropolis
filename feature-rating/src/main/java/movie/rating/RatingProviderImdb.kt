package movie.rating

import movie.rating.internal.AbstractRatingProvider
import movie.rating.internal.LazyHttpClient
import movie.rating.internal.LinkProvider

internal class RatingProviderImdb(
    override val client: LazyHttpClient,
    override val provider: LinkProvider
) : AbstractRatingProvider() {

    @Throws(ResultNotFoundException::class)
    override fun getRating(content: String): Byte {
        val rating = rating.find(content) ?: throw ResultNotFoundException()
        val ratingMultiplied = rating.groupValues[1].toFloat() * 10
        return ratingMultiplied.toInt().toByte()
    }

    companion object {

        private val rating =
            Regex("(?:\"aggregateRating\")[\\S ]+\"ratingValue\":([\\d.]+)")

    }

}