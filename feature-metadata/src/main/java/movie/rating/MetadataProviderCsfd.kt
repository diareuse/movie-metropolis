package movie.rating

import movie.rating.internal.AbstractMetadataProvider
import movie.rating.internal.LazyHttpClient
import movie.rating.internal.LinkProvider

internal class MetadataProviderCsfd(
    override val client: LazyHttpClient,
    override val provider: LinkProvider
) : AbstractMetadataProvider() {

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