package movie.rating

internal class RatingProviderCsfd(
    override val client: LazyHttpClient,
    override val provider: LinkProvider
) : AbstractRatingProvider() {

    @Throws(ResultNotFoundException::class)
    override fun getRating(content: String): Byte {
        val result = rating.find(content) ?: throw ResultNotFoundException()
        return result.groupValues[1].toByte()
    }

    companion object {

        private val rating =
            Regex("(?:class=\"film-rating-average\"[\\s\\S]*?>)\\s+?(\\d+)%\\s+?<\\/")

    }

}