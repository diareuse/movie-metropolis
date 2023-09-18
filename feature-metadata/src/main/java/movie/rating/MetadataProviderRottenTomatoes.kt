package movie.rating

import movie.rating.internal.AbstractMetadataProvider
import movie.rating.internal.LazyHttpClient
import movie.rating.internal.LinkProvider

internal class MetadataProviderRottenTomatoes(
    override val client: LazyHttpClient,
    override val provider: LinkProvider
) : AbstractMetadataProvider() {

    override fun getRating(content: String): Byte {
        val scoreBoard = score.find(content)?.value ?: throw ResultNotFoundException()
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